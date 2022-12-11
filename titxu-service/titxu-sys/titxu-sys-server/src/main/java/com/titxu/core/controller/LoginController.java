package com.titxu.core.controller;


import com.titxu.core.sys.application.CodeApplicationService;
import com.titxu.web.util.Response;
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
    public Response loginByMobile() {
        return Response.ok();
    }

    /**
     * 发送验证码
     */
    @ApiOperation("发送验证码")
    @PostMapping("/sendCode")
    public Response sendCode(@RequestParam String mobile) {
        codeApplicationService.sendCode(mobile);
        return Response.ok();
    }
}
