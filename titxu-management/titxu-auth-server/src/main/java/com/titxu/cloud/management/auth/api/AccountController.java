package com.titxu.cloud.management.auth.api;


import com.titxu.cloud.common.core.util.BasicAuth;
import com.titxu.cloud.common.core.util.Result;
import com.titxu.cloud.management.auth.application.command.LoginPasswordCommand;
import com.titxu.cloud.management.auth.infrastructure.client.RemoteAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户登陆
 **/
@Tag(name = "用户登陆")
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {


    private final RemoteAuthService remoteAuthService;


    public AccountController(RemoteAuthService remoteAuthService) {
        this.remoteAuthService = remoteAuthService;
    }

    /**
     * 获取微服务 服务列表
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> loginByMobile(@RequestBody LoginPasswordCommand loginPasswordCommand) {
        // 封装 basic auth 认证信息
        String basicAuth = BasicAuth.generateBasicAuth("messaging-client", "secret");
        Map<String, Object> res = remoteAuthService.login("password", "message.read", "18555555555", "123456", basicAuth);
        log.info("res:{}", res);
        return Result.ok(res);

    }
}
