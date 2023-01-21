import { ResResultData } from '../types';
import { LoginData, LoginDataType } from './types';
import { del, post } from '/@/utils/http/axios';

enum URL {
  login = '/auth/account/login',
  refresh = '/auth/account/refresh',
  logout = '/auth/account/logout',
}

const login = async (data: LoginData) => post<ResResultData<LoginDataType>>({ url: URL.login, data });

// 刷新token 用于token过期后刷新token
const refresh = async (params: string) => post<any>({ url: URL.refresh, params: { refreshToken: params } });

const logout = async () => del<any>({ url: URL.logout });

export { login, refresh, logout };
