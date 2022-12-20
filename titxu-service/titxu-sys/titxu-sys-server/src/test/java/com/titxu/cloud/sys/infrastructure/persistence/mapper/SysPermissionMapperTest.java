package com.titxu.cloud.sys.infrastructure.persistence.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SysPermissionMapperTest {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Test
    void queryPermissionByUserId() {
        sysPermissionMapper.queryPermissionByUserId("1408403800345300994");
    }
}