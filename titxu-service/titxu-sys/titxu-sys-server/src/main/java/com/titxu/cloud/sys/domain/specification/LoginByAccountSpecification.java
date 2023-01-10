package com.titxu.cloud.sys.domain.specification;

import com.titxu.cloud.common.core.domain.AbstractSpecification;
import com.titxu.cloud.sys.domain.model.user.User;

/**
 * 账号登录Specification
 **/
public class LoginByAccountSpecification extends AbstractSpecification<User> {

    private String password;

    public LoginByAccountSpecification(String password) {
        this.password = password;
    }


    @Override
    public boolean isSatisfiedBy(User user) {
        if (user.getAccount().checkPassword(password)) {
            throw new IllegalArgumentException("用户或密码不正确");
        }
        return true;
    }
}
