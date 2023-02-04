import { MenuType, PermissionType } from '/@/api/permission/types';

// export type PermissionEnum = '' | '*' | 'admin' | 'user';

export interface PermissionStateType {
  navList: MenuType[];
  menuList: MenuStoreType[];
  permissions: string[];
  parentMenuList: MenuType[];
  permissionTree: PermissionType[];
  permissionInfo: any;
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
