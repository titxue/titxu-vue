import { LoginUserType } from '/@/api/auth/types';

export interface AuthStoreType {
  loginUser: LoginUserType;
  accessToken: string;
  refreshToken: string;
}

export interface ToeknType {
  access_token: string;
  refresh_token: string;
}
