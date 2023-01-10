package com.titxu.cloud.sys.domain.model.role;

import com.titxu.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 角色ID
 **/
public class RoleId implements ValueObject<RoleId> {

    private String id;

    public RoleId(final String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("角色id不能为空");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean sameValueAs(RoleId other) {
        return other != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
