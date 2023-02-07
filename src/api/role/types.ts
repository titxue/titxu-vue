export type RoleType = {
  id: string;
  roleName: string;
  roleCode: string;
  status: string;
  permissionIdList: string[];
  remarks: string;
  createTime: string;
  createdBy: string;
};
export type RoleResultType = {
  id: string;
  delFlag: string;
  createdBy: string;
  createdTime: string;
  updatedBy: string;
  updatedTime: string;
  roleCode: string;
  roleName: string;
  tenantId: string;
  status: string;
  remarks: string;
};

export type RoleSaveOrUpdateType = {
  id: string;
  roleName: string;
  roleCode: string;
  remarks: string;
  status: string;
  permissionIdList: string[];
};
