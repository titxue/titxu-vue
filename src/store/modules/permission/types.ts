import { MenuType } from '/@/api/permission/types';

export type RoleType = '' | '*' | 'admin' | 'user';

export interface PermissionStateType {
  navList: MenuType[];
  menuList: MenuType[];
  permissions: string[];
}
