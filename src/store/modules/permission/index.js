import { defineStore } from 'pinia';
import {
  deletePermission,
  disablePermission,
  getMenu,
  getNav,
  getPermissionInfo,
  getPermissionList,
  getPermissionTree,
  savePermission,
  updatePermission,
} from '/@/api/permission';
import { tranListToTreeData } from '/@/utils';
export const usePermissionStore = defineStore('permission', {
  state: () => ({
    navList: [],
    parentMenuList: [],
    menuList: [],
    permissions: [],
    permissionTree: [],
    permissionInfo: {},
  }),
  getters: {
    getPermissions(state) {
      if (state.permissions?.length === 0 || state.permissions === undefined) return [];
      return state.permissions;
    },
    getMenuList(state) {
      if (state.menuList?.length === 0 || state.menuList === undefined) return [];
      return state.menuList;
    },
  },
  actions: {
    async refreshTable() {
      this.$reset();
      await this.setMenuList();
      await this.setParentMenuList();
      await this.setNavList();
    },
    async editMenu(data) {
      const { code } = await updatePermission(data);
      if (code !== 0) return false;
      return true;
    },
    async addMenu(data) {
      const { code } = await savePermission(data);
      if (code !== 0) return false;
      return true;
    },
    async setParentMenuList() {
      if (this.parentMenuList.length !== 0) return;
      const { code, data } = await getPermissionList();
      if (code !== 0) return;
      if (data === undefined) return;
      const list = data.filter((item) => item.permissionType === '0');
      this.$patch({ parentMenuList: list });
    },
    async setPermissionTree() {
      if (this.permissionTree.length !== 0) return;
      const { code, data } = await getPermissionTree();
      if (code !== 0) return;
      this.$patch({ permissionTree: data });
    },
    async setNavList() {
      if (this.navList.length !== 0) return;
      const { code, data } = await getNav();
      if (code !== 0) return;
      if (data === undefined) return;
      const { menuList, permissions } = data;
      if (!menuList) return;
      if (!permissions) return;
      this.$patch({ navList: menuList, permissions: permissions });
    },
    async removePermission(id) {
      const { code } = await deletePermission(id);
      if (code !== 0) return false;
      return true;
    },
    async setMenuList() {
      if (this.menuList.length !== 0) return;
      const { code, data } = await getMenu();
      if (code !== 0) return;
      if (data === undefined) return;
      data.forEach((item) => {
        delete item.subList;
      });
      const menuList = tranListToTreeData(data, '0');
      this.$patch({ menuList: menuList });
    },
    async setPermissionInfoById(id) {
      const { code, data } = await getPermissionInfo(id);
      if (code !== 0) return;
      this.$patch({ permissionInfo: data });
    },
    async disableMenu(id) {
      const { code } = await disablePermission(id);
      if (code !== 0) return false;
      this.refreshTable();
      return true;
    },
    setInfo(partial) {
      this.$patch(partial);
    },
    resetInfo() {
      this.$reset();
    },
  },
  persist: true,
});
//# sourceMappingURL=index.js.map
