package com.titxu.cloud.management.auth.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证中心
 **/
@Tag(name = "认证中心")
@RestController
@RequestMapping("/oauth")
@Slf4j
public class AuthController {
//    grant_type=password&client_id=client&client_secret=123456&mobile=18666666666&password=123456
//    private TokenEndpoint tokenEndpoint;
//
//
//
//    @Autowired
//    public void setTokenEndpoint(TokenEndpoint tokenEndpoint) {
//        this.tokenEndpoint = tokenEndpoint;
//    }
//
//
//
//    @PostMapping("/token")
//    @Operation(summary =value = "OAuth2认证", notes = "login")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "grant_type", defaultValue = "password", value = "授权模式", required = true),
//            @ApiImplicitParam(name = "client_id", defaultValue = "client", value = "Oauth2客户端ID", required = true),
//            @ApiImplicitParam(name = "client_secret", defaultValue = "123456", value = "Oauth2客户端秘钥", required = true),
//            @ApiImplicitParam(name = "refresh_token", value = "刷新token"),
//            @ApiImplicitParam(name = "username", defaultValue = "admin", value = "登录用户名"),
//            @ApiImplicitParam(name = "password", defaultValue = "123456", value = "登录密码"),
//    })
//    public OAuth2AccessToken postAccessToken(
//            Principal principal,
//            @RequestParam Map<String, String> parameters
//    ) throws HttpRequestMethodNotSupportedException {
//        OAuth2AccessToken oAuth2AccessToken;
//        oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
//        return oAuth2AccessToken;
//    }
}
