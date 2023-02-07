package com.titxu.cloud.sys.api;

import com.titxu.cloud.common.core.util.Result;
import com.titxu.cloud.common.log.annotation.SysLog;
import com.titxu.cloud.common.mybatis.util.Page;
import com.titxu.cloud.sys.application.TenantApplicationService;
import com.titxu.cloud.sys.application.TenantQueryService;
import com.titxu.cloud.sys.application.dto.TenantDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 租户Controller
 **/
@Tag(name = "租户管理")
@RestController
@RequestMapping("/tenant")
public class TenantController {

    private TenantQueryService tenantQueryService;

    private TenantApplicationService tenantApplicationService;

    /**
     * 用户分页查询
     */
    @Operation(summary = "租户分页查询")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:tenant:list')")
    public Result<Page> list(@RequestParam Map<String, Object> params) {
        Page page = tenantQueryService.queryPage(params);
        return Result.ok(page);
    }
    /**
     * 更新查询角色信息
     */
    @Operation(summary = "更新查询租户信息")
    @GetMapping("/update")
    @PreAuthorize("hasAuthority('sys:tenant:info')")
    public Result<TenantDTO> updateInfo(@RequestParam("id") String id) {
        TenantDTO role = tenantQueryService.getById(id);
        return Result.ok(role);
    }
    /**
     * 禁用租户
     */
    @Operation(summary = "禁用租户")
    @SysLog("禁用租户")
    @PostMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('sys:tenant:disable')")
    public Result<?> disable(@PathVariable("id") String id) {
        tenantApplicationService.disable(id);
        return Result.ok();
    }


    @Autowired
    public void setTenantQueryService(TenantQueryService tenantQueryService) {
        this.tenantQueryService = tenantQueryService;
    }
    @Autowired
    public void setTenantApplicationService(TenantApplicationService tenantApplicationService) {
        this.tenantApplicationService = tenantApplicationService;
    }
}
