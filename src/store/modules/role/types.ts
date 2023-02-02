import { PageType } from '../../types';
import { RoleResultType, RoleType } from '/@/api/role/types';
import { PagingArgumentsType } from '/@/api/types';

export interface RoleStoreType {
  roleList: RoleType[];
  roleInfo: RoleType;
  rolePageList: RoleResultType[];
  rolePage: PageType;
  pagingArguments: PagingArgumentsType;
}
