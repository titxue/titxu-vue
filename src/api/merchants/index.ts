// 权限问题后期增加
import { get, post } from '/@/utils/http/axios';
import { ReqMerchantsCreateParams, ReqMerchantsUpdateParams, MerchantType } from './types';
import { PageResultType, PagingArgumentsType } from '../types';
import { ApiPromise } from '/@/utils/http/axios/type';
// import axios from 'axios';
enum URL {
  list = '/wifi/merchants/list',
  update = '/wifi/merchants/update',
  delete = '/wifi/merchants/delete',
  disable = '/wifi/merchants/disable/',
  info = '/wifi/merchants/info/',
  save = '/wifi/merchants/save',
  all = '/wifi/merchants/all',
}
// 保存用户信息
const saveMerchantsInfo = async (data: ReqMerchantsCreateParams): ApiPromise => post({ url: URL.save, data });

// 修改用户信息
const updateMerchants = async (data: ReqMerchantsUpdateParams): ApiPromise => post({ url: URL.update, data });

// 删除用户
const deleteMerchants = async (id: string[]): ApiPromise => post({ url: URL.delete, data: id });

// 修改用户状态
const disableMerchants = async (id: string): ApiPromise => post({ url: URL.disable + id });

// 根据id获取用户信息
const getMerchantsInfoById = async (id: string): ApiPromise<MerchantType> => get({ url: URL.info + id });

// 获取用户信息
const getMerchantsInfo = async (): ApiPromise<MerchantType> => get({ url: URL.info });

// 获取商家列表
const getMerchantsList = async (params?: PagingArgumentsType): ApiPromise<PageResultType<MerchantType>> => get({ url: URL.list, params });

// 获取用户列表
const getMerchantsAll = async (): ApiPromise<MerchantType[]> => get({ url: URL.all });

export {
  getMerchantsAll,
  getMerchantsInfoById,
  getMerchantsInfo,
  getMerchantsList,
  deleteMerchants,
  updateMerchants,
  saveMerchantsInfo,
  disableMerchants,
};
