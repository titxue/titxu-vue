package com.titxu.cloud.sys.application.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.titxu.cloud.sys.domain.model.permission.Permission;
import com.titxu.cloud.sys.domain.model.permission.PermissionId;
import com.titxu.cloud.sys.domain.model.permission.PermissionRepository;
import com.titxu.cloud.sys.domain.model.permission.PermissionTypeEnum;
import com.titxu.cloud.sys.domain.model.role.RoleCode;
import com.titxu.cloud.sys.domain.model.tenant.TenantId;
import com.titxu.cloud.sys.domain.model.user.UserId;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysPermissionDO;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysPermissionMapper;
import com.xtoon.cloud.common.core.domain.StatusEnum;
import com.xtoon.cloud.common.core.util.TenantContext;
import com.titxu.cloud.sys.application.PermissionQueryService;
import com.titxu.cloud.sys.application.assembler.PermissionDTOAssembler;
import com.titxu.cloud.sys.application.dto.PermissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限查询服务实现类
 *
 * @author haoxin
 * @date 2021-05-10
 **/
@Service
public class PermissionQueryServiceImpl implements PermissionQueryService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<PermissionDTO> listAllPermission() {
        List<Permission> permissionList;
        if (TenantId.PLATFORM_TENANT.equals(TenantContext.getTenantId())) {
            permissionList = permissionRepository.queryList(new HashMap<>());
        } else {
            permissionList = permissionRepository.queryList(new RoleCode(RoleCode.TENANT_ADMIN));
        }
        return PermissionDTOAssembler.getPermissionList(permissionList);
    }

    @Override
    public List<PermissionDTO> listAllMenu() {
        List<Permission> permissionList;
        if (TenantId.PLATFORM_TENANT.equals(TenantContext.getTenantId())) {
            permissionList = permissionRepository.queryList(new HashMap<>());
        } else {
            permissionList = permissionRepository.queryList(new RoleCode(RoleCode.TENANT_ADMIN));
        }
        return PermissionDTOAssembler.getMenuList(permissionList);
    }

    @Override
    public PermissionDTO getById(String id) {
        Permission permission = permissionRepository.find(new PermissionId(id));
        return PermissionDTOAssembler.fromPermission(permission);
    }

    @Override
    public List<PermissionDTO> getUserMenuTree(String userId) {
        Set<String> permissionIds = getPermissionIds(userId);
        if (permissionIds == null) {
            return null;
        }
        return getAllMenuList(permissionIds);
    }

    @Override
    public Set<String> getPermissionCodes(String userId) {
        List<SysPermissionDO> sysPermissionDOList = getSysPermissionDOList(userId);
        Set<String> permissionCodes = sysPermissionDOList.stream().filter(p -> p.getPermissionCodes() != null).
                flatMap(p -> Arrays.asList(p.getPermissionCodes().trim().split(",")).stream()).collect(Collectors.toSet());
        return permissionCodes;
    }

    @Override
    public Set<String> getPermissionIds(String userId) {
        List<SysPermissionDO> sysPermissionDOList = getSysPermissionDOList(userId);
        Set<String> permissionIds = sysPermissionDOList.stream().map(p -> p.getId()).collect(Collectors.toSet());
        return permissionIds;
    }

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    private List<SysPermissionDO> getSysPermissionDOList(String userId) {
        List<SysPermissionDO> sysPermissionDOList;
        if (new UserId(userId).isSysAdmin()) {
            sysPermissionDOList = sysPermissionMapper.selectList(new QueryWrapper<SysPermissionDO>().eq("status", StatusEnum.ENABLE.getValue()));
        } else {
            sysPermissionDOList = sysPermissionMapper.queryPermissionByUserId(userId);
        }
        return sysPermissionDOList;
    }

    /**
     * 获取所有菜单列表
     */
    private List<PermissionDTO> getAllMenuList(Set<String> menuIdList) {
        //查询根菜单列表
        List<PermissionDTO> menuList = queryListParentId(Permission.ROOT_ID, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);
        return menuList;
    }

    /**
     * 通过父级id获取权限
     *
     * @param parentId
     * @param menuIdList
     * @return
     */
    private List<PermissionDTO> queryListParentId(String parentId, Set<String> menuIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        List<PermissionDTO> menuList = PermissionDTOAssembler.getPermissionList(permissionRepository.queryList(params));
        if (menuIdList == null) {
            return menuList;
        }
        List<PermissionDTO> userMenuList = new ArrayList<>();
        for (PermissionDTO menu : menuList) {
            if (menuIdList.contains(menu.getId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    /**
     * 递归
     */
    private List<PermissionDTO> getMenuTreeList(List<PermissionDTO> menuList, Set<String> menuIdList) {
        List<PermissionDTO> subMenuList = new ArrayList<>();
        for (PermissionDTO entity : menuList) {
            //目录
            if (PermissionTypeEnum.CATALOG.getValue().equals(entity.getPermissionType())) {
                entity.setSubList(getMenuTreeList(queryListParentId(entity.getId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }
        return subMenuList;
    }
}
