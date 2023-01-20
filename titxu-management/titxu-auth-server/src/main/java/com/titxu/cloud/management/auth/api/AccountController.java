package com.titxu.cloud.management.auth.api;


import cn.hutool.core.bean.BeanUtil;
import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.core.util.BasicAuth;
import com.titxu.cloud.common.core.util.Result;
import com.titxu.cloud.common.core.util.TokenUtils;
import com.titxu.cloud.management.auth.application.command.LoginPasswordCommand;
import com.titxu.cloud.management.auth.application.dto.OAuth2Dto;
import com.titxu.cloud.management.auth.infrastructure.client.RemoteAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
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
     * 刷新token Multi
     *
     * @see LocalVariableTableParameterNameDiscoverer
     */
    @Operation(summary = "刷新token")
    @PostMapping("/refresh")
    public Result<?> refresh(@RequestHeader("Authorization") String authorization) {
        String refreshToken = TokenUtils.removeTokenPrefix(authorization);
        OAuth2Dto dto = OAuth2Dto.builder().refreshToken(refreshToken).build();

        /*
         todo
          1.编译参数必须加上 -parameters 不然不能解析到参数名
          2.原因是 LocalVariableTableParameterNameDiscoverer 在最新的jdk17版本中已被弃用并被标记为移除
          (LocalVariableTableParameterNameDiscoverer是ParameterNameDiscoverer的一个实现类，用于找出参数名。
          它是Spring的一个经典实现，早在Spring Framework 2.0就已出现。
          我们知道java代码编译后，默认情况下参数名是不会保留的，而它利用了LocalVariableTable + ASM字节码技术实现了参数名的查找)
          3.后期优化为手动传递参数名称
         */
        Map<String, Object> params = BeanUtil.beanToMap(dto, false, true);
        return Result.ok(remoteAuthService.token(params, basicAuth));
    }
}
