package com.titxu.cloud.sys.domain.specification;

import com.titxu.cloud.sys.domain.model.tenant.Tenant;
import com.titxu.cloud.sys.domain.model.tenant.TenantRepository;
import com.titxu.cloud.sys.domain.model.user.User;
import com.titxu.cloud.common.core.domain.AbstractSpecification;
import com.titxu.cloud.sys.domain.model.user.UserId;

/**
 * 用户修改Specification
 *
 **/
public class UserUpdateSpecification extends AbstractSpecification<User> {

    private final TenantRepository tenantRepository;

    public UserUpdateSpecification(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public boolean isSatisfiedBy(User user) {
        Tenant tenant = tenantRepository.find(user.getTenantId());
        if (tenant.getCreatorId().sameValueAs(user.getUserId())) {
            throw new RuntimeException("租户创建者无法修改");
        }
        return false;
    }
    public boolean isDeleteVerifiedBy(UserId userId){
        if (userId.isSysAdmin()) {
            throw new RuntimeException("管理员无法删除");
        }
        return false;
    }
}
