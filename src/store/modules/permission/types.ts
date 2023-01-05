export type RoleType = '' | '*' | 'admin' | 'user';
export type Menu = {
  id: string;
  menuIcon: string;
  menuUrl?: string;
  open?: boolean;
  orderNum: number;
  parentId: string;
  parentName: string;
  permissionCodes?: string;
  permissionLevel: number;
  permissionName: string;
  permissionType: number;
  subList?: Array<Menu>;
};

export interface PermissionState {
  menuList?: Array<Menu>;
  permissions?: Array<string>;
}
