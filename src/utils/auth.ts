const AccessToken = 'accessToken';
const RefreshTokenKey = 'refreshToken';
const TokenPrefix = 'bearer ';
const isLogin = () => {
  return !!localStorage.getItem(AccessToken);
};
const getToken = () => {
  return localStorage.getItem(AccessToken);
};
const setToken = (token: string) => {
  localStorage.setItem(AccessToken, token);
};
const clearToken = () => {
  localStorage.removeItem(AccessToken);
};

const getRefreshToken = () => {
  return localStorage.getItem(RefreshTokenKey);
};

const setRefreshToken = (token: string) => {
  localStorage.setItem(RefreshTokenKey, token);
};
const clearRefreshToken = () => {
  localStorage.removeItem(RefreshTokenKey);
};
export { TokenPrefix, isLogin, getToken, setToken, clearToken, getRefreshToken, setRefreshToken, clearRefreshToken };
