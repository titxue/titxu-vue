package com.titxu.cloud.sys.application.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.titxu.cloud.common.mybatis.util.Page;
import com.titxu.cloud.common.mybatis.util.PageAssembler;
import com.titxu.cloud.common.mybatis.util.Query;
import com.titxu.cloud.sys.application.TenantQueryService;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysTenantDO;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysTenantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 租户查询服务实现类
 **/
@Service
public class TenantQueryServiceImpl implements TenantQueryService {


    private final SysTenantMapper sysTenantMapper;

    @Autowired
    public TenantQueryServiceImpl(SysTenantMapper sysTenantMapper) {
        this.sysTenantMapper = sysTenantMapper;
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        IPage<SysTenantDO> page = sysTenantMapper.queryPage(new Query<SysTenantDO>().getPage(params), params);
        return PageAssembler.toPage(page);
    }
}
