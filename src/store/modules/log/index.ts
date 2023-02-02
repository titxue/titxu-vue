import { defineStore } from 'pinia';
import { PageType } from '../../types';
import { LogStoreType } from './types';
import { listLogin } from '/@/api/log';

export const useLogStore = defineStore('log', {
  state: (): LogStoreType => ({
    logList: [],
    page: {
      currPage: 1,
    } as PageType,
  }),
  getters: {},
  actions: {
    // 获取日志列表
    async setLogList(page?: string) {
      // if (this.logList.length !== 0) return;
      if (page === undefined) page = this.page.currPage.toString();
      const { code, data } = await listLogin(page);
      if (code !== 0) return;
      if (data === undefined) return;
      const { totalCount, pageSize, totalPage, currPage, list } = data;
      this.resetInfo();
      this.$patch({ logList: list, page: { totalCount, pageSize, totalPage, currPage } });
    },

    resetInfo() {
      this.$reset();
    },
  },
});
