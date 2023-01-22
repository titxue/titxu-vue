package com.titxu.cloud.sys.application.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.titxu.cloud.common.mybatis.util.Page;
import com.titxu.cloud.common.mybatis.util.PageAssembler;
import com.titxu.cloud.common.mybatis.util.Query;
import com.titxu.cloud.sys.application.PermissionQueryService;
import com.titxu.cloud.sys.application.UserQueryService;
import com.titxu.cloud.sys.application.assembler.UserDTOAssembler;
import com.titxu.cloud.sys.application.dto.TenantDTO;
import com.titxu.cloud.sys.application.dto.UserDTO;
import com.titxu.cloud.sys.domain.model.user.User;
import com.titxu.cloud.sys.domain.model.user.UserId;
import com.titxu.cloud.sys.domain.model.user.UserRepository;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysTenantDO;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysUserDO;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysTenantMapper;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户查询服务实现类
 **/
@Service
public class UserQueryServiceImpl implements UserQueryService {


    private SysUserMapper sysUserMapper;


    private UserRepository userRepository;


    private SysTenantMapper sysTenantMapper;


    private PermissionQueryService permissionQueryService;

    @Autowired
    public void setSysUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setSysTenantMapper(SysTenantMapper sysTenantMapper) {
        this.sysTenantMapper = sysTenantMapper;
    }

    @Autowired
    public void setPermissionQueryService(PermissionQueryService permissionQueryService) {
        this.permissionQueryService = permissionQueryService;
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<SysUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper
                .eq(false,SysUserDO::getUserNick, params.get("userId"))
                .eq(false,SysUserDO::getUserType, params.get("userType"))
                .eq(false,SysUserDO::getEmail, params.get("email"))
                .eq(false,SysUserDO::getMobile, params.get("mobile"));
        IPage<SysUserDO> page = sysUserMapper.selectPage(new Query<SysUserDO>().getPage(params),lambdaQueryWrapper);
        return PageAssembler.toPage(page);
    }

    @Override
    public UserDTO find(String userId) {
        User user = userRepository.find(new UserId(userId));
        UserDTO userDTO = UserDTOAssembler.fromUser(user);
        SysTenantDO tenantDO = sysTenantMapper.selectById(user.getTenantId());
        userDTO.setTenantName(tenantDO.getTenantName());
        userDTO.setPermissionCodes(permissionQueryService.getPermissionCodes(userId));
        userDTO.setPermissionIds(permissionQueryService.getPermissionIds(userId));
        userDTO.setTenants(getUserTenants(userId));
        return userDTO;
    }

    /**
     * 获取用户关联的租户
     *
     * @param userId 用户id
     * @return 租户列表
     */
    private List<TenantDTO> getUserTenants(String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        List<SysUserDO> sysUserDOList = sysUserMapper.queryUserNoTenant(params);
        List<TenantDTO> tenants = new ArrayList<>();
        for (SysUserDO sysUserDO : sysUserDOList) {
            TenantDTO tenantDTO = new TenantDTO();
            tenantDTO.setTenantId(sysUserDO.getTenantId());
            tenantDTO.setTenantCode(sysUserDO.getTenantCode());
            tenantDTO.setTenantName(sysUserDO.getTenantName());
            tenants.add(tenantDTO);
        }
        return tenants;
    }
}
