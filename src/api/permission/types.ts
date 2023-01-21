export interface NavResultData {
  code: number;
  permissions: string[];
  menuList: Menu[];
  msg: string;
}

export type Menu = {
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
  subList: Array<Menu>;
};
