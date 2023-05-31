export interface ReqParams {
  Merchantsname: string;
  password: string;
}

export interface ReqListParams {
  MerchantsName?: string;
  MerchantsType?: string;
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

export interface ReqMerchantsUpdateParams {
  id: string;
  MerchantsNick: string;
  mobile: string;
  email?: string;
  roleIdList?: string[];
  remarks?: string;
}

export interface ReqMerchantsCreateParams {
  id?: string;
  MerchantsNick: string;
  mobile: string;
  email?: string;
  roleIdList?: string[];
}

// export interface PageMerchantsInfoType {
//   totalCount: number;
//   pageSize: number;
//   totalPage: number;
//   currPage: number;
//   list?: MerchantsInfoType[];
// }

export interface ResultOkType {
  msg: string;
  code: number;
}

export interface MerchantType {
  id: string;
  createdBy: string;
  createdTime: string;
  updatedBy: string;
  updatedTime: string;
  name: string;
  contactName: string;
  contactPhone?: string;
  address?: string;
  status: string;
  remarks?: string;
  email?: string;
}
