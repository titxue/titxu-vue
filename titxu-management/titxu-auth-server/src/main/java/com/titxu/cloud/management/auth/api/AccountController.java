package com.titxu.cloud.management.auth.api;


import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.core.util.BasicAuth;
import com.titxu.cloud.common.core.util.Result;
import com.titxu.cloud.common.core.util.TokenUtils;
import com.titxu.cloud.management.auth.application.command.LoginPasswordCommand;
import com.titxu.cloud.management.auth.application.command.RefreshCommand;
import com.titxu.cloud.management.auth.application.dto.OAuth2Dto;
import com.titxu.cloud.management.auth.infrastructure.client.RemoteAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户登陆
 **/
@Tag(name = "用户登陆")
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    private final String basicAuth = BasicAuth.generateBasicAuth(AuthConstants.ADMIN_CLIENT_ID, AuthConstants.ADMIN_CLIENT_SECRET);
    private final RemoteAuthService remoteAuthService;


    public AccountController(RemoteAuthService remoteAuthService) {
        this.remoteAuthService = remoteAuthService;
    }

    /**
     * 获取微服务 服务列表
     */
    @Operation(summary = "登陆接口")
    @PostMapping("/login")
    public Result<Map<String, Object>> loginByMobile(@RequestBody LoginPasswordCommand command) {
        // 封装 basic auth 认证信息

        Map<String, Object> res = remoteAuthService.login(AuthConstants.GRANT_TYPE_PASSWORD, AuthConstants.ADMIN_SCOPE, command.getMobile(), command.getPassword(), basicAuth);
        return Result.ok(res);

    }

    /**
     * 刷新token
     */
    @Operation(summary = "刷新token")
    @PostMapping("/refresh")
    public Result<Map<String,Object>> refresh(@RequestHeader("Authorization") String authorization) {
        String refreshToken = TokenUtils.removeTokenPrefix(authorization);
        OAuth2Dto dto = OAuth2Dto.builder().refreshToken(refreshToken).build();
        Map<String, Object> token = remoteAuthService.token(dto, basicAuth);
        return Result.ok(token);
    }
}
