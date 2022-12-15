package com.titxu.cloud.sys.application.impl;

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

 * @date 2021-02-14
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
            throw new RuntimeException("平台租户无法删除");
        }
        Tenant tenant = tenantRepository.find(tenantId);
        tenant.disable();
        tenantRepository.store(tenant);
    }

}
