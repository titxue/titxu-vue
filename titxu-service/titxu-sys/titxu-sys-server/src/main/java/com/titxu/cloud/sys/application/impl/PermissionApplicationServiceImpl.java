package com.titxu.cloud.sys.application.impl;

import com.titxu.cloud.sys.application.PermissionApplicationService;
import com.titxu.cloud.sys.application.assembler.PermissionDTOAssembler;
import com.titxu.cloud.sys.application.command.PermissionCommand;
import com.titxu.cloud.sys.domain.model.permission.Permission;
import com.titxu.cloud.sys.domain.model.permission.PermissionId;
import com.titxu.cloud.sys.domain.model.permission.PermissionRepository;
import com.titxu.cloud.sys.domain.service.PermissionDisableService;
import com.titxu.cloud.sys.domain.specification.PermissionCreateSpecification;
import com.titxu.cloud.sys.domain.specification.PermissionDeleteSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限应用服务实现类
 *

 
 **/
@Service
public class PermissionApplicationServiceImpl implements PermissionApplicationService {

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

    @Override
    public void deleteBatch(List<String> ids) {
        PermissionDeleteSpecification deleteSpecification = new PermissionDeleteSpecification(permissionRepository);

        List<PermissionId> permissionIds = new ArrayList<>();
        ids.forEach(id -> {
            PermissionId permissionId = new PermissionId(id);
            deleteSpecification.isSatisfiedBy(permissionId);
            permissionIds.add(permissionId);
        });
        permissionRepository.remove(permissionIds);
    }

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
}
