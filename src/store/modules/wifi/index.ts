import { defineStore } from 'pinia';
import { WifiStoreType } from './types';
import { MerchantType } from '/@/api/merchants/types';
import { getMerchantsAll } from '/@/api/merchants';

export const useWifiStore = defineStore('wifi', {
  state: (): WifiStoreType => ({
    merchantList: [] as MerchantType[],
  }),
  getters: {},
  actions: {
    // 获取商家列表
    async setMerchantsvList() {
      if (this.merchantList.length !== 0) return;
      const { code, data } = await getMerchantsAll();
      if (code !== 0) return;
      if (data === undefined) return;
      // const { list } = data;
      this.resetInfo();
      this.$patch({ merchantList: data });
    },
    // 重置用户信息
    resetInfo() {
      this.$reset();
    },
  },
  persist: true,
});
