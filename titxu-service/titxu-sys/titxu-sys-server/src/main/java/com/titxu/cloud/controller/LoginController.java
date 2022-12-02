package com.titxu.cloud.sys.controller;


import com.titxu.cloud.common.web.util.Result;
import com.titxu.cloud.sys.application.CodeApplicationService;
import com.titxu.cloud.sys.application.command.MobileCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户登陆")
@RestController
@RequestMapping("/sys")
@AllArgsConstructor
public class LoginController {


    private CodeApplicationService codeApplicationService;

    /**
     * 手机验证码登陆
     */
    @ApiOperation("手机验证码登陆")
    @PostMapping("/loginByMobile")
    public Result loginByMobile(@RequestBody MobileCommand command) {
        return Result.ok().put("test", "test");
    }

    /**
     * 发送验证码
     */
    @ApiOperation("发送验证码")
    @PostMapping("/sendCode")
    public Result sendCode(@RequestParam String mobile) {
        codeApplicationService.sendCode(mobile);
        return Result.ok();
    }
}
