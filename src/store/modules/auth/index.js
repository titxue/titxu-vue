import { ElMessage } from 'element-plus';
import { defineStore } from 'pinia';
import { login, logout } from '/@/api/auth';
import { clearRefreshToken, clearToken, setRefreshToken, setToken } from '/@/utils/auth';
import { refreshToken } from '/@/utils/token';
export const useAuthStore = defineStore('auth', {
  state: () => ({
    loginUser: {},
    accessToken: '',
    refreshToken: '',
  }),
  getters: {
    getToken(state) {
      return { access_token: state.accessToken, refresh_token: state.refreshToken };
    },
    getLoginUser(state) {
      return { ...state.loginUser };
    },
  },
  actions: {
    setToken(partial) {
      this.$patch({ accessToken: partial.access_token, refreshToken: partial.refresh_token });
    },
    setLoginUser(partial) {
      this.$patch({ loginUser: partial });
    },
    async login(loginForm) {
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
    async refreshAccessToken() {
      const code = await refreshToken();
      if (code !== 0) {
        ElMessage.error('刷新token失败');
        return;
      }
      ElMessage.success('刷新token成功');
    },
    async userLogout() {
      await logout();
      this.resetInfo();
      clearToken();
      clearRefreshToken();
      location.reload();
    },
    resetInfo() {
      this.$reset();
    },
  },
});
//# sourceMappingURL=index.js.map
