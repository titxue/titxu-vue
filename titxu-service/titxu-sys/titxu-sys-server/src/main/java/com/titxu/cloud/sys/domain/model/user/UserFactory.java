package com.titxu.cloud.sys.domain.model.user;

import com.titxu.cloud.common.core.domain.StatusEnum;
import com.titxu.cloud.common.core.exception.BaseException;
import com.titxu.cloud.sys.domain.model.role.RoleId;
import com.titxu.cloud.sys.domain.model.tenant.TenantId;

import java.util.List;

/**
 * 用户工厂
 **/
public class UserFactory {

    private final UserRepository userRepository;

    public UserFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(Mobile mobile, Email email, Password password, UserNick userNick, List<RoleId> roleIdList, TenantId currentTenantId, StatusEnum statusEnum) {
        List<User> users = userRepository.find(mobile);
        Account account;
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                if (user.getTenantId().sameValueAs(currentTenantId)) {
                    throw new BaseException("租户内账号已存在");
                }
            }
            account = users.get(0).getAccount();
        } else {
            account = new Account(mobile, email, password);
        }
        if (roleIdList == null || roleIdList.isEmpty()) {
            throw new BaseException("角色未分配");
        }
        StatusEnum status = statusEnum == null ? StatusEnum.ENABLE : statusEnum;
        return new User(userNick, account, roleIdList,status);
    }



}
