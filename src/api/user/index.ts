// 权限问题后期增加
import { get, post } from '/@/utils/http/axios';
import { UserState } from '/@/store/modules/user/types';
// import axios from 'axios';
enum URL {
  login = '/auth/account/password',
  logout = '/auth/account/logout',
  profile = '/auth/account/profile',
}
interface LoginRes {
  token: string;
}

export interface LoginData {
  mobile: string;
  password: string;
}

const getUserProfile = async () => get<UserState>({ url: URL.profile });
const login = async (data: LoginData) => post<any>({ url: URL.login, data });
const logout = async () => post<LoginRes>({ url: URL.logout });
export { getUserProfile, logout, login };
