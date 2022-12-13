package com.titxu.cloud.sys.application;

import com.titxu.cloud.sys.application.command.PermissionCommand;

/**
 * 权限应用服务接口
 *

 * @date 2021-02-17
 **/
public interface PermissionApplicationService {

    /**
     * 保存或更新
     *
     * @param permissionCommand
     */
    void saveOrUpdate(PermissionCommand permissionCommand);

    /**
     * 删除
     *
     * @param id
     */
    void delete(String id);

    /**
     * 禁用
     *
     * @param id
     */
    void disable(String id);
}
