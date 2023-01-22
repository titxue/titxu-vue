package com.titxu.cloud.sys.api;

import com.titxu.cloud.common.log.annotation.SysLog;
import com.titxu.cloud.common.web.util.Result;
import com.titxu.cloud.common.web.util.validator.ValidatorUtils;
import com.titxu.cloud.common.web.util.validator.group.AddGroup;
import com.titxu.cloud.sys.application.RegisterApplicationService;
import com.titxu.cloud.sys.application.command.RegisterTenantCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册Controller
 **/
@Tag(name = "注册")
@RestController
@RequestMapping("/register")
public class RegisterController {

    private RegisterApplicationService registerApplicationService;

    /**
     * 注册租户
     */
    @Operation(summary = "注册租户")
    @SysLog("注册租户")
    @PostMapping("/tenant")
    public Result registerTenantAndUser(@RequestBody RegisterTenantCommand registerTenantCommand) {
        ValidatorUtils.validateEntity(registerTenantCommand, AddGroup.class);
        registerApplicationService.registerTenant(registerTenantCommand);
        return Result.ok();
    }

    @Autowired
    public void setRegisterApplicationService(RegisterApplicationService registerApplicationService) {
        this.registerApplicationService = registerApplicationService;
    }
}
