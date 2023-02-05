import { defineStore } from 'pinia';
import { deleteRoleByIds, disableRole, getRoleAll, getRoleInfoById, getRoleList, saveRole, updateRole } from '/@/api/role';
export const useRoleStore = defineStore('role', {
  state: () => ({
    roleList: [],
    rolePageList: [],
    roleInfo: {},
    rolePage: {},
    pagingArguments: {
      page: 1,
      limit: 10,
    },
  }),
  getters: {
    getRoleList(state) {
      return { ...state.roleList };
    },
    getRoleInfo(state) {
      return { ...state.roleInfo };
    },
  },
  actions: {
    async setRoleAll() {
      if (this.roleList.length !== 0) return;
      const { code, data } = await getRoleAll();
      if (code !== 0) return [];
      if (!data) return [];
      this.$patch({ roleList: data });
    },
    setPagingArguments(partial) {
      this.$patch({ pagingArguments: partial });
    },
    async setRoleList(params) {
      if (params) this.setPagingArguments(params);
      else params = this.pagingArguments;
      const { code, data } = await getRoleList(params);
      if (code !== 0) return [];
      const { totalCount, pageSize, totalPage, currPage, list } = data;
      this.$patch({ rolePageList: list, rolePage: { totalCount, pageSize, totalPage, currPage } });
    },
    async setRoleInfoById(userId) {
      if (this.roleInfo.id === userId) return;
      const { code, data } = await getRoleInfoById(userId);
      if (code !== 0) return;
      this.$patch({ roleInfo: data });
    },
    async switchRoleStatus(id) {
      const { code } = await disableRole(id);
      if (code !== 0) return false;
      this.setRoleList();
      return true;
    },
    async deleteRoleByIds(id) {
      const { code } = await deleteRoleByIds(id);
      if (code !== 0) return false;
      this.setRoleList();
      return true;
    },
    async addRole(roleInfo) {
      const { code } = await saveRole(roleInfo);
      if (code !== 0) return false;
      this.setRoleList();
      return true;
    },
    async updateRole(roleInfo) {
      const { code } = await updateRole(roleInfo);
      if (code !== 0) return false;
      this.setRoleList();
      return true;
    },
    setInfo(partial) {
      this.$patch(partial);
    },
    resetInfo() {
      this.$reset();
    },
  },
  persist: true,
});
//# sourceMappingURL=index.js.map
