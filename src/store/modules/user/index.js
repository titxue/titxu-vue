import { defineStore } from 'pinia';
import { getUserList, updateUser, deleteUser, disableUser, getUserInfoById, getUserInfo, saveUserInfo } from '/@/api/user/index';
export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: {},
    userInfoList: {},
    userInfoById: {},
    pagingArguments: {
      page: 1,
      limit: 10,
    },
  }),
  getters: {
    getUserInfo(state) {
      return { ...state.userInfo };
    },
    getUserInfoList(state) {
      return state.userInfoList;
    },
    getUserInfoById(state) {
      return state.userInfoById;
    },
    getPagingArguments(state) {
      return state.pagingArguments;
    },
  },
  actions: {
    setUserInfoList(partial) {
      this.$patch({ userInfoList: partial });
    },
    async setUserInfo() {
      this.$patch({ userInfo: {} });
      const { code, data } = await getUserInfo();
      if (code === 0) {
        this.$patch({ userInfo: data });
      }
    },
    async refreshTable() {
      const result = await this.list(this.pagingArguments);
      if (result) {
        this.$patch({ userInfoList: result });
      }
    },
    async setUserInfoById(id) {
      const { code, data } = await getUserInfoById(id);
      if (code === 0) {
        this.$patch({ userInfoById: data });
      }
    },
    resetInfo() {
      this.$reset();
    },
    setPagingArguments(partial) {
      this.$patch({ pagingArguments: partial });
    },
    async changeStatus(id) {
      const { code } = await disableUser(id);
      if (code !== 0) {
        return Promise.reject('修改用户状态失败');
      }
    },
    async deleteUser(id) {
      const { code } = await deleteUser(id);
      return code;
    },
    async updateUser(data) {
      const { code } = await updateUser({
        id: data.id,
        userNick: data.userNick,
        mobile: data.mobile,
        email: data.email,
        roleIdList: data.roleIdList,
        remarks: data.remarks,
      });
      if (code !== 0) {
        return Promise.reject('修改用户信息失败');
      }
    },
    async createUser(data) {
      const { code } = await saveUserInfo({
        userNick: data.userNick,
        mobile: data.mobile,
        email: data.email,
        roleIdList: data.roleIdList,
      });
      if (code !== 0) {
        return Promise.reject('创建用户失败');
      }
    },
    async editUserInfo(row, currentStatus) {
      const { id, status: newStatus } = row;
      if (currentStatus === newStatus) return;
      if (newStatus === '0') {
        await this.changeStatus(id);
      }
      if (newStatus === '1') {
        await this.changeStatus(id);
      }
    },
    async list(params) {
      const { code, data } = await getUserList(params);
      if (code !== 0 || !data) {
        return Promise.reject('获取用户列表失败');
      }
      this.$patch({ userInfoList: data });
      return data;
    },
  },
  persist: true,
});
//# sourceMappingURL=index.js.map
