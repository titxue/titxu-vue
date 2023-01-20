package com.titxu.cloud.sys.domain.model.user;

import com.titxu.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户名
 **/
public class UserNick implements ValueObject<UserNick> {

    /**
     * 用户名
     */
    private String name;

    public UserNick(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean sameValueAs(UserNick other) {
        return other != null && this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
