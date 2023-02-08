package com.titxu.cloud.sys.domain.specification;

import com.titxu.cloud.common.core.domain.AbstractSpecification;
import com.titxu.cloud.sys.domain.model.permission.Permission;
import com.titxu.cloud.sys.domain.model.permission.PermissionId;
import com.titxu.cloud.sys.domain.model.permission.PermissionRepository;

/**
 * 权限删除Specification
 **/
public class PermissionDeleteSpecification extends AbstractSpecification<PermissionId> {

    private final PermissionRepository permissionRepository;

    public PermissionDeleteSpecification(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean isSatisfiedBy(PermissionId permissionId) {
        Permission permission = permissionRepository.find(permissionId);
        if (permission.hasSub()) {
            throw new RuntimeException("请先删除子菜单或按钮");
        }
        return true;
    }
}
