import { get, post } from '../../utils/http/axios';
var URL;
(function (URL) {
  URL['nav'] = '/sys/permission/nav';
  URL['list'] = '/sys/permission/list';
  URL['tree'] = '/sys/permission/tree';
  URL['selectMenu'] = '/sys/permission/selectMenu';
  URL['info'] = '/sys/permission/info/';
  URL['save'] = '/sys/permission/save';
  URL['update'] = '/sys/permission/update';
  URL['delete'] = '/sys/permission/delete/';
  URL['disable'] = '/sys/permission/disable/';
})(URL || (URL = {}));
const getNav = async () => get({ url: URL.nav }, { loading: true });
const getPermissionList = async () => get({ url: URL.list }, { loading: true });
const getPermissionTree = async () => get({ url: URL.tree }, { loading: true });
const getMenu = async () => get({ url: URL.selectMenu }, { loading: true });
const getPermissionInfo = async (id) => get({ url: URL.info + id }, { loading: true });
const savePermission = async (data) => post({ url: URL.save, data: data }, { loading: true });
const updatePermission = async (data) => post({ url: URL.update, data: data }, { loading: true });
const deletePermission = async (id) => post({ url: URL.delete + id }, { loading: true });
const disablePermission = async (id) => post({ url: URL.disable + id }, { loading: true });
export {
  getNav,
  getPermissionList,
  getMenu,
  getPermissionInfo,
  getPermissionTree,
  savePermission,
  updatePermission,
  deletePermission,
  disablePermission,
};
//# sourceMappingURL=index.js.map
