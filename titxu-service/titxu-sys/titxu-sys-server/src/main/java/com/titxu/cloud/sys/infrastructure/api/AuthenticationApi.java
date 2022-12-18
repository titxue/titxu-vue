package com.titxu.cloud.sys.infrastructure.api;

import com.titxu.cloud.sys.dto.AuthenticationDTO;
import com.titxu.cloud.sys.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationApi {

    private AuthenticationService authenticationService;

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("validateCaptcha")
    public boolean validateCaptcha(@RequestParam String uuid, @RequestParam String captchaCode){
        return authenticationService.validateCaptcha(uuid, captchaCode);
    }


    @PostMapping("loginByUserName")
    public AuthenticationDTO loginByUserName(@RequestParam String userName){
        return authenticationService.loginByUserName(userName);
    }
}
