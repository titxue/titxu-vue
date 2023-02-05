const AccessToken = 'accessToken';
const RefreshTokenKey = 'refreshToken';
const TokenPrefix = 'bearer ';
const isLogin = () => {
  return !!localStorage.getItem(AccessToken);
};
const getToken = () => {
  const token = localStorage.getItem(AccessToken);
  if (token) {
    return encodeURIComponent(token);
  }
};
const setToken = (token) => {
  if (token) {
    localStorage.setItem(AccessToken, token);
  }
};
const clearToken = () => {
  localStorage.removeItem(AccessToken);
};
const getRefreshToken = () => {
  return localStorage.getItem(RefreshTokenKey);
};
const setRefreshToken = (token) => {
  if (token) {
    localStorage.setItem(RefreshTokenKey, token);
  }
};
const clearRefreshToken = () => {
  localStorage.removeItem(RefreshTokenKey);
};
const removeToken = () => {
  localStorage.clear();
};
export { TokenPrefix, isLogin, getToken, setToken, clearToken, getRefreshToken, setRefreshToken, clearRefreshToken, removeToken };
//# sourceMappingURL=auth.js.map
