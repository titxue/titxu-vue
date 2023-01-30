export interface LogType {
  id: string;
  userNick: string;
  mobile: string;
  operation: string;
  method: string;
  params: string;
  time: string;
  ip: string;
  tenantId: string;
  createdBy: string;
  updatedBy: string;
  createTime: string;
  updatedTime: string;
  delFlag: string;
}
export interface LogPageType {
  totalCount: number;
  pageSize: number;
  totalPage: number;
  currPage: number;
  list: LogType[];
}
