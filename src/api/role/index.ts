import { get } from '/@/utils/http/axios';
import { ResResultData } from '/@/api/types';
import { RoleType } from './types';
enum URL {
  info = '/sys/role/info/',
  all = '/sys/role/select',
}

// 获取角色信息
const getRoleInfoById = async (id: string) => get<ResResultData<RoleType>>({ url: URL.info + id });
// 获取全部角色列表
const getRoleAll = async () => get<ResResultData<RoleType[]>>({ url: URL.all });
export { getRoleInfoById, getRoleAll };
