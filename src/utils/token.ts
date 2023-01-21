import { refresh } from '../api/auth';
import { getRefreshToken, setRefreshToken, setToken } from './auth';

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
    console.log('result', result);
    const { access_token, refresh_token } = result;
    setToken(access_token);
    setRefreshToken(refresh_token);
    if (access_token === '' || refresh_token === '') {
      Promise.resolve<number>(1);
    }
    return Promise.resolve<number>(0);
  }
};
export { refreshToken };
