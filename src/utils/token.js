import { refresh } from '../api/auth';
import { getRefreshToken, setRefreshToken, setToken } from './auth';
const refreshToken = async () => {
  const refreshTokenParam = getRefreshToken();
  if (refreshTokenParam) {
    const { data, code } = await refresh(refreshTokenParam);
    if (code !== 0) return;
    if (data === undefined) return;
    const { access_token, refresh_token } = data;
    setToken(access_token);
    setRefreshToken(refresh_token);
    if (access_token === '' || refresh_token === '') {
      Promise.resolve(1);
    }
    return Promise.resolve(0);
  }
};
export { refreshToken };
//# sourceMappingURL=token.js.map
