package com.titxu.cloud.sys.application;

/**
 * 租户应用服务接口
 **/
public interface TenantApplicationService {

    /**
     * 禁用
     *
     * @param id
     */
    void disable(String id);
}
