import { defineStore } from 'pinia';
import { getNav } from '/@/api/permission';
import { PermissionState } from './types';

export const usePermissionStore = defineStore('permission', {
  state: (): PermissionState => ({
    menuList: undefined,
    permissions: undefined,
  }),
  getters: {
    permissionsProfile(state: PermissionState): PermissionState {
      return { ...state };
    }
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
    async info() {
      const result = await getNav();
      console.log(result);
      this.setInfo(result);
    },
  },
});
