package com.titxu.cloud.sys.application.impl;

import com.titxu.cloud.sys.application.assembler.PermissionDTOAssembler;
import com.titxu.cloud.sys.application.command.PermissionCommand;
import com.titxu.cloud.sys.domain.model.permission.Permission;
import com.titxu.cloud.sys.domain.model.permission.PermissionId;
import com.titxu.cloud.sys.domain.model.permission.PermissionRepository;
import com.titxu.cloud.sys.application.PermissionApplicationService;
import com.titxu.cloud.sys.domain.service.PermissionDisableService;
import com.titxu.cloud.sys.domain.specification.PermissionCreateSpecification;
import com.titxu.cloud.sys.domain.specification.PermissionDeleteSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权限应用服务实现类
 *
 * @author haoxin
 * @date 2021-02-17
 **/
@Service
public class PermissionApplicationServiceImpl implements PermissionApplicationService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(PermissionCommand permissionCommand) {
        Permission parent = permissionRepository.find(new PermissionId(permissionCommand.getParentId()));
        Permission permission = PermissionDTOAssembler.toPermission(permissionCommand, parent);
        PermissionCreateSpecification permissionCreateSpecification = new PermissionCreateSpecification(permissionRepository);
        permissionCreateSpecification.isSatisfiedBy(permission);
        permissionRepository.store(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        PermissionId permissionId = new PermissionId(id);
        PermissionDeleteSpecification permissionDeleteSpecification = new PermissionDeleteSpecification(permissionRepository);
        permissionDeleteSpecification.isSatisfiedBy(permissionId);
        permissionRepository.remove(permissionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String id) {
        PermissionDisableService permissionDisableService = new PermissionDisableService(permissionRepository);
        permissionDisableService.disable(new PermissionId(id));
    }
}
