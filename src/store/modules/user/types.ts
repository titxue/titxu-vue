import { PagingArgumentsType } from '/@/api/types';
import { PageUserInfoType, UserInfoType } from '/@/api/user/types';

export interface UserStoreType {
  userInfo: UserInfoType;

  userInfoList: PageUserInfoType;
  pagingArguments: PagingArgumentsType;
}
