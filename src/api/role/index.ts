import { get } from '/@/utils/http/axios';
import { RoleType } from './types';
import { ApiPromise } from '/@/utils/http/axios/type';
enum URL {
  info = '/sys/role/info/',
  all = '/sys/role/select',
}

// 获取角色信息
const getRoleInfoById = async (id: string): ApiPromise<RoleType> => get({ url: URL.info + id });
// 获取全部角色列表
const getRoleAll = async (): ApiPromise<RoleType[]> => get({ url: URL.all });
export { getRoleInfoById, getRoleAll };
