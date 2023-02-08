import { LogPageType } from './types';
import { post } from '/@/utils/http/axios';
import { ApiPromise } from '/@/utils/http/axios/type';
enum URL {
  listLogin = '/sys/log/listLogin',
  list = '/sys/log/list',
}

// 获取角色信息
const listLogin = async (currPage: string): ApiPromise<LogPageType> =>
  post({
    url: URL.listLogin,
    data: {
      page: currPage,
      limit: 10,
    },
  });

export { listLogin };
