import { MenuType } from '/@/api/permission/types';

// export type PermissionEnum = '' | '*' | 'admin' | 'user';

export interface PermissionStateType {
  navList: MenuType[];
  menuList: MenuStoreType[];
  permissions: string[];
}

export type MenuStoreType = {
  id: string;
  menuIcon: string;
  menuUrl: string;
  open: boolean;
  orderNum: number;
  parentId: string;
  parentName: string;
  permissionCodes: string;
  permissionLevel: number;
  permissionName: string;
  permissionType: number;
  children: Array<MenuStoreType>;
};
