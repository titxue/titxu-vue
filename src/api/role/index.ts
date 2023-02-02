import { get, post } from '/@/utils/http/axios';
import { RoleResultType, RoleSaveOrUpdateType, RoleType } from './types';
import { ApiPromise } from '/@/utils/http/axios/type';
import { PageResultType, PagingArgumentsType } from '../types';
enum URL {
  info = '/sys/role/info/',
  all = '/sys/role/select',
  list = '/sys/role/list',
  save = '/sys/role/save',
  update = '/sys/role/update',
  delete = '/sys/role/delete',
  disable = '/sys/role/disable/',
}

// 获取角色信息
const getRoleInfoById = async (id: string): ApiPromise<RoleType> => get({ url: URL.info + id });
// 获取全部角色列表
const getRoleAll = async (): ApiPromise<RoleType[]> => get({ url: URL.all });
// 所有角色分页列表
const getRoleList = async (params?: PagingArgumentsType): ApiPromise<PageResultType<RoleResultType>> => get({ url: URL.list, params });
// 保存角色
const saveRole = async (data: RoleSaveOrUpdateType): ApiPromise<any> => post({ url: URL.save, data: data });
// 修改角色
const updateRole = async (data: RoleSaveOrUpdateType): ApiPromise<any> => post({ url: URL.update, data: data });
// 删除角色
const deleteRoleByIds = async (ids: string[]): ApiPromise<any> => post({ url: URL.delete, data: { roleIds: ids } });
// 禁用角色
const disableRole = async (id: string): ApiPromise<any> => post({ url: URL.disable + id });

export { getRoleInfoById, getRoleAll, getRoleList, saveRole, updateRole, deleteRoleByIds, disableRole };
