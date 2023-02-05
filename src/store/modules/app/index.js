import { defineStore } from 'pinia';
import piniaStore from '/@/store/index';
export const useAppStore = defineStore('app', {
  state: () => ({
    title: '管理',
    h1: '主页',
    theme: '',
    cdnUrl: '',
    upload: {
      mode: 'local',
      maxsize: 0,
      mimetype: '',
      savename: '',
    },
  }),
  getters: {},
  actions: {
    updateSettings(partial) {
      this.$patch(partial);
    },
    toggleTheme(dark) {
      if (dark) {
        this.theme = 'dark';
        document.documentElement.classList.add('dark');
      } else {
        this.theme = 'light';
        document.documentElement.classList.remove('dark');
      }
    },
  },
  persist: {
    key: 'theme',
    storage: localStorage,
    paths: ['theme'],
  },
});
export function useAppOutsideStore() {
  return useAppStore(piniaStore);
}
//# sourceMappingURL=index.js.map
