export interface RequestOptions {
  // Whether to process the request result
  isTransformResponse?: boolean;
}

// 返回res.data的interface
export interface IResponse<T = any> {
  code: number | string;
  data: T;
  message: string;
  status: string | number;
}

/**用户登录 */
export interface ILogin {
  /** 账户名称 */
  mobile: string;
  /** 账户密码 */
  password: string;
}

export interface ApiResponse<T = any> {
  code: number;
  data?: T;
  msg: string;
}
export interface anyObj {
  [key: string]: any;
}
export interface Options {
  // 是否开启取消重复请求, 默认为 true
  CancelDuplicateRequest?: boolean;
  // 是否开启loading层效果, 默认为false
  loading?: boolean;
  // 是否开启简洁的数据结构响应, 默认为true
  reductDataFormat?: boolean;
  // 是否开启接口错误信息展示,默认为true
  showErrorMessage?: boolean;
  // 是否开启code不为0时的信息提示, 默认为true
  showCodeMessage?: boolean;
  // 是否开启code为0时的信息提示, 默认为false
  showSuccessMessage?: boolean;
  // 当前请求使用另外的用户token
  anotherToken?: string;
}
export interface IWindow {
  existLoading?: boolean;
  lazy?: NodeJS.Timer;
  unique?: number;
  tokenRefreshing?: boolean;
  requests?: Function[];
  eventSource?: EventSource;
  loadLangHandle?: Record<string, any>;
}
export type ApiPromise<T = any> = Promise<ApiResponse<T>>;
