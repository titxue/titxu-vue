// 权限问题后期增加
import { get, post } from '/@/utils/http/axios';
import { ReqWifiInfoCreateParams, ReqWifiInfoUpdateParams, WifiInfoType } from './types';
import { PageResultType, PagingArgumentsType } from '../types';
import { ApiPromise } from '/@/utils/http/axios/type';
// import axios from 'axios';
enum URL {
  list = '/wifi/wifiifo/list',
  update = '/wifi/wifiifo/update',
  delete = '/wifi/wifiifo/delete',
  disable = '/wifi/wifiifo/disable/',
  info = '/wifi/wifiifo/info/',
  save = '/wifi/wifiifo/save',
}
// 保存WIFI信息
const saveWifiInfo = async (data: ReqWifiInfoCreateParams): ApiPromise => post({ url: URL.save, data });

// 修改WIFI信息
const updateWifiInfo = async (data: ReqWifiInfoUpdateParams): ApiPromise => post({ url: URL.update, data });

// 删除WIFI
const deleteWifiInfo = async (id: string[]): ApiPromise => post({ url: URL.delete, data: id });

// 修改WIFI状态
const disableWifiInfo = async (id: string): ApiPromise => post({ url: URL.disable + id });

// 根据id获取WIFI信息
const getWifiInfoById = async (id: string): ApiPromise<WifiInfoType> => get({ url: URL.info + id });

// 获取WIFI信息
const getWifiInfo = async (): ApiPromise<WifiInfoType> => get({ url: URL.info });

// 获取WIFI列表
const getWifiInfoList = async (params?: PagingArgumentsType): ApiPromise<PageResultType<WifiInfoType>> => get({ url: URL.list, params });

export { getWifiInfoById, getWifiInfo, getWifiInfoList, deleteWifiInfo, updateWifiInfo, saveWifiInfo, disableWifiInfo };
