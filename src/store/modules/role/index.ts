import { defineStore } from 'pinia';
import { RoleStoreType } from './types';
import { getRoleAll, getRoleInfoById } from '/@/api/role';
import { RoleType } from '/@/api/role/types';

export const useRoleStore = defineStore('role', {
  state: (): RoleStoreType => ({
    roleTypeList: [],
    roleInfo: {} as RoleType,
  }),
  getters: {
    // 获取角色信息列表
    getUserInfo(state: RoleStoreType): RoleType[] {
      return { ...state.roleTypeList };
    },
    // 获取角色信息
    getRoleInfo(state: RoleStoreType): RoleType {
      return { ...state.roleInfo };
    },
  },
  actions: {
    // 获取角色信息列表
    async setRoleAll() {
      const { code, data } = await getRoleAll();
      if (code !== 0) return [];
      if (!data) return [];
      this.$patch({ roleTypeList: data });
    },
    // 获取角色信息
    async setRoleInfo(userId: string) {
      const { code, data } = await getRoleInfoById(userId);
      if (code !== 0) return {} as RoleType;
      if (!data) return {} as RoleType;
      this.$patch({ roleInfo: data });
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
});
