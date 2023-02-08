package com.titxu.cloud.sys.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.titxu.cloud.sys.domain.model.permission.Permission;
import com.titxu.cloud.sys.domain.model.permission.PermissionId;
import com.titxu.cloud.sys.domain.model.permission.PermissionName;
import com.titxu.cloud.sys.domain.model.permission.PermissionRepository;
import com.titxu.cloud.sys.domain.model.role.RoleCode;
import com.titxu.cloud.sys.infrastructure.persistence.converter.PermissionConverter;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysPermissionDO;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysPermissionMapper;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysRolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 权限-Repository实现类
 **/
@Repository
public class PermissionRepositoryImpl extends ServiceImpl<SysPermissionMapper, SysPermissionDO> implements PermissionRepository, IService<SysPermissionDO> {


    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public List<Permission> queryList(Map<String, Object> params) {
        List<Permission> permissions = new ArrayList<>();
        List<SysPermissionDO> list = getBaseMapper().queryList(params);
        for (SysPermissionDO sysPermissionDO : list) {
            Permission permission = PermissionConverter.toPermission(sysPermissionDO, getParentPermission(sysPermissionDO.getParentId()), null);
            permissions.add(permission);
        }
        return permissions;
    }

    @Override
    public List<Permission> queryList(RoleCode rolecode) {
        List<Permission> permissions = new ArrayList<>();
        List<SysPermissionDO> list = getBaseMapper().queryPermissionByRoleCode(rolecode.getCode());
        for (SysPermissionDO sysPermissionDO : list) {
            Permission permission = PermissionConverter.toPermission(sysPermissionDO, getParentPermission(sysPermissionDO.getParentId()), null);
            permissions.add(permission);
        }
        return permissions;
    }

    @Override
    public Permission find(PermissionId permissionId) {
        SysPermissionDO sysPermissionDO = this.getById(permissionId.getId());
        if (sysPermissionDO == null) {
            return null;
        }
        return PermissionConverter.toPermission(sysPermissionDO, getParentPermission(sysPermissionDO.getParentId()), getSubPermission(sysPermissionDO.getId()));
    }

    @Override
    public Permission find(PermissionName permissionName) {
        SysPermissionDO sysPermissionDO = this.getOne(new QueryWrapper<SysPermissionDO>().eq("permission_name", permissionName.getName()));
        if (sysPermissionDO == null) {
            return null;
        }
        return PermissionConverter.toPermission(sysPermissionDO, getParentPermission(sysPermissionDO.getParentId()), getSubPermission(sysPermissionDO.getId()));
    }

    /**
     * 设置父权限
     * @param parentId 父权限id
     */
    private SysPermissionDO getParentPermission(String parentId) {
        SysPermissionDO parent = null;
        if (parentId != null) {
            parent = this.getById(parentId);
        }
        return parent;
    }

    /**
     * 设置子权限
     * @param permissionId 权限id
     */
    private List<SysPermissionDO> getSubPermission(String permissionId) {
        return this.list(new QueryWrapper<SysPermissionDO>().eq("parent_id", permissionId));
    }

    @Override
    public PermissionId store(Permission permission) {
        SysPermissionDO sysPermissionDO = PermissionConverter.fromPermission(permission);
        this.saveOrUpdate(sysPermissionDO);
        return new PermissionId(sysPermissionDO.getId());
    }

    @Override
    public void remove(PermissionId permissionId) {
        this.removeById(permissionId.getId());
        List<String> permissionIds = new ArrayList<>();
        permissionIds.add(permissionId.getId());
        sysRolePermissionMapper.deleteByPermissionIds(permissionIds);
    }

    @Override
    public void remove(List<PermissionId> permissionIds) {
        // 转换为字符串id集合
        List<String> ids = permissionIds.stream().map(PermissionId::getId).toList();
        this.removeByIds(ids);
        sysRolePermissionMapper.deleteByPermissionIds(ids);
    }

    @Autowired
    public void setSysRolePermissionMapper(SysRolePermissionMapper sysRolePermissionMapper) {
        this.sysRolePermissionMapper = sysRolePermissionMapper;
    }
}
