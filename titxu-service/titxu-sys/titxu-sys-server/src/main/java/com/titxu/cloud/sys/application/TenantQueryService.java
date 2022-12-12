package com.titxu.cloud.sys.application;

import com.xtoon.cloud.common.mybatis.util.Page;

import java.util.Map;

/**
 * 租户查询服务接口
 *
 * @author haoxin
 * @date 2021-05-10
 **/
public interface TenantQueryService {

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    Page queryPage(Map<String, Object> params);


}
