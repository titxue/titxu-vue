// 权限问题后期增加
import { get, post } from '/@/utils/http/axios';
import { ReqUserUpdateParams, ResultListType, ResultOkType } from './types';
import { PagingArgumentsType } from '../types';
// import axios from 'axios';
enum URL {
  list = '/sys/user/list',
  update = '/sys/user/update',
  delete = '/sys/user/delete',
  disable = '/sys/user/disable/',
  info = '/sys/user/info/',
}

// 修改用户信息
const updateUser = async (data: ReqUserUpdateParams) => post<ResultOkType>({ url: URL.update, data });
// 删除用户
const deleteUser = async (id: string[]) => post<ResultOkType>({ url: URL.delete, data: id });
// 修改用户状态
const disableUser = async (id: string) => post<ResultOkType>({ url: URL.disable + id });

const getUserInfoById = async (id: string) => get<any>({ url: URL.info + id });

// 获取用户列表
const getUserList = async (params?: PagingArgumentsType) => get<ResultListType>({ url: URL.list, params });

export { getUserInfoById, getUserList, deleteUser, updateUser, disableUser };
