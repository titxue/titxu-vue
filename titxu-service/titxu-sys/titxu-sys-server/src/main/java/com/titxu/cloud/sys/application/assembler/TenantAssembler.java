package com.titxu.cloud.sys.application.assembler;

import com.titxu.cloud.sys.application.dto.TenantDTO;
import com.titxu.cloud.sys.domain.model.tenant.Tenant;

public class TenantAssembler {
    public static TenantDTO fromTenant(final Tenant tenant) {
        final TenantDTO dto = new TenantDTO();
        dto.setId(tenant.getTenantId() == null ? null : tenant.getTenantId().getId());
        dto.setTenantCode(tenant.getTenantCode() == null ? null : tenant.getTenantCode().getCode());
        dto.setTenantName(tenant.getTenantName() == null ? null : tenant.getTenantName().getName());
        dto.setStatus(tenant.getStatus() == null ? null : tenant.getStatus().getValue());
        return dto;
    }
}
