import axios from 'axios';
import type { AxiosRequestConfig, AxiosPromise, Method } from 'axios';
import { ElLoading, LoadingOptions, ElNotification } from 'element-plus';
import { clearRefreshToken, clearToken, getRefreshToken, getToken, TokenPrefix } from '../../auth';
import { anyObj, ApiPromise, Options } from './type';
import { showMessage } from './status';
import { refresh } from '/@/api/auth';

const pendingMap = new Map();
const loadingInstance: LoadingInstance = {
  target: null,
  count: 0,
};

// 无需携带凭证url白名单
// todo 白名单需要优化,后台动态返回
const whiteList = ['/auth/account/password', '/auth/account/refresh'];

/*
 * 根据运行环境获取基础请求URL
 */
export const getUrl = (): string => {
  const value: string = import.meta.env.VITE_APP_API_BASEURL as string;
  return value;
};

/*
 * 根据运行环境获取基础请求URL的端口
 */
export const getUrlPort = (): string => {
  const url = getUrl();
  return new URL(url).port;
};

/*
 * 创建Axios
 * 默认开启`reductDataFormat(简洁响应)`,返回类型为`ApiPromise`
 * 关闭`reductDataFormat`,返回类型则为`AxiosPromise`
 */
function createAxios(axiosConfig: AxiosRequestConfig, options: Options = {}, loading: LoadingOptions = {}): ApiPromise | AxiosPromise {
  const Axios = axios.create({
    baseURL: getUrl(),
    timeout: 1000 * 10,
    headers: {
      server: true,
    },
    responseType: 'json',
  });

  options = Object.assign(
    {
      CancelDuplicateRequest: true, // 是否开启取消重复请求, 默认为 true
      loading: false, // 是否开启loading层效果, 默认为false
      reductDataFormat: true, // 是否开启简洁的数据结构响应, 默认为true
      showErrorMessage: true, // 是否开启接口错误信息展示,默认为true
      showCodeMessage: true, // 是否开启code不为1时的信息提示, 默认为true
      showSuccessMessage: false, // 是否开启code为1时的信息提示, 默认为false
      anotherToken: '', // 当前请求使用另外的用户token
    },
    options,
  );

  // 请求拦截
  Axios.interceptors.request.use(
    (config) => {
      removePending(config);
      options.CancelDuplicateRequest && addPending(config);
      // 创建loading实例
      if (options.loading) {
        loadingInstance.count++;
        if (loadingInstance.count === 1) {
          loadingInstance.target = ElLoading.service(loading);
        }
      }

      // 自动携带token
      if (config.headers && !whiteList.includes(config.url as string)) {
        const token = getToken();
        if (token) (config.headers as anyObj).Authorization = `${TokenPrefix}${token}`;
        // 其他用户token
        const userToken = options.anotherToken || '';
        if (userToken) (config.headers as anyObj)['Authorization'] = userToken;
      }

      return config;
    },
    (error) => {
      return Promise.reject(error);
    },
  );

  // 响应拦截
  Axios.interceptors.response.use(
    (response) => {
      removePending(response.config);
      options.loading && closeLoading(options); // 关闭loading

      if (response.config.responseType == 'json') {
        if (response.data && response.data.code !== 0) {
          if (response.data.code == 401) {
            console.log('token过期');
          }
          if (options.showCodeMessage) {
            ElNotification({
              type: 'error',
              message: response.data.msg,
            });
          }
          // 自动跳转到路由name或path，仅限server端返回302的情况
          if (response.data.code == 302) {
            console.log('302 跳转');
          }
          // code不等于1, 页面then内的具体逻辑就不执行了
          return Promise.reject(response.data);
        } else if (options.showSuccessMessage && response.data && response.data.code == 0) {
          ElNotification({
            message: response.data.msg ? response.data.msg : '操作成功',
            type: 'success',
          });
        }
      }

      return options.reductDataFormat ? response.data : response;
    },
    (error) => {
      if (error.config.responseType == 'json') {
        if (error.response.data.code == 401) {
          console.log('token过期');
          const refreshTokenParam = getRefreshToken();
          if (refreshTokenParam === null) return;
          refresh(refreshTokenParam)
            .then((res) => {
              console.log(res);
              return Axios(error.config);
            })
            .catch((err) => {
              clearToken();
              clearRefreshToken();
              console.log(err);
              return Axios(error.config);
            });
        }
        if (options.showCodeMessage) {
          ElNotification({
            type: 'error',
            message: error.response.data.msg,
          });
        }
      }
      error.config && removePending(error.config);
      options.loading && closeLoading(options); // 关闭loading
      options.showErrorMessage && httpErrorStatusHandle(error); // 处理错误状态码
      return Promise.reject(error); // 错误继续返回给到具体页面
    },
  );
  return options.reductDataFormat ? (Axios(axiosConfig) as ApiPromise) : (Axios(axiosConfig) as AxiosPromise);
}

export default createAxios;

/**
 * 处理异常
 * @param {*} error
 */
function httpErrorStatusHandle(error: any) {
  // 处理被取消的请求
  if (axios.isCancel(error)) return console.error(error.message);
  let message = '';
  if (error && error.response) {
    message = showMessage(error.response.status);
  }
  if (error.message.includes('timeout')) message = '请求超时, 请稍后再试!';
  if (error.message.includes('Network')) message = window.navigator.onLine ? '服务器错误' : '网络错误, 请稍后再试!';

  ElNotification({
    type: 'error',
    message,
  });
}

/**
 * 关闭Loading层实例
 */
function closeLoading(options: Options) {
  if (options.loading && loadingInstance.count > 0) loadingInstance.count--;
  if (loadingInstance.count === 0) {
    loadingInstance.target.close();
    loadingInstance.target = null;
  }
}

/**
 * 储存每个请求的唯一cancel回调, 以此为标识
 */
function addPending(config: AxiosRequestConfig) {
  const pendingKey = getPendingKey(config);
  config.cancelToken =
    config.cancelToken ||
    new axios.CancelToken((cancel) => {
      if (!pendingMap.has(pendingKey)) {
        pendingMap.set(pendingKey, cancel);
      }
    });
}

/**
 * 删除重复的请求
 */
function removePending(config: AxiosRequestConfig) {
  const pendingKey = getPendingKey(config);
  if (pendingMap.has(pendingKey)) {
    const cancelToken = pendingMap.get(pendingKey);
    cancelToken(pendingKey);
    pendingMap.delete(pendingKey);
  }
}

/**
 * 生成每个请求的唯一key
 */
function getPendingKey(config: AxiosRequestConfig) {
  let { data } = config;
  const { url, method, params, headers } = config;
  if (typeof data === 'string') data = JSON.parse(data); // response里面返回的config.data是个字符串对象
  return [
    url,
    method,
    headers && (headers as anyObj).batoken ? (headers as anyObj).batoken : '',
    headers && (headers as anyObj)['ba-user-token'] ? (headers as anyObj)['ba-user-token'] : '',
    JSON.stringify(params),
    JSON.stringify(data),
  ].join('&');
}

/**
 * 根据请求方法组装请求数据/参数
 */
export function requestPayload(method: Method, data: anyObj) {
  if (method == 'GET') {
    return {
      params: data,
    };
  } else if (method == 'POST') {
    return {
      data: data,
    };
  }
}

interface LoadingInstance {
  target: any;
  count: number;
}

/*
 * 感谢掘金@橙某人提供的思路和分享
 * 本axios封装详细解释请参考：https://juejin.cn/post/6968630178163458084?share_token=7831c9e0-bea0-469e-8028-b587e13681a8#heading-27
 */
