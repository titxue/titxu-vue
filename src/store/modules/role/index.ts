import { defineStore } from 'pinia';
import { PageType } from '../../types';
import { RoleStoreType } from './types';
import { deleteRoleByIds, disableRole, getRoleAll, getRoleInfoById, getRoleList, saveRole, updateRole } from '/@/api/role';
import { RoleResultType, RoleSaveOrUpdateType, RoleType } from '/@/api/role/types';
import { PageResultType, PagingArgumentsType } from '/@/api/types';

export const useRoleStore = defineStore('role', {
  state: (): RoleStoreType => ({
    roleList: [],
    rolePageList: [],
    roleInfo: {} as RoleType,
    rolePage: {} as PageType,
    pagingArguments: {
      page: 1,
      limit: 10,
    },
  }),
  getters: {
    // 获取角色信息列表
    getRoleList(state: RoleStoreType): RoleType[] {
      return { ...state.roleList };
    },
    // 获取角色信息
    getRoleInfo(state: RoleStoreType): RoleType {
      return { ...state.roleInfo };
    },
  },
  actions: {
    // 设置角色的信息
    setInfo(partial: Partial<RoleType>) {
      // 清除缓存
      this.$patch({ roleInfo: {} });
      this.$patch({ roleInfo: partial });
    },
    // 获取角色信息列表
    async setRoleAll() {
      // 如果有缓存直接返回
      if (this.roleList.length !== 0) return;
      const { code, data } = await getRoleAll();
      if (code !== 0) return [];
      if (!data) return [];
      this.$patch({ roleList: data });
    },
    // 设置分页参数
    setPagingArguments(partial: Partial<PagingArgumentsType>) {
      this.$patch({ pagingArguments: partial });
    },
    // 获取角色信息分页列表
    async setRoleList(params?: PagingArgumentsType) {
      if (params) this.setPagingArguments(params);
      else params = this.pagingArguments;
      // 如果有缓存直接返回
      // if (this.roleList.length !== 0) return;
      const { code, data } = await getRoleList(params);
      if (code !== 0) return [];
      const { totalCount, pageSize, totalPage, currPage, list } = data as PageResultType<RoleResultType>;
      this.$patch({ rolePageList: list, rolePage: { totalCount, pageSize, totalPage, currPage } });
    },
    // 获取角色信息
    async setRoleInfoById(userId: string) {
      if (this.roleInfo.id === userId) return;
      const { code, data } = await getRoleInfoById(userId);
      if (code !== 0) return;
      this.$patch({ roleInfo: data });
    },
    // 切换角色状态
    async switchRoleStatus(id: string): Promise<boolean> {
      const { code } = await disableRole(id);
      if (code !== 0) return false;
      // 重新获取角色信息
      this.setRoleList();
      return true;
    },
    // 删除角色
    async deleteRoleByIds(id: string[]): Promise<boolean> {
      const { code } = await deleteRoleByIds(id);
      if (code !== 0) return false;
      // 重新获取角色列表
      this.setRoleList();
      return true;
    },
    // 新增角色
    async addRole(roleInfo: RoleSaveOrUpdateType): Promise<boolean> {
      const { code } = await saveRole(roleInfo);
      if (code !== 0) return false;
      // 重新获取角色信息
      this.setRoleList();
      return true;
    },
    // 修改角色
    async updateRole(roleInfo: RoleSaveOrUpdateType): Promise<boolean> {
      const { code } = await updateRole(roleInfo);
      if (code !== 0) return false;
      // 重新获取角色信息
      this.setRoleList();
      return true;
    },

    // 重置权限信息
    resetInfo() {
      this.$reset();
    },
  },
  persist: true,
});
