package com.titxu.cloud.sys.domain.service;

import com.titxu.cloud.common.core.domain.StatusEnum;
import com.titxu.cloud.sys.domain.model.permission.Permission;
import com.titxu.cloud.sys.domain.model.permission.PermissionId;
import com.titxu.cloud.sys.domain.model.permission.PermissionLevelEnum;
import com.titxu.cloud.sys.domain.model.permission.PermissionRepository;
import com.titxu.cloud.sys.domain.model.role.*;
import com.titxu.cloud.sys.domain.model.tenant.*;
import com.titxu.cloud.sys.domain.model.user.*;
import com.titxu.cloud.sys.domain.specification.TenantCreateSpecification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户注册服务
 **/
public class TenantRegisterService {

    private final TenantRepository tenantRepository;

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final UserRepository userRepository;

    public TenantRegisterService(TenantRepository tenantRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, UserRepository userRepository) {
        this.tenantRepository = tenantRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    public void registerTenant(TenantName tenantName, TenantCode tenantCode, Mobile mobile, Password password, UserNick userNick) {
        Tenant tenant = new Tenant(tenantCode, tenantName);
        TenantCreateSpecification roleCreateSpecification = new TenantCreateSpecification(tenantRepository);
        roleCreateSpecification.isSatisfiedBy(tenant);
        // 保存租户
        TenantId tenantId = tenantRepository.store(tenant);
        // 保存角色
        Map<String, Object> param = new HashMap<>();
        param.put("permissionLevel", PermissionLevelEnum.TENANT.getValue());
        List<Permission> tenantPermission = permissionRepository.queryList(param);
        List<PermissionId> tenantPermissionId = new ArrayList<>();
        for (Permission permission : tenantPermission) {
            tenantPermissionId.add(permission.getPermissionId());
        }
        Role adminRole = new Role(new RoleCode(RoleCode.TENANT_ADMIN), new RoleName(RoleName.TENANT_ADMIN), tenantPermissionId);
        RoleId adminRoleId = roleRepository.store(adminRole);
        // 保存用户
        List<RoleId> roleIdList = new ArrayList<>();
        roleIdList.add(adminRoleId);
        UserFactory userFactory = new UserFactory(userRepository);
        User user = userFactory.createUser(mobile, null, password, userNick, roleIdList, new TenantId(tenantId.getId()), StatusEnum.ENABLE);
        UserId userId = userRepository.store(user);
        tenant = new Tenant(tenantId, tenantCode, tenantName, StatusEnum.ENABLE, userId);
        tenantRepository.store(tenant);
    }
}
