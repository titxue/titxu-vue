export interface NavListType {
  permissions: string[];
  menuList: MenuType[];
}

export type MenuType = {
  id: string;
  menuIcon: string;
  menuUrl: string;
  open: boolean;
  orderNum: number;
  parentId: string;
  parentName: string;
  permissionCodes: string;
  permissionLevel: string;
  permissionName: string;
  permissionType: string;
  subList?: Array<MenuType>;
  status: string;
};
export type PermissionType = {
  id: string;
  parentId: string;
  parentName: string;
  permissionName: string;
  permissionType: string;
  permissionLevel: string;
  permissionCodes: string;
  menuIcon: string;
  orderNum: number;
  menuUrl: string;
  open: boolean;
  subList: Array<PermissionType>;
};
