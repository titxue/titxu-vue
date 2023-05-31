// export interface ReqParams {
//   username: string;
//   password: string;
// }

// export interface ReqListParams {
//   userName?: string;
//   userType?: string;
//   email?: string;
//   mobile?: string;
//   page?: number;
//   limit?: number;
//   sidx?: string;
//   order?: string;
//   asc?: string;
// }

// export interface ReqAuth {
//   auths: string[];
//   modules: string[];
//   is_admin?: 0 | 1;
// }

export interface ReqWifiInfoUpdateParams {
  id?: string;
  merchantId: string;
  wifiName: string;
  wifiPassword?: string;
  wifiBssid?: string;
  wifiFrequency?: string;
  remarks?: string;
  status?: string;
}

export interface ReqWifiInfoCreateParams {
  id?: string;
  merchantId: string;
  wifiName: string;
  wifiPassword?: string;
  wifiBssid?: string;
  wifiFrequency?: string;
  remarks?: string;
  status?: string;
}

export interface ResultOkType {
  msg: string;
  code: number;
}

export interface WifiInfoType {
  id: string;
  merchantId: string;
  wifiName: string;
  wifiPassword?: string;
  wifiBssid?: string;
  wifiFrequency: string;
  remarks?: string;
  createdBy: string;
  createdTime: string;
  updatedBy: string;
  updatedTime: string;
}
