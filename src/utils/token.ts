import { getRefreshToken, setRefreshToken, setToken } from './auth';
import { refresh } from '/@/api/user';

/**
 * 单独处理token 刷新 避免循环引用
 * warning: "import.meta" is not available in the configured target environment ("es2015") and will be empty
 */

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
export { refreshToken };
