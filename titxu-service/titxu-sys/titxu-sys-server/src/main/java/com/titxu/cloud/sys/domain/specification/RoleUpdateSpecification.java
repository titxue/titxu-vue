package com.titxu.cloud.sys.domain.specification;

import com.titxu.cloud.common.core.domain.AbstractSpecification;
import com.titxu.cloud.sys.domain.model.role.Role;

/**
 * 角色修改Specification
 **/
public class RoleUpdateSpecification extends AbstractSpecification<Role> {

    @Override
    public boolean isSatisfiedBy(Role role) {
        if (role.getRoleCode().isTenantAdmin()) {
            throw new RuntimeException("租户管理角色无法修改");
        }
        return true;
    }
}
