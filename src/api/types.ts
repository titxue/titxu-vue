export interface ResResultData<T> {
  code: number;
  data?: T;
  msg: string;
}

export interface PageResultType<T> {
  totalCount: number;
  pageSize: number;
  totalPage: number;
  currPage: number;
  list?: T[];
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
