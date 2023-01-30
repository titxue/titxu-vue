import { post } from '/@/utils/http/axios';
import { ApiPromise } from '/@/utils/http/axios/type';
enum URL {
  listLogin = '/sys/log/listLogin',
}

// 获取角色信息
const listLogin = async (): ApiPromise<any> => post({ url: URL.listLogin, data: {} });

export { listLogin };
