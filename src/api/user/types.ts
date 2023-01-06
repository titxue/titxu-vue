export interface ReqParams {
  username: string;
  password: string;
}

export interface ReqListParams {
  userName?: string;
  userType?: string;
  email?: string;
  mobile?: string;
  page?: number;
  limit?: number;
  sidx?: string;
  order?: string;
  asc?: string;
}

export interface ReqAuth {
  auths: string[];
  modules: string[];
  is_admin?: 0 | 1;
}

export interface ResResult {
  data?: ResResultData;
  status: string | '';
  headers: object;
}
export interface ResResultData {
  code?: number;
  result?: any;
  message: string;
  status: string;
}

export interface UserInfoType {
  id: string;
  delFlag: string;
  createdBy: string;
  createdTime: string;
  updatedBy: string;
  updatedTime: string;
  userName: string;
  accountId: string;
  userType?: string;
  linkId?: string;
  status: string;
  remarks?: string;
  tenantId: string;
  tenantCode?: string;
  tenantName?: string;
  mobile: string;
  email?: string;
}

export interface ReqUserUpdateParams {
  id: string;
  userName: string;
  mobile: string;
  email?: string;
  roleIdList?: string[];
}

export interface PageUserInfoType {
  totalCount: number;
  pageSize: number;
  totalPage: number;
  currPage: number;
  list?: UserInfoType[];
}

export interface ResultListType {
  msg: string;
  code: number;
  page: PageUserInfoType;
}

export interface ResultOkType {
  msg: string;
  code: number;
}
