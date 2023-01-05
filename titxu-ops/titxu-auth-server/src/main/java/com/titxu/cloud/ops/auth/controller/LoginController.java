package com.titxu.cloud.ops.auth.controller;

import com.titxu.cloud.common.web.util.Result;
import com.titxu.cloud.ops.auth.application.LoginService;
import com.titxu.cloud.ops.auth.application.command.LoginPasswordCommand;
import com.titxu.cloud.ops.auth.application.command.RefreshCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 登录Controller
 */
@Api(tags = "登陆")
@RestController
@RequestMapping("/account")
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
        return Result.ok().put("data", loginService.login(loginPasswordCommand));
    }
    /**
     * 刷新登陆
     */
    @ApiOperation("刷新登陆")
    @PostMapping("/refresh")
    public Result refresh(@RequestBody RefreshCommand refreshCommand) {
        return Result.ok().put("data", loginService.refresh(refreshCommand));
    }
    /**
     * 退出登陆
     */
    @ApiOperation("退出登陆")
    @PostMapping("/logout")
    public Result logout() {
        // TODO 退出登陆 1.删除token 2.删除redis中的用户信息
        return Result.ok();
    }
}
