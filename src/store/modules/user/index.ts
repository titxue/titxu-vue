import { defineStore } from 'pinia';
import { getUserList, updateUser, deleteUser, disableUser, getUserInfoById, getUserInfo, saveUserInfo } from '/@/api/user/index';
import { PageUserInfoType, UserInfoType } from '/@/api/user/types';
import { UserStoreType } from './types';
import { PagingArgumentsType } from '/@/api/types';

export const useUserStore = defineStore('user', {
  state: (): UserStoreType => ({
    userInfo: {} as UserInfoType,
    userInfoList: {} as PageUserInfoType,
    userInfoById: {} as UserInfoType,
    pagingArguments: {
      page: 1,
      limit: 10,
    },
  }),
  getters: {
    // 获取用户信息
    getUserInfo(state: UserStoreType): UserInfoType {
      return { ...state.userInfo };
    },

    // 获取用户信息列表
    getUserInfoList(state: UserStoreType): PageUserInfoType {
      return state.userInfoList;
    },
    // 获取用户信息
    getUserInfoById(state: UserStoreType): UserInfoType {
      return state.userInfoById;
    },
    // 获取分页参数
    getPagingArguments(state: UserStoreType): PagingArgumentsType {
      return state.pagingArguments;
    },
  },
  actions: {
    // 设置用户信息列表
    setUserInfoList(partial: Partial<PageUserInfoType>) {
      this.$patch({ userInfoList: partial });
    },

    // 设置用户的信息
    async setUserInfo() {
      const { code, data } = await getUserInfo();
      if (code === 0) {
        this.$patch({ userInfo: data });
      }
    },
    // switchRoles() {
    //   return new Promise((resolve) => {
    //     this.role = this.role === 'user' ? 'user' : 'admin';
    //     resolve(this.role);
    //   });
    // },
    // 刷新表格数据
    async refreshTable() {
      const result = await this.list(this.pagingArguments);
      if (result) {
        this.$patch({ userInfoList: result });
      }
    },

    // 根据id获取用户信息
    async setUserInfoById(id: string) {
      const { code, data } = await getUserInfoById(id);
      if (code === 0) {
        this.$patch({ userInfoById: data });
      }
    },

    // 重置用户信息
    resetInfo() {
      this.$reset();
    },
    // 设置分页参数
    setPagingArguments(partial: Partial<PagingArgumentsType>) {
      this.$patch({ pagingArguments: partial });
    },
    // 修改用户状态
    async changeStatus(id: string) {
      const { code } = await disableUser(id);
      if (code !== 0) {
        return Promise.reject('修改用户状态失败');
      }
    },
    // 删除用户
    async deleteUser(id: string[]): Promise<number> {
      const { code } = await deleteUser(id);
      return code;
    },
    // 修改用户信息
    async updateUser(data: UserInfoType) {
      const { code } = await updateUser({
        id: data.id,
        userNick: data.userNick,
        mobile: data.mobile,
        email: data.email,
        roleIdList: data.roleIdList,
      });
      if (code !== 0) {
        return Promise.reject('修改用户信息失败');
      }
    },
    // 创建用户
    async createUser(data: UserInfoType) {
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

    // 修改用户信息
    async editUserInfo(row: UserInfoType, currentStatus: string) {
      const { id, status: newStatus } = row;
      if (currentStatus === newStatus) return;
      if (newStatus === '0') {
        await this.changeStatus(id);
      }
      if (newStatus === '1') {
        await this.changeStatus(id);
      }
    },
    // 获取用户列表
    async list(params?: PagingArgumentsType): Promise<PageUserInfoType> {
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
