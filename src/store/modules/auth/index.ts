import { ElMessage } from 'element-plus';
import { defineStore } from 'pinia';
import { AuthStoreType, ToeknType } from './types';
import { login, logout } from '/@/api/auth';
import { LoginData, LoginUserType } from '/@/api/auth/types';
import { clearRefreshToken, clearToken, setRefreshToken, setToken } from '/@/utils/auth';
import { refreshToken } from '/@/utils/token';

export const useAuthStore = defineStore('auth', {
  state: (): AuthStoreType => ({
    loginUser: {} as LoginUserType,
    accessToken: '',
    refreshToken: '',
  }),
  getters: {
    // 获取token
    getToken(state: AuthStoreType): ToeknType {
      return { access_token: state.accessToken, refresh_token: state.refreshToken };
    },
    // 获取登陆信息列表
    getLoginUser(state: AuthStoreType): LoginUserType {
      return { ...state.loginUser };
    },
  },
  actions: {
    // 设置token
    setToken(partial: Partial<ToeknType>) {
      this.$patch({ accessToken: partial.access_token, refreshToken: partial.refresh_token });
    },
    // 设置登陆的信息
    setLoginUser(partial: Partial<LoginUserType>) {
      this.$patch({ loginUser: partial });
    },
    // 异步登录并存储token
    async login(loginForm: LoginData) {
      const result = await login(loginForm);
      const { data, code } = result;
      if (code !== 0) {
        return Promise.reject(result.msg);
      }
      console.log('result', data);
      const accessToken = data?.access_token;
      const refreshToken = data?.refresh_token;
      if (accessToken && refreshToken) {
        this.setToken({ access_token: accessToken, refresh_token: refreshToken });
        setToken(accessToken);
        setRefreshToken(refreshToken);
      }
      if (data?.user_info) {
        this.setLoginUser(data.user_info);
      }
      return result;
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
    // Logout
    async userLogout() {
      await logout();
      this.resetInfo();
      clearToken();
      clearRefreshToken();
      // 路由表重置
      location.reload();
    },
    // 重置权限信息
    resetInfo() {
      this.$reset();
    },
  },
});
