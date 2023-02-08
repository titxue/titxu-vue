package com.titxu.cloud.sys.application;

import com.titxu.cloud.sys.application.command.PermissionCommand;

import java.util.List;

/**
 * 权限应用服务接口
 **/
public interface PermissionApplicationService {

    /**
     * 保存或更新
     * @param permissionCommand 权限命令
     */
    void saveOrUpdate(PermissionCommand permissionCommand);

    /**
     * 删除
     * @param id 权限id
     */
    void delete(String id);

    /**
     * 禁用
     * @param id 权限id
     */
    void disable(String id);

    void deleteBatch(List<String> ids);
}
