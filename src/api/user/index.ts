// 权限问题后期增加
import { get, post } from '/@/utils/http/axios';
import { UserState } from '/@/store/modules/user/types';
import { LoginDataType, ReqListParams, ReqUserUpdateParams, ResultListType, ResultOkType, ResultType } from './types';
// import axios from 'axios';
enum URL {
  login = '/auth/account/login',
  refresh = '/auth/account/refresh',
  logout = '/auth/account/logout',
  profile = '/auth/account/profile',
  list = '/sys/user/list',
  update = '/sys/user/update',
  delete = '/sys/user/delete',
  disable = '/sys/user/disable/',
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
const deleteUser = async (id: string[]) => post<ResultOkType>({ url: URL.delete, data: id });
// 修改用户状态
const disableUser = async (id: string) => post<ResultOkType>({ url: URL.disable + id });

const getUserProfile = async () => get<UserState>({ url: URL.profile });

// 获取用户列表
const getUserList = async (params?: ReqListParams) => get<ResultListType>({ url: URL.list, params });
const login = async (data: LoginData) => post<ResultType<LoginDataType>>({ url: URL.login, data });
// 刷新token 用于token过期后刷新token
const refresh = async (params: string) => post<any>({ url: URL.refresh, params: { refreshToken: params } });
const logout = async () => post<LoginRes>({ url: URL.logout });
export { getUserProfile, logout, login, getUserList, refresh, deleteUser, updateUser, disableUser };
