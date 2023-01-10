package com.titxu.cloud.sys.application;

import com.titxu.cloud.sys.application.command.RegisterTenantCommand;

/**
 * 注册应用服务接口
 **/
public interface RegisterApplicationService {

    /**
     * 注册租户
     *
     * @param registerTenantCommand
     */
    void registerTenant(RegisterTenantCommand registerTenantCommand);
}
