export interface MenuListType {
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
  permissionLevel: number;
  permissionName: string;
  permissionType: number;
  subList: Array<MenuType>;
};
