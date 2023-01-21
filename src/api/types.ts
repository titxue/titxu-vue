export interface ResResultData<T> {
  code: number;
  data?: T;
  msg: string;
}
export interface PagingArgumentsType {
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
