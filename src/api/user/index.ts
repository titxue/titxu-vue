// 权限问题后期增加
import { get, post } from '/@/utils/http/axios';
import { UserState } from '/@/store/modules/user/types';
import { ReqListParams, ReqUserUpdateParams, ResultListType, ResultOkType } from './types';
// import axios from 'axios';
enum URL {
  login = '/auth/account/password',
  refresh = '/auth/account/refresh',
  logout = '/auth/account/logout',
  profile = '/auth/account/profile',
  list = '/sys/user/list',
  update = '/sys/user/update',
  delete = '/sys/user/delete',
}
interface LoginRes {
  token: string;
}

export interface LoginData {
  mobile: string;
  password: string;
}

// 修改用户信息
const updateUser = async (data: ReqUserUpdateParams) => post<ResultOkType>({ url: URL.update, data });
// 删除用户
const deleteUser = async (id: number[]) => post<ResultOkType>({ url: URL.delete, data: { id } });

const getUserProfile = async () => get<UserState>({ url: URL.profile });
// 获取用户列表
const getUserList = async (params?: ReqListParams) => get<ResultListType>({ url: URL.list, params });
const login = async (data: LoginData) => post<any>({ url: URL.login, data });
// 刷新token
const refresh = async (refreshToken: string) => post<any>({ url: URL.refresh, data: { refreshToken } });
const logout = async () => post<LoginRes>({ url: URL.logout });
export { getUserProfile, logout, login, getUserList,refresh };
