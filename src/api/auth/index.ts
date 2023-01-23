import { LoginData, LoginDataType } from './types';
import { del, post } from '/@/utils/http/axios/http';
import { ApiPromise } from '/@/utils/http/axios/type';

enum URL {
  login = '/auth/account/login',
  refresh = '/auth/account/refresh',
  logout = '/auth/account/logout',
}

const login = async (data: LoginData): ApiPromise<LoginDataType> => post({ url: URL.login, data });

// 刷新token 用于token过期后刷新token
const refresh = async (params: string): ApiPromise<any> => post({ url: URL.refresh, params: { refreshToken: params } });

const logout = async (): ApiPromise<any> => del({ url: URL.logout });

export { login, refresh, logout };
