package com.titxu.cloud.sys.application;

import com.titxu.cloud.sys.application.command.RoleCommand;

import java.util.List;

/**
 * 角色应用服务接口
 **/
public interface RoleApplicationService {

    /**
     * 保存或更新
     *
     * @param roleCommand
     */
    void saveOrUpdate(RoleCommand roleCommand);

    /**
     * 批量删除
     *
     * @param ids
     */
    void deleteBatch(List<String> ids);

    /**
     * 禁用
     *
     * @param id
     */
    void disable(String id);
}
