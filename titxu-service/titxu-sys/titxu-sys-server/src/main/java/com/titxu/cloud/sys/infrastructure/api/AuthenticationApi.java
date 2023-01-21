package com.titxu.cloud.sys.infrastructure.api;

import com.titxu.cloud.common.security.annotation.Inner;
import com.titxu.cloud.sys.api.dto.AuthenticationDTO;
import com.titxu.cloud.sys.api.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/remote/auth")
public class AuthenticationApi {

    private AuthenticationService authenticationService;

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 校验验证码 1. 校验验证码是否正确 2. 校验验证码是否过期
     *
     * @param uuid        uuid
     * @param captchaCode 验证码
     * @return 是否正确
     */
    @Inner
    @PostMapping("/validateCaptcha")
    public boolean validateCaptcha(@RequestParam("uuid") String uuid, @RequestParam("captchaCode") String captchaCode) {
        return authenticationService.validateCaptcha(uuid, captchaCode);
    }


    /**
     * 根据用户名获取用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    @Inner
    @PostMapping("/loginByUserName")
    public AuthenticationDTO loginByUserName(@RequestParam("userName") String userName) {
        return authenticationService.loginByUserName(userName);
    }
}
