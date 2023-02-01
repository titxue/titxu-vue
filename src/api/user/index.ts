// 权限问题后期增加
import { get, post } from '/@/utils/http/axios';
import { PageUserInfoType, ReqUserCreateParams, ReqUserUpdateParams, UserInfoType, _UserInfoType } from './types';
import { PagingArgumentsType } from '../types';
import { ApiPromise } from '/@/utils/http/axios/type';
// import axios from 'axios';
enum URL {
  list = '/sys/user/list',
  update = '/sys/user/update',
  delete = '/sys/user/delete',
  disable = '/sys/user/disable/',
  info = '/sys/user/info/',
  save = '/sys/user/save',
}
// 保存用户信息
const saveUserInfo = async (data: ReqUserCreateParams): ApiPromise => post({ url: URL.save, data });

// 修改用户信息
const updateUser = async (data: ReqUserUpdateParams): ApiPromise => post({ url: URL.update, data });

// 删除用户
const deleteUser = async (id: string[]): ApiPromise => post({ url: URL.delete, data: id });

// 修改用户状态
const disableUser = async (id: string): ApiPromise => post({ url: URL.disable + id });

// 根据id获取用户信息
const getUserInfoById = async (id: string): ApiPromise<UserInfoType> => get({ url: URL.info + id });

// 获取用户信息
const getUserInfo = async (): ApiPromise<_UserInfoType> => get({ url: URL.info });

// 获取用户列表
const getUserList = async (params?: PagingArgumentsType): ApiPromise<PageUserInfoType> => get({ url: URL.list, params });

export { getUserInfoById, getUserInfo, getUserList, deleteUser, updateUser, saveUserInfo, disableUser };
