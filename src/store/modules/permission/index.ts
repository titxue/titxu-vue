import { defineStore } from 'pinia';
import { getMenu, getNav } from '/@/api/permission';
import { MenuStoreType, PermissionStateType } from './types';
import { MenuType } from '/@/api/permission/types';
import { tranListToTreeData } from '/@/utils';

export const usePermissionStore = defineStore('permission', {
  state: (): PermissionStateType => ({
    navList: [] as MenuType[],
    menuList: [] as MenuStoreType[],
    permissions: [] as string[],
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
    // 设置权限的信息
    setInfo(partial: Partial<any>) {
      this.$patch(partial);
    },
    // 重置权限信息
    resetInfo() {
      this.$reset();
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

    // 获取菜单信息
    async setMenuList() {
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
  },
  persist: true,
});
