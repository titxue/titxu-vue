import { Menu } from '/@/api/permission/types';

export type RoleType = '' | '*' | 'admin' | 'user';

export interface PermissionStateType {
  menuList: Menu[];
  permissions: string[];
}
