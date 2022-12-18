package com.titxu.cloud.sys.api;

import com.titxu.cloud.common.web.util.Result;
import com.titxu.cloud.sys.application.LoginService;
import com.titxu.cloud.sys.application.command.LoginPasswordCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录Controller
 */
@Api(tags = "登陆")
@RestController
@RequestMapping("/login")
public class LoginController {

    private LoginService loginService;


    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 密码登陆
     */
    @ApiOperation("密码登陆")
    @PostMapping("/password")
    public Result login(@RequestBody LoginPasswordCommand loginPasswordCommand) {
        loginService.login(loginPasswordCommand);
        return Result.ok();
    }
}
