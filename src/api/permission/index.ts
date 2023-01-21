// 权限问题后期增加
import { NavResultData } from './types';
import { get } from '/@/utils/http/axios';
// import axios from 'axios';
enum URL {
  nav = '/sys/permission/nav',
}

// 获取导航菜单
const getNav = async () => get<NavResultData>({ url: URL.nav });
export { getNav };
