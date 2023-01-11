package com.titxu.cloud.sys.api;

import com.titxu.cloud.common.log.annotation.SysLog;
import com.titxu.cloud.common.mybatis.constant.PageConstant;
import com.titxu.cloud.common.mybatis.util.Page;
import com.titxu.cloud.common.web.util.Result;
import com.titxu.cloud.common.web.util.validator.ValidatorUtils;
import com.titxu.cloud.sys.application.RoleApplicationService;
import com.titxu.cloud.sys.application.RoleQueryService;
import com.titxu.cloud.sys.application.command.RoleCommand;
import com.titxu.cloud.sys.application.dto.RoleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 角色Controller
 **/
@Tag(name = "角色管理")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleQueryService roleQueryService;

    @Autowired
    private RoleApplicationService roleApplicationService;

    /**
     * 角色分页查询
     */
    @Operation(summary = "角色分页查询")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result list(@RequestParam Map<String, Object> params) {
        Page page = roleQueryService.queryPage(params);
        return Result.ok().put(PageConstant.PAGE, page);
    }

    /**
     * 角色列表
     */
    @Operation(summary = "角色列表")
    @GetMapping("/select")
    @PreAuthorize("hasAuthority('sys:role:select')")
    public Result select() {
        List<RoleDTO> list = roleQueryService.listAll();
        return Result.ok().put("list", list);
    }

    /**
     * 角色信息
     */
    @Operation(summary = "角色信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:info')")
    public Result info(@PathVariable("id") String id) {
        RoleDTO role = roleQueryService.getById(id);
        return Result.ok().put("role", role);
    }

    /**
     * 保存角色
     */
    @Operation(summary = "保存角色")
    @SysLog("保存角色")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result save(@RequestBody RoleCommand roleCommand) {
        ValidatorUtils.validateEntity(roleCommand);
        roleApplicationService.saveOrUpdate(roleCommand);
        return Result.ok();
    }

    /**
     * 修改角色
     */
    @Operation(summary = "修改角色")
    @SysLog("修改角色")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result update(@RequestBody RoleCommand roleCommand) {
        ValidatorUtils.validateEntity(roleCommand);
        roleApplicationService.saveOrUpdate(roleCommand);
        return Result.ok();
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色")
    @SysLog("删除角色")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result delete(@RequestBody String[] roleIds) {
        roleApplicationService.deleteBatch(Arrays.asList(roleIds));
        return Result.ok();
    }

    /**
     * 禁用角色
     */
    @Operation(summary = "禁用角色")
    @SysLog("禁用角色")
    @PostMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('sys:role:disable')")
    public Result disable(@PathVariable("id") String id) {
        roleApplicationService.disable(id);
        return Result.ok();
    }
}
