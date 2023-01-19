package com.titxu.cloud.sys.application.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.titxu.cloud.common.core.domain.StatusEnum;
import com.titxu.cloud.common.mybatis.util.Page;
import com.titxu.cloud.common.mybatis.util.PageAssembler;
import com.titxu.cloud.common.mybatis.util.Query;
import com.titxu.cloud.sys.application.RoleQueryService;
import com.titxu.cloud.sys.application.assembler.RoleDTOAssembler;
import com.titxu.cloud.sys.application.dto.RoleDTO;
import com.titxu.cloud.sys.domain.model.role.RoleId;
import com.titxu.cloud.sys.domain.model.role.RoleRepository;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysRoleDO;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色查询服务实现类
 **/
@Service
public class RoleQueryServiceImpl implements RoleQueryService {


    private RoleRepository roleRepository;


    private SysRoleMapper sysRoleMapper;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setSysRoleMapper(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        IPage<SysRoleDO> page = sysRoleMapper.queryList(new Query<SysRoleDO>().getPage(params), params);
        return PageAssembler.toPage(page);
    }

    @Override
    public List<RoleDTO> listAll() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("status", StatusEnum.ENABLE.getValue());
        return RoleDTOAssembler.getRoleDTOList(sysRoleMapper.queryList(param));
    }

    @Override
    public RoleDTO getById(String id) {
        return RoleDTOAssembler.fromRole(roleRepository.find(new RoleId(id)));
    }
}
