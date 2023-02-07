package com.titxu.cloud.sys.application.impl;

import com.titxu.cloud.common.core.exception.BaseException;
import com.titxu.cloud.sys.domain.model.tenant.Tenant;
import com.titxu.cloud.sys.domain.model.tenant.TenantId;
import com.titxu.cloud.sys.domain.model.tenant.TenantRepository;
import com.titxu.cloud.sys.application.TenantApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户应用服务实现类
 *


 **/
@Service
public class TenantApplicationServiceImpl implements TenantApplicationService {

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String id) {
        TenantId tenantId = new TenantId(id);
        if (tenantId.isPlatformId()) {
            throw new BaseException("系统租户无法修改状态");
        }
        Tenant tenant = tenantRepository.find(tenantId);
        tenant.disable();
        tenantRepository.store(tenant);
    }

}
