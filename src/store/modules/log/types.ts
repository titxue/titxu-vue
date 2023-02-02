import { PageType } from '../../types';
import { LogType } from '/@/api/log/types';

export interface LogStoreType {
  logList: LogType[];
  page: PageType;
}
