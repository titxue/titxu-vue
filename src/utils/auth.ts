import { refresh } from '../api/user';

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
/**
 * 刷新token
 */
const refreshToken = async () => {
  const refreshTokenParam = getRefreshToken();
  if (refreshTokenParam) {
    const { data: result } = await refresh(refreshTokenParam);
    const { value, refreshToken } = result;
    setToken(value);
    setRefreshToken(refreshToken);
  }
};
const setRefreshToken = (token: string) => {
  localStorage.setItem(RefreshTokenKey, token);
};
const clearRefreshToken = () => {
  localStorage.removeItem(RefreshTokenKey);
};
export { TokenPrefix, refreshToken, isLogin, getToken, setToken, clearToken, getRefreshToken, setRefreshToken, clearRefreshToken };
