import { defineStore } from 'pinia';
import piniaStore from '/@/store/index';
import { AppState } from './types';

export const useAppStore = defineStore(
  // 唯一ID
  'app',
  {
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
      updateSettings(partial: Partial<AppState>) {
        this.$patch(partial);
      },

      // Change theme color
      toggleTheme(dark: boolean) {
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
  },
);

export function useAppOutsideStore() {
  return useAppStore(piniaStore);
}
