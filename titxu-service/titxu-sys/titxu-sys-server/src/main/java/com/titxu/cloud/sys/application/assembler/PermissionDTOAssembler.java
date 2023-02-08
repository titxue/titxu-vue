package com.titxu.cloud.sys.application.assembler;

import com.titxu.cloud.common.core.domain.StatusEnum;
import com.titxu.cloud.sys.application.command.PermissionCommand;
import com.titxu.cloud.sys.application.dto.PermissionDTO;
import com.titxu.cloud.sys.domain.model.permission.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Assembler class for the Permission.
 **/
public class PermissionDTOAssembler {

    public static PermissionDTO fromPermission(final Permission permission) {
        final PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getPermissionId() == null ? null : permission.getPermissionId().getId());
        dto.setPermissionName(permission.getPermissionName() == null ? null : permission.getPermissionName().getName());
        dto.setPermissionType(permission.getPermissionType() == null ? null : permission.getPermissionType().getValue());
        dto.setMenuIcon(permission.getMenuIcon());
        dto.setMenuUrl(permission.getMenuUrl() == null ? null : permission.getMenuUrl().getUrl());
        dto.setOrderNum(permission.getOrderNum());
        dto.setParentId(permission.getParent() == null ? null : permission.getParent().getPermissionId().getId());
        dto.setParentName(permission.getParent() == null ? null : permission.getParent().getPermissionName().getName());
        dto.setPermissionCodes(permission.getPermissionCodes() == null ? null : permission.getPermissionCodes().getCodesString());
        dto.setPermissionLevel(permission.getPermissionLevel() == null ? null : permission.getPermissionLevel().getValue());
        dto.setStatus(permission.getStatus() == null ? null : permission.getStatus().getValue());
        dto.setOpen(permission.getParent() != null && permission.hasSub());
        return dto;
    }

    public static Permission toPermission(final PermissionCommand permissionCommand, Permission parent) {
        PermissionId permissionId = null;
        if (permissionCommand.getId() != null) {
            permissionId = new PermissionId(permissionCommand.getId());
        }
        PermissionName permissionName = null;
        if (permissionCommand.getPermissionName() != null) {
            permissionName = new PermissionName(permissionCommand.getPermissionName());
        }
        PermissionTypeEnum permissionType = null;
        if (permissionCommand.getPermissionType() != null) {
            permissionType = PermissionTypeEnum.getMenuTypeEnum(permissionCommand.getPermissionType());
        }
        PermissionLevelEnum permissionLevel = null;
        if (permissionCommand.getPermissionLevel() != null) {
            permissionLevel = PermissionLevelEnum.getMenuLevelEnum(permissionCommand.getPermissionLevel());
        }
        PermissionCodes permissionCodes = null;
        if (permissionCommand.getPermissionCodes() != null) {
            Set<String> permsSet = new HashSet<>(Arrays.asList(permissionCommand.getPermissionCodes().trim().split(",")));
            permissionCodes = new PermissionCodes(permsSet);
        }
        MenuUrl menuUrl = null;
        if (!StringUtils.isEmpty(permissionCommand.getMenuUrl())) {
            menuUrl = new MenuUrl(permissionCommand.getMenuUrl());
        }
        StatusEnum status = null;
        if (permissionCommand.getStatus() != null) {
            status = StatusEnum.getStatusEnum(permissionCommand.getStatus());
        }
        return new Permission(permissionId, permissionName, permissionType, permissionLevel, permissionCommand.getMenuIcon(), permissionCodes, permissionCommand.getOrderNum(),
                menuUrl, parent, status, null);
    }


    public static List<PermissionDTO> getPermissionList(@NonNull final List<Permission> permissionList) {
        final List<PermissionDTO> List = new ArrayList<>();
        for (Permission permission : permissionList) {
            List.add(fromPermission(permission));
        }
        return List;
    }

    /**
     * 转换为权限树
     *
     * @param permissionList 权限列表
     * @return 权限树
     */
    public static List<PermissionDTO> getPermissionTree(@NonNull final List<Permission> permissionList) {
        // 权限树
        return permissionList.stream()
                // 过滤出根节点
                .filter(Permission::isRoot)
                // 为根节点递归添加子节点
                .map(permission -> covert(permission, permissionList))
                .toList();
    }
    /**
     * 转换为权限树
     *
     * @param permissionList 权限列表
     * @return 权限树
     */
    public static List<PermissionDTO> getMenuTree(@NonNull final List<Permission> permissionList) {
        // 权限树
        return permissionList.stream()
                // 过滤出根节点
                .filter(Permission::isRoot)
                // 为根节点递归添加子节点
                .map(permission -> covert(permission, permissionList, true))
                .toList();
    }
    /**
     * 将权限转换为带有子级的权限对象
     * 当找不到子级权限的时候map操作不会再递归调用covert
     * @param permission 权限
     * @param permissionList 权限列表
     * @param isMenu 是否是菜单
     * @return 带有子级的权限对象
     */
    private static PermissionDTO covert(Permission permission, List<Permission> permissionList, Boolean isMenu) {
        /*
          递归冗余设计变量
         */
        isMenu = isMenu != null && isMenu;
        Boolean finalIsMenu = isMenu;
        // 转换为PermissionDTO
        PermissionDTO permissionDTO = fromPermission(permission);
        List<PermissionDTO> children = permissionList.stream()
                // 父级不为空并且子级父id等于父级id
                .filter(subPermission -> subPermission.getParent() != null && subPermission.getParent().sameIdentityAs(permission) && subPermission.isMenu())
                // 递归子级
                .map(subPermission -> covert(subPermission, permissionList, finalIsMenu)).collect(Collectors.toList());
        permissionDTO.setSubList(children);
        // 有子节点设置为true
        permissionDTO.setOpen(permissionDTO.getSubList() != null && !permissionDTO.getSubList().isEmpty());
        return permissionDTO;
    }

    /**
     * 将权限转换为带有子级的权限对象
     * 当找不到子级权限的时候map操作不会再递归调用covert
     * @param permission 权限
     * @param permissionList 权限列表
     * @return 带有子级的权限对象
     */
    private static PermissionDTO covert(Permission permission, List<Permission> permissionList) {
        // 转换为PermissionDTO
        PermissionDTO permissionDTO = fromPermission(permission);
        List<PermissionDTO> children = permissionList.stream()
                // 父级不为空并且子级父id等于父级id
                .filter(subPermission -> subPermission.getParent() != null && subPermission.getParent().sameIdentityAs(permission) && subPermission.isMenu())
                // 递归子级
                .map(subPermission -> covert(subPermission, permissionList)).collect(Collectors.toList());
        permissionDTO.setSubList(children);
        // 有子节点设置为true
        permissionDTO.setOpen(permissionDTO.getSubList() != null && !permissionDTO.getSubList().isEmpty());
        return permissionDTO;
    }


    public static List<PermissionDTO> getMenuList(@NonNull final List<Permission> permissionList) {
        final List<PermissionDTO> List = new ArrayList<>();
        for (Permission permission : permissionList) {
            if (permission.isMenu()) {
                List.add(fromPermission(permission));
            }
        }
        return List;
    }
}
