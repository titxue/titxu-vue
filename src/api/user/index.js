import { get, post } from '/@/utils/http/axios';
var URL;
(function (URL) {
  URL['list'] = '/sys/user/list';
  URL['update'] = '/sys/user/update';
  URL['delete'] = '/sys/user/delete';
  URL['disable'] = '/sys/user/disable/';
  URL['info'] = '/sys/user/info/';
  URL['save'] = '/sys/user/save';
})(URL || (URL = {}));
const saveUserInfo = async (data) => post({ url: URL.save, data });
const updateUser = async (data) => post({ url: URL.update, data });
const deleteUser = async (id) => post({ url: URL.delete, data: id });
const disableUser = async (id) => post({ url: URL.disable + id });
const getUserInfoById = async (id) => get({ url: URL.info + id });
const getUserInfo = async () => get({ url: URL.info });
const getUserList = async (params) => get({ url: URL.list, params });
export { getUserInfoById, getUserInfo, getUserList, deleteUser, updateUser, saveUserInfo, disableUser };
//# sourceMappingURL=index.js.map
