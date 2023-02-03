import { defineStore } from 'pinia';
import {
  deletePermission,
  disablePermission,
  getMenu,
  getNav,
  getPermissionInfo,
  getPermissionList,
  getPermissionTree,
  savePermission,
  updatePermission,
} from '/@/api/permission';
import { MenuStoreType, PermissionStateType, PermissionType } from './types';
import { MenuType } from '/@/api/permission/types';
import { tranListToTreeData } from '/@/utils';

export const usePermissionStore = defineStore('permission', {
  state: (): PermissionStateType => ({
    navList: [] as MenuType[],
    parentMenuList: [] as MenuType[],
    menuList: [] as MenuStoreType[],
    permissions: [] as string[],
    permissionTree: [] as PermissionType[],
    permissionInfo: {} as any,
  }),
  getters: {
    getPermissions(state: PermissionStateType): string[] {
      if (state.permissions?.length === 0 || state.permissions === undefined) return [];
      return state.permissions;
    },
    getMenuList(state: PermissionStateType): MenuStoreType[] {
      if (state.menuList?.length === 0 || state.menuList === undefined) return [];
      return state.menuList;
    },
  },
  actions: {
    // 刷新权限信息
    async refreshTable() {
      // 清空菜单信息
      this.$reset();
      // 重新获取权限信息
      await this.setMenuList();
      await this.setParentMenuList();
      await this.setNavList();
    },
    // 编辑菜单
    async editMenu(data: MenuType): Promise<boolean> {
      const { code } = await updatePermission(data);
      if (code !== 0) return false;
      return true;
    },
    // 新增菜单
    async addMenu(data: MenuType): Promise<boolean> {
      const { code } = await savePermission(data);
      if (code !== 0) return false;
      return true;
    },
    // 获取父目录列表
    async setParentMenuList() {
      if (this.parentMenuList.length !== 0) return;
      const { code, data } = await getPermissionList();
      if (code !== 0) return;
      if (data === undefined) return;
      // 删除根节点
      // data.shift();
      // 删除类型不为目录的节点
      const list = data.filter((item: MenuType) => item.permissionType === '0');
      this.$patch({ parentMenuList: list });
    },
    // 获取权限树
    async setPermissionTree() {
      if (this.permissionTree.length !== 0) return;
      const { code, data } = await getPermissionTree();
      if (code !== 0) return;
      this.$patch({ permissionTree: data });
    },

    // 获取导航信息信息
    async setNavList() {
      // 如果有缓存直接返回
      if (this.navList.length !== 0) return;

      const { code, data } = await getNav();
      if (code !== 0) return;
      if (data === undefined) return;

      const { menuList, permissions } = data;
      if (!menuList) return;
      if (!permissions) return;

      this.$patch({ navList: menuList, permissions: permissions });
    },
    async removePermission(id: string): Promise<boolean> {
      const { code } = await deletePermission(id);
      if (code !== 0) return false;
      return true;
    },

    // 获取菜单信息
    async setMenuList() {
      if (this.menuList.length !== 0) return;
      const { code, data } = await getMenu();
      if (code !== 0) return;
      if (data === undefined) return;
      // 删除无用的属性
      data.forEach((item: MenuType) => {
        delete item.subList;
      });
      // 数组转换为树形结构
      const menuList: MenuStoreType[] = tranListToTreeData(data, '0');
      this.$patch({ menuList: menuList });
    },
    async setPermissionInfoById(id: string) {
      const { code, data } = await getPermissionInfo(id);
      if (code !== 0) return;

      this.$patch({ permissionInfo: data });
    },
    // 禁用菜单
    async disableMenu(id: string): Promise<boolean> {
      const { code } = await disablePermission(id);
      if (code !== 0) return false;
      this.refreshTable();
      return true;
    },
    // 设置权限的信息
    setInfo(partial: Partial<any>) {
      this.$patch(partial);
    },
    // 重置权限信息
    resetInfo() {
      this.$reset();
    },
  },
  persist: true,
});
