import { defineStore } from 'pinia';
import {
  login as userLogin,
  logout as userLogout,
  getUserProfile,
  LoginData,
  getUserList,
  updateUser,
  deleteUser,
  disableUser,
} from '/@/api/user/index';
import { setToken, clearToken, setRefreshToken, clearRefreshToken } from '/@/utils/auth';
import { PageUserInfoType, ReqListParams, UserInfoType } from '/@/api/user/types';
import { refreshToken } from '/@/utils/token';
import { ElMessage } from 'element-plus';

export const useUserStore = defineStore('user', {
  state: (): UserInfoType => ({
    id: '',
    delFlag: '',
    createdBy: '',
    createdTime: '',
    updatedBy: '',
    updatedTime: '',
    userName: '',
    accountId: '',
    userType: '',
    linkId: '',
    status: '',
    remarks: '',
    tenantId: '',
    tenantCode: '',
    tenantName: '',
    mobile: '',
    email: '',
  }),
  getters: {
    userProfile(state: UserInfoType): UserInfoType {
      return { ...state };
    },
  },
  actions: {
    // switchRoles() {
    //   return new Promise((resolve) => {
    //     this.role = this.role === 'user' ? 'user' : 'admin';
    //     resolve(this.role);
    //   });
    // },
    // 设置用户的信息
    setInfo(partial: Partial<UserInfoType>) {
      this.$patch(partial);
    },
    // 重置用户信息
    resetInfo() {
      this.$reset();
    },
    // 刷新token
    async refreshAccessToken() {
      const code = await refreshToken();
      if (code !== 0) {
        ElMessage.error('刷新token失败');
        return;
      }
      ElMessage.success('刷新token成功');
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
        userName: data.userName,
        mobile: data.mobile,
        email: data.email,
      });
      if (code !== 0) {
        return Promise.reject('修改用户信息失败');
      }
    },
    // 获取用户列表
    async list(params?: ReqListParams): Promise<PageUserInfoType> {
      const { code, page } = await getUserList(params);
      if (code !== 0) {
        return Promise.reject('获取用户列表失败');
      }
      return page;
    },
    // 获取用户信息
    async info() {
      const result = await getUserProfile();
      this.setInfo(result);
    },
    // 异步登录并存储token
    async login(loginForm: LoginData) {
      const { data: result } = await userLogin(loginForm);
      console.log('result', result);
      const token = result?.value;
      const refreshToken = result?.refreshToken;
      if (token) {
        setToken(token);
        setRefreshToken(refreshToken);
      }
      return result;
    },
    // Logout
    async logout() {
      await userLogout();
      this.resetInfo();
      clearToken();
      clearRefreshToken();
      // 路由表重置
      location.reload();
    },
  },
});
