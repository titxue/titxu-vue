package com.titxu.cloud.sys.application;

import com.titxu.cloud.sys.application.dto.PermissionDTO;

import java.util.List;
import java.util.Set;

/**
 * 权限查询服务接口
 **/
public interface PermissionQueryService {

    /**
     * 所有权限
     * @return 所有权限信息列表
     */
    List<PermissionDTO> listAllPermission();

    /**
     * 权限树
     * @return 权限树
     */
    List<PermissionDTO> treeAllPermission();

    /**
     * 所有菜单（不包括按钮）
     * @return 所有菜单信息列表
     */
    List<PermissionDTO> listAllMenu();

    /**
     * 所有菜单（不包括按钮）
     * @return 所有菜单信息树
     */
    List<PermissionDTO> treeAllMenu();

    /**
     * 通过ID获取
     * @param id 权限ID
     * @return 权限信息
     */
    PermissionDTO getById(String id);

    /**
     * 获取权限树
     * @param userId 用户ID
     * @return 权限树
     */
    List<PermissionDTO> getUserMenuTree(String userId);

    /**
     * 获取权限编码
     * @param userId 用户ID
     * @return 权限编码列表
     */
    Set<String> getPermissionCodes(String userId);

    /**
     * 获取权限id
     * @param userId 用户ID
     * @return 权限id列表
     */
    Set<String> getPermissionIds(String userId);

}
