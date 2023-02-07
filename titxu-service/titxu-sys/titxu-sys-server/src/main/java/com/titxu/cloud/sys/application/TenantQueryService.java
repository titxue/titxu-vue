package com.titxu.cloud.sys.application;

import com.titxu.cloud.common.mybatis.util.Page;
import com.titxu.cloud.sys.application.dto.TenantDTO;

import java.util.Map;

/**
 * 租户查询服务接口
 **/
public interface TenantQueryService {

    /**
     * 分页查询
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);


    TenantDTO getById(String id);
}
