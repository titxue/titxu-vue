import { defineStore } from 'pinia';
import { listLogin } from '/@/api/log';
export const useLogStore = defineStore('log', {
  state: () => ({
    logList: [],
    page: {
      currPage: 1,
    },
  }),
  getters: {},
  actions: {
    async setLogList(page) {
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
//# sourceMappingURL=index.js.map
