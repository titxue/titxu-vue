import axios from 'axios';
import { ElLoading, ElNotification } from 'element-plus';
import { getRefreshToken, getToken, removeToken, TokenPrefix } from '../../auth';
import { showMessage } from './status';
import { refresh } from '/@/api/auth';
const pendingMap = new Map();
const loadingInstance = {
  target: null,
  count: 0,
};
const whiteList = ['/auth/account/password', '/auth/account/refresh'];
export const getUrl = () => {
  const value = import.meta.env.VITE_APP_API_BASEURL;
  return value;
};
export const getUrlPort = () => {
  const url = getUrl();
  return new URL(url).port;
};
function createAxios(axiosConfig, options = {}, loading = {}) {
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
      CancelDuplicateRequest: true,
      loading: false,
      reductDataFormat: true,
      showErrorMessage: true,
      showCodeMessage: true,
      showSuccessMessage: false,
      anotherToken: '',
    },
    options,
  );
  Axios.interceptors.request.use(
    (config) => {
      removePending(config);
      options.CancelDuplicateRequest && addPending(config);
      if (options.loading) {
        loadingInstance.count++;
        if (loadingInstance.count === 1) {
          loadingInstance.target = ElLoading.service(loading);
        }
      }
      if (config.headers && !whiteList.includes(config.url)) {
        const token = getToken();
        if (token) config.headers.Authorization = `${TokenPrefix}${token}`;
        const userToken = options.anotherToken || '';
        if (userToken) config.headers['Authorization'] = userToken;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    },
  );
  Axios.interceptors.response.use(
    (response) => {
      removePending(response.config);
      options.loading && closeLoading(options);
      if (response.config.responseType == 'json') {
        if (response.data && response.data.code !== 0) {
          if (response.data.code == 401) {
            const refresh_token = getRefreshToken();
            refresh(refresh_token)
              .then((res) => {
                console.log(res);
                return Axios(response.config);
              })
              .catch((err) => {
                removeToken();
                console.log(err);
                location.reload();
                return Axios(response.config);
              });
          }
          if (options.showCodeMessage) {
            ElNotification({
              type: 'error',
              message: response.data.msg,
            });
          }
          if (response.data.code == 302) {
            console.log('302 跳转');
          }
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
      error.config && removePending(error.config);
      options.loading && closeLoading(options);
      options.showErrorMessage && httpErrorStatusHandle(error);
      return Promise.reject(error);
    },
  );
  return options.reductDataFormat ? Axios(axiosConfig) : Axios(axiosConfig);
}
export default createAxios;
function httpErrorStatusHandle(error) {
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
function closeLoading(options) {
  if (options.loading && loadingInstance.count > 0) loadingInstance.count--;
  if (loadingInstance.count === 0) {
    loadingInstance.target.close();
    loadingInstance.target = null;
  }
}
function addPending(config) {
  const pendingKey = getPendingKey(config);
  config.cancelToken =
    config.cancelToken ||
    new axios.CancelToken((cancel) => {
      if (!pendingMap.has(pendingKey)) {
        pendingMap.set(pendingKey, cancel);
      }
    });
}
function removePending(config) {
  const pendingKey = getPendingKey(config);
  if (pendingMap.has(pendingKey)) {
    const cancelToken = pendingMap.get(pendingKey);
    cancelToken(pendingKey);
    pendingMap.delete(pendingKey);
  }
}
function getPendingKey(config) {
  let { data } = config;
  const { url, method, params, headers } = config;
  if (typeof data === 'string') data = JSON.parse(data);
  return [
    url,
    method,
    headers && headers.batoken ? headers.batoken : '',
    headers && headers['ba-user-token'] ? headers['ba-user-token'] : '',
    JSON.stringify(params),
    JSON.stringify(data),
  ].join('&');
}
export function requestPayload(method, data) {
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
//# sourceMappingURL=axios.js.map
