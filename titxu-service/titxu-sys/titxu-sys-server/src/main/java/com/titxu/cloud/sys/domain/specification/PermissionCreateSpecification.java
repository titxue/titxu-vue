package com.titxu.cloud.sys.domain.specification;

import com.titxu.cloud.common.core.domain.AbstractSpecification;
import com.titxu.cloud.sys.domain.model.permission.Permission;
import com.titxu.cloud.sys.domain.model.permission.PermissionRepository;
import com.titxu.cloud.sys.domain.model.permission.PermissionTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 权限创建Specification
 **/
public class PermissionCreateSpecification extends AbstractSpecification<Permission> {

    private final PermissionRepository permissionRepository;

    public PermissionCreateSpecification(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean isSatisfiedBy(Permission permission) {
        if (permission.getPermissionName() != null) {
            Permission existPermission = permissionRepository.find(permission.getPermissionName());
            if (existPermission != null && !existPermission.getPermissionId().sameValueAs(permission.getPermissionId())) {
                throw new IllegalArgumentException("权限名已存在");
            }
        }
        if (permission.getParent() == null) {
            throw new IllegalArgumentException("上级菜单不能为空");
        }
        // 菜单icon 不能为数字（StringUtils.isNumeric("-1") 返回 false）
        if (StringUtils.isNumeric(permission.getMenuIcon()) || Objects.equals(permission.getMenuIcon(), "-1")) {
            throw new IllegalArgumentException("菜单图标不能为数字");
        }

        if (permission.getPermissionType() == null) {
            throw new IllegalArgumentException("菜单类型不能为空");
        }

        //菜单
        if (permission.getPermissionType() == PermissionTypeEnum.MENU) {
            if (permission.getMenuUrl() == null || StringUtils.isBlank(permission.getMenuUrl().getUrl())) {
                throw new IllegalArgumentException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        PermissionTypeEnum parentType = permission.getParent().getPermissionType();

        //目录、菜单
        if (permission.getPermissionType() == PermissionTypeEnum.CATALOG ||
                permission.getPermissionType() == PermissionTypeEnum.MENU) {
            if (parentType != PermissionTypeEnum.CATALOG) {
                throw new IllegalArgumentException("上级菜单只能为目录类型");
            }
        }

        //按钮
        if (permission.getPermissionType() == PermissionTypeEnum.BUTTON) {
            if (parentType != PermissionTypeEnum.MENU) {
                throw new IllegalArgumentException("上级菜单只能为菜单类型");
            }
        }
        return true;
    }


}
