package com.titxu.cloud.sys.application.impl;

import com.titxu.cloud.sys.application.PermissionApplicationService;
import com.titxu.cloud.sys.application.command.PermissionCommand;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PermissionApplicationServiceImplTest {

    @Resource
    private PermissionApplicationService permissionApplicationService;

    @Test
    void saveOrUpdate() {
        PermissionCommand command = new PermissionCommand();
        command.setParentId("1362321550283399170");
        command.setPermissionType("1");
        command.setMenuUrl("sys/menu");
        command.setPermissionName("菜单管理");
        permissionApplicationService.saveOrUpdate(command);
    }
}