package com.titxu.cloud.sys.api;

import com.titxu.cloud.common.core.util.WebUtils;
import com.titxu.cloud.common.log.annotation.SysLog;
import com.titxu.cloud.common.mybatis.constant.PageConstant;
import com.titxu.cloud.common.mybatis.util.Page;
import com.titxu.cloud.common.web.util.Result;
import com.titxu.cloud.common.web.util.validator.ValidatorUtils;
import com.titxu.cloud.common.web.util.validator.group.AddGroup;
import com.titxu.cloud.common.web.util.validator.group.UpdateGroup;
import com.titxu.cloud.sys.application.UserApplicationService;
import com.titxu.cloud.sys.application.UserQueryService;
import com.titxu.cloud.sys.application.command.PasswordCommand;
import com.titxu.cloud.sys.application.command.UserCommand;
import com.titxu.cloud.sys.application.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 用户Controller
 **/
@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {


    private UserApplicationService userApplicationService;


    private UserQueryService userQueryService;

    @Autowired
    public void setUserApplicationService(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @Autowired
    public void setUserQueryService(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    /**
     * 用户分页查询
     */
    @Operation(summary = "用户分页查询")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result list(@RequestParam Map<String, Object> params) {
        Page page = userQueryService.queryPage(params);
        return Result.ok().put(PageConstant.PAGE, page);
    }

    /**
     * 获取登录的用户信息
     */
    @Operation(summary = "获取登录的用户信息")
    @GetMapping("/info")
    public com.titxu.cloud.common.core.util.Result<UserDTO> info() {
        return com.titxu.cloud.common.core.util.Result.ok(userQueryService.find(WebUtils.getUserId()));
    }

    /**
     * 修改登录用户密码
     */
    @Operation(summary = "修改密码")
    @SysLog("修改密码")
    @PostMapping("/password")
    public Result changePassword(@RequestBody PasswordCommand passwordCommand) {
        ValidatorUtils.validateEntity(passwordCommand);
        passwordCommand.setUserId(WebUtils.getUserId());
        userApplicationService.changePassword(passwordCommand);
        return Result.ok();
    }

    /**
     * 用户信息
     */
    @Operation(summary = "用户信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:info')")
    public com.titxu.cloud.common.core.util.Result<UserDTO> info(@PathVariable("id") String id) {
        return com.titxu.cloud.common.core.util.Result.ok(userQueryService.find(id));
    }

    /**
     * 保存用户
     */
    @Operation(summary = "保存用户")
    @SysLog("保存用户")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result save(@RequestBody UserCommand userCommand) {
        ValidatorUtils.validateEntity(userCommand, AddGroup.class);
        userApplicationService.save(userCommand);
        return Result.ok();
    }

    /**
     * 修改用户
     */
    @Operation(summary = "修改用户")
    @SysLog("修改用户")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result update(@RequestBody UserCommand userCommand) {
        ValidatorUtils.validateEntity(userCommand, UpdateGroup.class);
        userApplicationService.update(userCommand);
        return Result.ok();
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户")
    @SysLog("删除用户")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result delete(@RequestBody String[] userIds) {
        userApplicationService.deleteBatch(Arrays.asList(userIds));
        return Result.ok();
    }

    /**
     * 禁用用户
     */
    @Operation(summary = "禁用用户")
    @SysLog("禁用用户")
    @PostMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('sys:user:disable')")
    public Result disable(@PathVariable("id") String id) {
        userApplicationService.disable(id);
        return Result.ok();
    }
}
