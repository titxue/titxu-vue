package com.titxu.cloud.management.auth.api;


import cn.hutool.core.util.StrUtil;
import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.core.util.BasicAuth;
import com.titxu.cloud.common.core.util.Result;
import com.titxu.cloud.common.core.util.SpringContextHolder;
import com.titxu.cloud.common.log.annotation.SysLog;
import com.titxu.cloud.common.security.annotation.Inner;
import com.titxu.cloud.management.auth.application.command.LoginPasswordCommand;
import com.titxu.cloud.management.auth.application.dto.OAuth2Dto;
import com.titxu.cloud.management.auth.infrastructure.feign.RemoteAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

/**
 * 用户登陆
 **/
@Tag(name = "用户登陆")
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {
    // 封装 basic auth 认证信息
    private final String basicAuth = BasicAuth.generateBasicAuth(AuthConstants.ADMIN_CLIENT_ID, AuthConstants.ADMIN_CLIENT_SECRET);
    private RemoteAuthService remoteAuthService;
    private OAuth2AuthorizationService authorizationService;

    private CacheManager cacheManager;

    @Autowired
    public void setRemoteAuthService(RemoteAuthService remoteAuthService) {
        this.remoteAuthService = remoteAuthService;
    }

    @Autowired
    public void setAuthorizationService(OAuth2AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 获取微服务 服务列表
     */
    @Operation(summary = "登陆接口")
    @SysLog("登陆操作")
    @PostMapping("/login")
    public Result<Map<String, Object>> loginByMobile(@RequestBody LoginPasswordCommand command) {
        // 进行参数转换后期再OAuth2Dto实现参数验证，参数封装转换指定类
        OAuth2Dto dto = OAuth2Dto.builder().password(command.getMobile(), command.getPassword()).build();
        Map<String, Object> res = remoteAuthService.login(dto.getGrant_type(), dto.getScope(), dto.getUsername(), dto.getPassword(), basicAuth);
        return Result.ok(res);

    }

    /**
     * 刷新token Multi
     */
    @Operation(summary = "刷新token")
    @PostMapping("/refresh")
    public Result<?> refresh(@RequestParam("refreshToken") String refreshToken) {
        OAuth2Dto dto = OAuth2Dto.builder().refreshToken(refreshToken).build();
        /*
         todo
          1.编译参数必须加上 -parameters 不然不能解析到参数名
          2.原因是 LocalVariableTableParameterNameDiscoverer 在最新的jdk17版本中已被弃用并被标记为移除
          (LocalVariableTableParameterNameDiscoverer是ParameterNameDiscoverer的一个实现类，用于找出参数名。
          它是Spring的一个经典实现，早在Spring Framework 2.0就已出现。
          我们知道java代码编译后，默认情况下参数名是不会保留的，而它利用了LocalVariableTable + ASM字节码技术实现了参数名的查找)
          3.改为为手动传递参数名称
          4.或者使用spring-boot-starter-actuator依赖，它会自动开启参数名的保留
          5.后期优化为重写自定义参数解析器
         */
        return Result.ok(remoteAuthService.refreshToken(dto.getGrant_type(), dto.getRefresh_token(), basicAuth));
    }


    /**
     * 退出并删除token
     *
     * @param authHeader Authorization
     */
    @DeleteMapping("/logout")
    public Result<?> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StrUtil.isBlank(authHeader)) {
            return Result.ok();
        }

        String tokenValue = authHeader.replace(AuthConstants.AUTHORIZATION_PREFIX, StrUtil.EMPTY).trim();
        return removeToken(tokenValue);
    }


    /**
     * 令牌管理调用
     *
     * @param token token
     */
    @Inner
    @DeleteMapping("/{token}")
    public Result<?> removeToken(@PathVariable("token") String token) {
        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (authorization == null) {
            return Result.ok();
        }

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken == null || StrUtil.isBlank(accessToken.getToken().getTokenValue())) {
            return Result.ok();
        }
        // 清空用户信息
        Objects.requireNonNull(cacheManager.getCache(AuthConstants.USER_DETAILS)).evict(authorization.getPrincipalName());
        // 清空access token
        authorizationService.remove(authorization);
        // 处理自定义退出事件，保存相关日志
        SpringContextHolder.publishEvent(new LogoutSuccessEvent(new PreAuthenticatedAuthenticationToken(
                authorization.getPrincipalName(), authorization.getRegisteredClientId())));
        return Result.ok();
    }
}
