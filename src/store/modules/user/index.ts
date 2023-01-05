import { defineStore } from 'pinia';
import { login as userLogin, logout as userLogout, getUserProfile, LoginData, getUserList } from '/@/api/user/index';
import { setToken, clearToken, setRefreshToken, clearRefreshToken } from '/@/utils/auth';
import { UserState } from './types';
import { ReqListParams, ResultListType } from '/@/api/user/types';

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    user_name: undefined,
    avatar: undefined,
    organization: undefined,
    location: undefined,
    email: undefined,
    blogJuejin: undefined,
    blogZhihu: undefined,
    blogGithub: undefined,
    profileBio: undefined,
    devLanguages: undefined,
    role: '',
  }),
  getters: {
    userProfile(state: UserState): UserState {
      return { ...state };
    },
  },
  actions: {
    switchRoles() {
      return new Promise((resolve) => {
        this.role = this.role === 'user' ? 'user' : 'admin';
        resolve(this.role);
      });
    },
    // 设置用户的信息
    setInfo(partial: Partial<UserState>) {
      this.$patch(partial);
    },
    // 重置用户信息
    resetInfo() {
      this.$reset();
    },
    // 获取用户列表
    async list(params?: ReqListParams): Promise<ResultListType> {
      const result = await getUserList(params);
      console.log('result', result);
      return result;
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
