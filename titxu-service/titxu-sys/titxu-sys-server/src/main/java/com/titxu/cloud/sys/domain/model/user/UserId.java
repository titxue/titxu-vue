package com.titxu.cloud.sys.domain.model.user;

import com.titxu.cloud.common.core.domain.ValueObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户ID
 *


 **/
@Data
public class UserId implements ValueObject<UserId> {

    /**
     * 超级管理员角色
     */
    public static final String SYS_ADMIN = "1";

    private String id;

    public UserId(final String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("用户id不能为空");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isSysAdmin() {
        return SYS_ADMIN.equals(id);
    }

    @Override
    public boolean sameValueAs(UserId other) {
        return other != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
