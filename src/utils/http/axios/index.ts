import { AxiosRequestConfig } from 'axios';
import { ApiPromise, Options } from './type';
import createAxios from '/@/utils/http/axios/axios';

// GET 请求封装
function get(config: AxiosRequestConfig, options?: Options): ApiPromise {
  return createAxios({ ...config, method: 'GET' }, options) as ApiPromise;
}

// POST 请求封装
function post(config: AxiosRequestConfig, options?: Options): ApiPromise {
  return createAxios({ ...config, method: 'POST' }, options) as ApiPromise;
}

// PUT 请求封装
function put(config: AxiosRequestConfig, options?: Options): ApiPromise {
  return createAxios({ ...config, method: 'PUT' }, options) as ApiPromise;
}

// DELETE 请求封装
function del(config: AxiosRequestConfig, options?: Options): ApiPromise {
  return createAxios({ ...config, method: 'DELETE' }, options) as ApiPromise;
}

export { get, post, put, del };
