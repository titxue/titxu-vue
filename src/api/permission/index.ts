// 权限问题后期增加
import { ResResultData } from '../types';
import { NavList } from './types';
import { get, post } from '/@/utils/http/axios';
// import axios from 'axios';
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
const getNav = async () => get<ResResultData<NavList>>({ url: URL.nav });
// 所有权限列表
const getPermissionList = async () => get<ResResultData<any>>({ url: URL.list });
// 获取菜单列表
const getMenu = async () => get<ResResultData<any>>({ url: URL.selectMenu });
// 获取权限信息
const getPermissionInfo = async (id: string) => get<ResResultData<any>>({ url: URL.info + id });
// 保存权限
const savePermission = async (data: any) => post<ResResultData<any>>({ url: URL.save, data: data });
// 修改权限
const updatePermission = async (data: any) => post<ResResultData<any>>({ url: URL.update, data: data });
// 删除权限
const deletePermission = async (id: string) => post<ResResultData<any>>({ url: URL.delete + id });
// 禁用权限
const disablePermission = async (id: string) => get<ResResultData<any>>({ url: URL.disable + id });
export { getNav, getPermissionList, getMenu, getPermissionInfo, savePermission, updatePermission, deletePermission, disablePermission };
