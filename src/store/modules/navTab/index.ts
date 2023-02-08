import { defineStore } from 'pinia';
import piniaStore from '/@/store/index';
import { TabType } from '/@/store/modules/navTab/types';
import { TabPaneName } from 'element-plus';

export const useNavTabStore = defineStore('navTab', {
  state: () => ({
    tabList: [] as TabType[],
    activeTab: '' as TabPaneName,
    activeNav: '' as string,
  }),
  getters: {
    // 获取当前激活的nav
    getActiveNav(state) {
      const activeTabStr: string = state.activeTab as string;
      if (Object.keys(activeTabStr).length === 0) return 'dashboard';
      return activeTabStr.replace('/admin/', '');
    },
  },
  actions: {},
  persist: true,
});
export function useNavTabOutsideStore() {
  return useNavTabStore(piniaStore);
}
