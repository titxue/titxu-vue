package com.titxu.cloud.sys.domain.specification;

import com.titxu.cloud.common.core.domain.AbstractSpecification;
import com.titxu.cloud.sys.domain.model.tenant.Tenant;
import com.titxu.cloud.sys.domain.model.tenant.TenantRepository;

/**
 * 租户创建Specification
 **/
public class TenantCreateSpecification extends AbstractSpecification<Tenant> {

    private TenantRepository tenantRepository;

    public TenantCreateSpecification(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public boolean isSatisfiedBy(Tenant tenant) {
        if (tenantRepository.find(tenant.getTenantName()) != null) {
            throw new IllegalArgumentException("租户名称已存在");
        }
        if (tenantRepository.find(tenant.getTenantCode()) != null) {
            throw new IllegalArgumentException("租户编码已存在");
        }
        return true;
    }
}
