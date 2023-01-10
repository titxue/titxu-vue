package com.titxu.cloud.sys.domain.service;

import com.titxu.cloud.sys.domain.model.permission.Permission;
import com.titxu.cloud.sys.domain.model.permission.PermissionId;
import com.titxu.cloud.sys.domain.model.permission.PermissionRepository;

/**
 * 权限禁用服务
 **/
public class PermissionDisableService {

    private PermissionRepository permissionRepository;

    public PermissionDisableService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void disable(PermissionId permissionId) {
        Permission permission = permissionRepository.find(permissionId);
        permission.disable();
        permissionRepository.store(permission);
        if (permission.hasSub()) {
            for (Permission subPermission : permission.getSubList()) {
                permissionRepository.store(subPermission);
            }
        }
    }
}
