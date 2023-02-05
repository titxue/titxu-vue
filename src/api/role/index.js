import { get, post } from '/@/utils/http/axios';
var URL;
(function (URL) {
  URL['info'] = '/sys/role/info/';
  URL['all'] = '/sys/role/select';
  URL['list'] = '/sys/role/list';
  URL['save'] = '/sys/role/save';
  URL['update'] = '/sys/role/update';
  URL['delete'] = '/sys/role/delete';
  URL['disable'] = '/sys/role/disable/';
})(URL || (URL = {}));
const getRoleInfoById = async (id) => get({ url: URL.info + id }, { loading: true });
const getRoleAll = async () => get({ url: URL.all }, { loading: true });
const getRoleList = async (params) => get({ url: URL.list, params }, { loading: true });
const saveRole = async (data) => post({ url: URL.save, data: data }, { loading: true });
const updateRole = async (data) => post({ url: URL.update, data: data }, { loading: true });
const deleteRoleByIds = async (ids) => post({ url: URL.delete, data: ids }, { loading: true });
const disableRole = async (id) => post({ url: URL.disable + id }, { loading: true });
export { getRoleInfoById, getRoleAll, getRoleList, saveRole, updateRole, deleteRoleByIds, disableRole };
//# sourceMappingURL=index.js.map
