package com.titxu.cloud.sys.application;

import com.titxu.cloud.sys.application.dto.PermissionDTO;

import java.util.List;
import java.util.Set;

/**
 * 权限查询服务接口
 *
 * @author haoxin
 * @date 2021-05-10
 **/
public interface PermissionQueryService {

    /**
     * 所有权限
     *
     * @return
     */
    List<PermissionDTO> listAllPermission();

    /**
     * 所有菜单（不保存按钮）
     *
     * @return
     */
    List<PermissionDTO> listAllMenu();

    /**
     * 通过ID获取
     *
     * @param id
     * @return
     */
    PermissionDTO getById(String id);

    /**
     * 获取权限树
     *
     * @param userId
     * @return
     */
    List<PermissionDTO> getUserMenuTree(String userId);

    /**
     * 获取权限编码
     *
     * @param userId
     * @return
     */
    Set<String> getPermissionCodes(String userId);

    /**
     * 获取权限id
     *
     * @param userId
     * @return
     */
    Set<String> getPermissionIds(String userId);

}
