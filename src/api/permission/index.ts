// 权限问题后期增加
import { MenuType, NavListType } from './types';
import { get, post } from '../../utils/http/axios';
import { ApiPromise } from '/@/utils/http/axios/type';
enum URL {
  nav = '/sys/permission/nav',
  list = '/sys/permission/list',
  selectMenu = '/sys/permission/selectMenu',
  info = '/sys/permission/info/',
  save = '/sys/permission/save',
  update = '/sys/permission/update',
  delete = '/sys/permission/delete/',
  disable = '/sys/permission/disable/',
}

// 获取导航菜单
const getNav = async (): ApiPromise<NavListType> => get({ url: URL.nav });
// 所有权限列表
const getPermissionList = async (): ApiPromise<MenuType[]> => get({ url: URL.list });
// 获取菜单列表
const getMenu = async (): ApiPromise<MenuType[]> => get({ url: URL.selectMenu });
// 获取权限信息
const getPermissionInfo = async (id: string): ApiPromise<any> => get({ url: URL.info + id });
// 保存权限
const savePermission = async (data: any): ApiPromise<any> => post({ url: URL.save, data: data });
// 修改权限
const updatePermission = async (data: any): ApiPromise<any> => post({ url: URL.update, data: data });
// 删除权限
const deletePermission = async (id: string): ApiPromise<any> => post({ url: URL.delete + id });
// 禁用权限
const disablePermission = async (id: string): ApiPromise<any> => post({ url: URL.disable + id });
export { getNav, getPermissionList, getMenu, getPermissionInfo, savePermission, updatePermission, deletePermission, disablePermission };
