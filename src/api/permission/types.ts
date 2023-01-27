export interface NavList {
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
};
