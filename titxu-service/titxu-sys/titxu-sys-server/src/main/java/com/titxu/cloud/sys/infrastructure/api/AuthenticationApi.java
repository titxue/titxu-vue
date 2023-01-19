package com.titxu.cloud.sys.infrastructure.api;

import com.titxu.cloud.common.security.annotation.Inner;
import com.titxu.cloud.sys.dto.AuthenticationDTO;
import com.titxu.cloud.sys.service.AuthenticationService;
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

    @Inner
    @PostMapping("/validateCaptcha")
    public boolean validateCaptcha(@RequestParam("uuid") String uuid, @RequestParam("captchaCode") String captchaCode) {
        return authenticationService.validateCaptcha(uuid, captchaCode);
    }


    @Inner
    @PostMapping("/loginByUserName")
    public AuthenticationDTO loginByUserName(@RequestParam("userName") String userName) {
        return authenticationService.loginByUserName(userName);
    }
}
