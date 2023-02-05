import { del, post } from '../../utils/http/axios';
var URL;
(function (URL) {
  URL['login'] = '/auth/account/login';
  URL['refresh'] = '/auth/account/refresh';
  URL['logout'] = '/auth/account/logout';
})(URL || (URL = {}));
const login = async (data) => post({ url: URL.login, data });
const refresh = async (params) => post({ url: URL.refresh, params: { refreshToken: params } });
const logout = async () => del({ url: URL.logout });
export { login, refresh, logout };
//# sourceMappingURL=index.js.map
