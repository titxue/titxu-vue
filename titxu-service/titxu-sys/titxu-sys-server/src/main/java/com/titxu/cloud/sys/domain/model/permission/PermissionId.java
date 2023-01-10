package com.titxu.cloud.sys.domain.model.permission;

import com.titxu.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 权限ID
 **/
public class PermissionId implements ValueObject<PermissionId> {

    private String id;

    public PermissionId(final String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("权限id不能为空");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean sameValueAs(PermissionId other) {
        return other != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
