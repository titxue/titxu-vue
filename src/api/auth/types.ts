export interface LoginData {
  mobile: string;
  password: string;
}

export interface LoginDataType {
  access_token: string;
  refresh_token: string;
  license: string;
  user_info: LoginUserType;
  scope: string;
  token_type: string;
  expires_in: number;
}

export interface RefreshType {
  access_token: string;
  refresh_token: string;
  scope: string;
  token_type: string;
  expires_in: number;
}

export interface LoginUserType {
  username: string;
  authorities: Array<AuthoritiesType>;
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  enabled: boolean;
  id: string;
  tenantId: string;
  name: string;
  userNick: string;
  attributes: AttributesType;
}

export interface AuthoritiesType {
  authority: string;
}
interface AttributesType {
  id: string;
}
