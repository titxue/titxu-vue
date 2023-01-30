import { LogType } from '/@/api/log/types';

export interface LogStoreType {
  logList: LogType[];
  page: PageType;
}
export interface PageType {
  totalCount: number;
  pageSize: number;
  totalPage: number;
  currPage: number;
}
