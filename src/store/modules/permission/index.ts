import { defineStore } from 'pinia';
import { getNav } from '/@/api/permission';
import { PermissionStateType } from './types';
import { Menu } from '/@/api/permission/types';

export const usePermissionStore = defineStore('permission', {
  state: (): PermissionStateType => ({
    menuList: [] as Menu[],
    permissions: [] as string[],
  }),
  getters: {
    getPermissions(state: PermissionStateType): string[] {
      if (state.permissions?.length === 0 || state.permissions === undefined) return [];
      return state.permissions;
    },
    getMenuList(state: PermissionStateType): Menu[] {
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
    // 获取权限信息
    async setPermissions() {
      const { code, menuList, permissions } = await getNav();
      if (code !== 0) return;
      if (!menuList) return;
      if (!permissions) return;
      this.$patch({ menuList: menuList, permissions: permissions });
    },
  },
});
