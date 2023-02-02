import { PageResultType, PagingArgumentsType } from '/@/api/types';
import { UserInfoType } from '/@/api/user/types';

export interface UserStoreType {
  userInfo: UserInfoType;
  userInfoById: UserInfoType;
  userInfoList: PageResultType<UserInfoType>;
  pagingArguments: PagingArgumentsType;
}
