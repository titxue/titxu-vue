import { defineStore } from 'pinia';
import piniaStore from '/@/store/index';
import { TabType } from '/@/store/modules/navTab/types';
import { TabPaneName } from 'element-plus';

export const useNavTabStore = defineStore('navTab', {
  state: () => ({
    tabList: [] as TabType[],
    activeTab: {} as TabPaneName,
    activeNav: '' as string,
  }),
  getters: {
    // 获取当前激活的nav
    getActiveNav(state) {
      console.log('getActiveNav', state.activeTab);
      const activeTabStr: string = state.activeTab as string;
      // const activeTabs: string[] = activeTabStr.split('/');
      // return activeTabs[activeTabs.length - 1];
      return activeTabStr.replace('/admin/', '');
    },
  },
  actions: {},
  persist: true,
});
export function useNavTabOutsideStore() {
  return useNavTabStore(piniaStore);
}
