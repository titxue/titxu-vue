package com.titxu.cloud.sys.domain.model.permission;

import com.titxu.cloud.sys.domain.model.role.RoleCode;

import java.util.List;
import java.util.Map;

/**
 * 权限-Repository接口
 **/
public interface PermissionRepository {

    /**
     * 查找菜单
     * @param params 参数
     * @return List<Permission>
     */
    List<Permission> queryList(Map<String, Object> params);

    /**
     * 角色编码获取权限
     * @param rolecode 角色编码
     * @return List<Permission>
     */
    List<Permission> queryList(RoleCode rolecode);

    /**
     * 获取权限
     * @param permissionId 权限id
     * @return Permission
     */
    Permission find(PermissionId permissionId);

    /**
     * 获取权限
     * @param permissionName 权限名称
     * @return Permission
     */
    Permission find(PermissionName permissionName);

    /**
     * 保存
     * @param permission 权限
     */
    PermissionId store(Permission permission);

    /**
     * 删除
     * @param permissionId 权限id
     */
    void remove(PermissionId permissionId);
    /**
     * 删除
     * @param permissionIds 权限id
     */
    void remove(List<PermissionId> permissionIds);
}
