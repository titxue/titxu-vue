import { get, post } from '/@/utils/http/axios';
import { ApiPromise } from '/@/utils/http/axios/type';
import { PageResultType } from '../types';
enum URL {
  list = '/sys/tenant/list',
  disable = '/sys/tenant/disable/',
}

// 所有角色分页列表
const getTenantList = async (params?: anyObj): ApiPromise<PageResultType<anyObj>> => get({ url: URL.list, params });

// 禁用角色
const disableTenant = async (id: string): ApiPromise => post({ url: URL.disable + id });

export { getTenantList, disableTenant };
