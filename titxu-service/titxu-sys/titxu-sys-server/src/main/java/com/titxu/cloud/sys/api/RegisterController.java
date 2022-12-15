package com.titxu.cloud.sys.api;

import com.titxu.cloud.sys.application.RegisterApplicationService;
import com.titxu.cloud.sys.application.command.RegisterTenantCommand;
import com.titxu.cloud.common.log.SysLog;
import com.titxu.cloud.common.web.util.Result;
import com.titxu.cloud.common.web.util.validator.ValidatorUtils;
import com.titxu.cloud.common.web.util.validator.group.AddGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册Controller
 *

 * @date 2021-02-13
 **/
@Api(tags = "注册")
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterApplicationService registerApplicationService;

    /**
     * 注册租户
     */
    @ApiOperation("注册租户")
    @SysLog("注册租户")
    @PostMapping("/tenant")
    public Result registerTenantAndUser(@RequestBody RegisterTenantCommand registerTenantCommand) {
        ValidatorUtils.validateEntity(registerTenantCommand, AddGroup.class);
        registerApplicationService.registerTenant(registerTenantCommand);
        return Result.ok();
    }
}
