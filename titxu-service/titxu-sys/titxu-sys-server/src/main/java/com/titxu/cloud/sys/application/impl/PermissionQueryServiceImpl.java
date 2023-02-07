package com.titxu.cloud.sys.application.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.titxu.cloud.common.mybatis.util.BaseDO;
import com.titxu.cloud.sys.domain.model.permission.Permission;
import com.titxu.cloud.sys.domain.model.permission.PermissionId;
import com.titxu.cloud.sys.domain.model.permission.PermissionRepository;
import com.titxu.cloud.sys.domain.model.permission.PermissionTypeEnum;
import com.titxu.cloud.sys.domain.model.role.RoleCode;
import com.titxu.cloud.sys.domain.model.tenant.TenantId;
import com.titxu.cloud.sys.domain.model.user.UserId;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysPermissionDO;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysPermissionMapper;
import com.titxu.cloud.common.core.domain.StatusEnum;
import com.titxu.cloud.common.core.util.TenantContext;
import com.titxu.cloud.sys.application.PermissionQueryService;
import com.titxu.cloud.sys.application.assembler.PermissionDTOAssembler;
import com.titxu.cloud.sys.application.dto.PermissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限查询服务实现类
 **/
@Service
public class PermissionQueryServiceImpl implements PermissionQueryService {

    private PermissionRepository permissionRepository;

    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<PermissionDTO> listAllPermission() {
        List<Permission> permissionList =getPermissionOrAdminList();
        return PermissionDTOAssembler.getPermissionList(permissionList);
    }

    @Override
    public List<PermissionDTO> treeAllPermission() {
        List<Permission> permissionList =getPermissionOrAdminList();
        return PermissionDTOAssembler.getPermissionTree(permissionList);
    }

    @Override
    public List<PermissionDTO> listAllMenu() {
        List<Permission> permissionList =getPermissionOrAdminList();
        return PermissionDTOAssembler.getMenuList(permissionList);
    }

    @Override
    public List<PermissionDTO> treeAllMenu() {
        List<Permission> permissionList =getPermissionOrAdminList();
        return PermissionDTOAssembler.getMenuTree(permissionList);
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
        return sysPermissionDOList.stream().filter(p -> p.getPermissionCodes() != null).
                flatMap(p -> Arrays.stream(p.getPermissionCodes().trim().split(","))).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getPermissionIds(String userId) {
        List<SysPermissionDO> sysPermissionDOList = getSysPermissionDOList(userId);
        return sysPermissionDOList.stream().map(BaseDO::getId).collect(Collectors.toSet());
    }

    /**
     * 获取权限列表，管理员获取所有权限
     * @return permissionList 权限列表
     */
    private List<Permission> getPermissionOrAdminList(){
        List<Permission> permissionList;
        if (TenantId.PLATFORM_TENANT.equals(TenantContext.getTenantId())) {
            permissionList = permissionRepository.queryList(new HashMap<>());
        } else {
            permissionList = permissionRepository.queryList(new RoleCode(RoleCode.TENANT_ADMIN));
        }
        return permissionList;
    }

    /**
     * 获取用户权限
     * @param userId 用户id
     * @return 权限列表
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
     * @param parentId 父级id
     * @param menuIdList 用户权限id列表
     * @return 权限列表
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

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
    @Autowired
    public void setSysPermissionMapper(SysPermissionMapper sysPermissionMapper) {
        this.sysPermissionMapper = sysPermissionMapper;
    }
}
