const AccessToken = 'accessToken';
const RefreshTokenKey = 'refreshToken';
const TokenPrefix = 'bearer ';
const isLogin = () => {
  return !!localStorage.getItem(AccessToken);
};
const getToken = () => {
  //防止请求头header中放入了中文，
  // 出现类型错误 request:fail TypeError: Failed to execute 'setRequestHeader' on 'XMLHttpRequest': String contains non ISO-8859-1 code point.
  // 进行编码
  const token = localStorage.getItem(AccessToken);
  if (token) {
    return encodeURIComponent(token);
  }
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
