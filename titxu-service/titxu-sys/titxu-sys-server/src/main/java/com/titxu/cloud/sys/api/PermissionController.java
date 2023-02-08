package com.titxu.cloud.sys.api;

import com.titxu.cloud.common.core.util.Result;
import com.titxu.cloud.common.core.util.WebUtils;
import com.titxu.cloud.common.log.annotation.SysLog;
import com.titxu.cloud.common.web.util.validator.ValidatorUtils;
import com.titxu.cloud.common.web.util.validator.group.AddGroup;
import com.titxu.cloud.common.web.util.validator.group.UpdateGroup;
import com.titxu.cloud.sys.application.PermissionApplicationService;
import com.titxu.cloud.sys.application.PermissionQueryService;
import com.titxu.cloud.sys.application.command.PermissionCommand;
import com.titxu.cloud.sys.application.dto.PermissionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 权限Controller
 **/
@Tag(name = "权限管理")
@RestController
@RequestMapping("/permission")
public class PermissionController {

    private PermissionApplicationService permissionApplicationService;

    private PermissionQueryService permissionQueryService;

    /**
     * 导航菜单
     */
    @Operation(summary = "导航菜单")
    @GetMapping("/nav")
    public Result<?> nav() {
        List<PermissionDTO> menuList = permissionQueryService.getUserMenuTree(WebUtils.getUserId());
        Set<String> permissions = permissionQueryService.getPermissionCodes(WebUtils.getUserId());
        Map<String, Object> map = Map.of("menuList", menuList, "permissions", permissions);
        return Result.ok(map);
    }

    /**
     * 所有权限列表
     * 默认树形结构
     * @param isTree 是否树形结构
     */
    @Operation(summary = "所有权限列表")
    @GetMapping("/list")
    public Result<List<PermissionDTO>> list(@RequestParam(value = "isTree", required = false, defaultValue = "true") Boolean isTree) {
        List<PermissionDTO> permissionList;
        if (isTree) {
            permissionList = permissionQueryService.treeAllMenu();
        } else {
            permissionList = permissionQueryService.listAllPermission();
        }

        return Result.ok(permissionList);
    }

    /**
     * 权限树
     */
    @Operation(summary = "权限树")
    @GetMapping("/tree")
    public Result<List<PermissionDTO>> tree() {
        List<PermissionDTO> permissionList = permissionQueryService.treeAllPermission();
        return Result.ok(permissionList);
    }

    /**
     * 菜单列表
     */
    @Operation(summary = "菜单列表")
    @GetMapping("/selectMenu")
    public Result<List<PermissionDTO>> selectMenu() {
        List<PermissionDTO> menuList = permissionQueryService.listAllMenu();
        return Result.ok(menuList);
    }

    /**
     * 权限信息
     */
    @Operation(summary = "权限信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:permission:info')")
    public Result<PermissionDTO> info(@PathVariable("id") String id) {
        PermissionDTO permission = permissionQueryService.getById(id);
        return Result.ok(permission);
    }

    /**
     * 保存权限
     */
    @Operation(summary = "保存权限")
    @SysLog("保存权限")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:permission:save')")
    public Result<?> save(@RequestBody PermissionCommand permissionCommand) {
        ValidatorUtils.validateEntity(permissionCommand, AddGroup.class);
        permissionApplicationService.saveOrUpdate(permissionCommand);
        return Result.ok();
    }
    /**
     * 更新时获取权限信息
     */
    @Operation(summary = "根据id查询权限信息")
    @GetMapping("/update")
    @PreAuthorize("hasAuthority('sys:permission:info')")
    public Result<PermissionDTO> updateById(@RequestParam("id") String id) {
        PermissionDTO permission = permissionQueryService.getById(id);
        return Result.ok(permission);
    }
    /**
     * 修改权限
     */
    @Operation(summary = "修改权限")
    @SysLog("修改权限")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:permission:update')")
    public Result<?> update(@RequestBody PermissionCommand permissionCommand) {
        ValidatorUtils.validateEntity(permissionCommand, UpdateGroup.class);
        permissionApplicationService.saveOrUpdate(permissionCommand);
        return Result.ok();
    }
    /**
     * 删除权限 单个删除
     * @param id 权限id
     * @return Result
     */
    @Operation(summary = "删除权限")
    @SysLog("删除权限")
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:permission:delete')")
    public Result<?> delete(@PathVariable("id") String id) {
        permissionApplicationService.delete(id);
        return Result.ok();
    }
    /**
     * 批量删除权限
     */
    @Operation(summary = "删除权限")
    @SysLog("删除权限")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('sys:permission:delete')")
    public Result<?> delete(@RequestBody String[] ids) {
        permissionApplicationService.deleteBatch(Arrays.asList(ids));
        return Result.ok();
    }

    /**
     * 禁用权限
     */
    @Operation(summary = "禁用权限")
    @SysLog("禁用权限")
    @PostMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('sys:permission:disable')")
    public Result<?> disable(@PathVariable("id") String id) {
        permissionApplicationService.disable(id);
        return Result.ok();
    }

    @Autowired
    public void setPermissionApplicationService(PermissionApplicationService permissionApplicationService) {
        this.permissionApplicationService = permissionApplicationService;
    }

    @Autowired
    public void setPermissionQueryService(PermissionQueryService permissionQueryService) {
        this.permissionQueryService = permissionQueryService;
    }
}
