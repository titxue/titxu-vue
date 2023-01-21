import { defineStore } from 'pinia';
import { getUserList, updateUser, deleteUser, disableUser } from '/@/api/user/index';
import { PageUserInfoType, UserInfoType } from '/@/api/user/types';
import { UserStoreType } from './types';
import { PagingArgumentsType } from '/@/api/types';

export const useUserStore = defineStore('user', {
  state: (): UserStoreType => ({
    userInfo: {} as UserInfoType,

    userInfoList: {} as PageUserInfoType,

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

    // 获取分页参数
    getPagingArguments(state: UserStoreType): PagingArgumentsType {
      return state.pagingArguments;
    },
  },
  actions: {
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
    // 设置用户信息列表
    setUserInfoList(partial: Partial<PageUserInfoType>) {
      this.$patch({ userInfoList: partial });
    },

    // 设置用户的信息
    setUserInfo(partial: Partial<UserInfoType>) {
      this.$patch({ userInfo: partial });
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
    async deleteUser(id: string[]) {
      const { code, msg } = await deleteUser(id);
      if (code !== 0) {
        return Promise.reject(msg);
      }
    },
    // 修改用户信息
    async updateUser(data: UserInfoType) {
      const { code } = await updateUser({
        id: data.id,
        userNick: data.userNick,
        mobile: data.mobile,
        email: data.email,
      });
      if (code !== 0) {
        return Promise.reject('修改用户信息失败');
      }
    },

    // 修改用户信息
    async editUserInfo(row: UserInfoType, currentStatus: string) {
      console.log(row);
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
      const { code, page } = await getUserList(params);
      if (code !== 0) {
        return Promise.reject('获取用户列表失败');
      }
      this.$patch({ userInfoList: page });
      return page;
    },
  },
});
