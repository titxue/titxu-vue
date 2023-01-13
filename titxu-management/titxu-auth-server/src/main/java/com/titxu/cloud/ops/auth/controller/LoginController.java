package com.titxu.cloud.ops.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.core.util.SpringContextHolder;
import com.titxu.cloud.common.core.util.TokenUtils;
import com.titxu.cloud.common.security.annotation.Inner;
import com.titxu.cloud.common.web.util.Result;
import com.titxu.cloud.ops.auth.application.LoginService;
import com.titxu.cloud.ops.auth.application.command.LoginPasswordCommand;
import com.titxu.cloud.ops.auth.application.command.RefreshCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

/**
 * 认证
 */
@Tag(name = "登陆")
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class LoginController {

    private final OAuth2AuthorizationService authorizationService;
    private final CacheManager cacheManager;
    private LoginService loginService;

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 认证页面
     *
     * @param modelAndView 模型视图
     * @param error        表单登录失败处理回调的错误信息
     * @return ModelAndView 模型视图
     */
    @RequestMapping("/login")
    public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
        modelAndView.setViewName("login");
        modelAndView.addObject("error", error);
        return modelAndView;
    }


    /**
     * 退出并删除token
     *
     * @param authHeader Authorization
     */
    @DeleteMapping("/logout")
    public Result logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StrUtil.isBlank(authHeader)) {
            return Result.ok();
        }

        String tokenValue = authHeader.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
        return removeToken(tokenValue);
    }

//    /**
//     * 校验token
//     * @param token 令牌
//     */
//    @SneakyThrows
//    @GetMapping("/check_token")
//    public void checkToken(String token, HttpServletResponse response, HttpServletRequest request) {
//        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
//
//        if (StrUtil.isBlank(token)) {
//            httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
//            this.authenticationFailureHandler.onAuthenticationFailure(request, response,
//                    new InvalidBearerTokenException(OAuth2ErrorCodesExpand.TOKEN_MISSING));
//            return;
//        }
//        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
//
//        // 如果令牌不存在 返回401
//        if (authorization == null || authorization.getAccessToken() == null) {
//            this.authenticationFailureHandler.onAuthenticationFailure(request, response,
//                    new InvalidBearerTokenException(OAuth2ErrorCodesExpand.INVALID_BEARER_TOKEN));
//            return;
//        }
//
//        Map<String, Object> claims = authorization.getAccessToken().getClaims();
//        OAuth2AccessTokenResponse sendAccessTokenResponse = OAuth2EndpointUtils.sendAccessTokenResponse(authorization,
//                claims);
//        this.accessTokenHttpResponseConverter.write(sendAccessTokenResponse, MediaType.APPLICATION_JSON, httpResponse);
//    }

    /**
     * 令牌管理调用
     *
     * @param token token
     */
    @Inner
    @DeleteMapping("/{token}")
    public Result removeToken(@PathVariable("token") String token) {
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


    /**
     * 密码登陆
     */
    @Operation(summary = "密码登陆")
    @PostMapping("/password")
    public Result login(@RequestBody LoginPasswordCommand loginPasswordCommand) {
        return Result.ok().put("data", loginService.login(loginPasswordCommand));
    }

    /**
     * 刷新登陆
     */
    @Operation(summary = "刷新登陆")
    @PostMapping("/refresh")
    public Result refresh(@RequestHeader("Authorization") String authorization) {
        String refreshToken = TokenUtils.removeTokenPrefix(authorization);
        return Result.ok().put("data", loginService.refresh(new RefreshCommand(refreshToken)));
    }

    /**
     * 退出登陆
     */
    @Operation(summary = "退出登陆")
    @PostMapping("/logout")
    public Result logout() {
        // TODO 退出登陆 1.删除token 2.删除redis中的用户信息
        return Result.ok();
    }
}
