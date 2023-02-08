package com.titxu.cloud.sys.api;

import com.titxu.cloud.common.core.util.Result;
import com.titxu.cloud.common.core.util.WebUtils;
import com.titxu.cloud.common.log.annotation.SysLog;
import com.titxu.cloud.common.mybatis.util.Page;
import com.titxu.cloud.sys.api.dto.LogDTO;
import com.titxu.cloud.sys.api.service.LogSaveService;
import com.titxu.cloud.sys.application.LogQueryService;
import com.titxu.cloud.sys.application.command.PageCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统日志Controller
 **/
@Tag(name = "日志管理")
@RestController
@RequestMapping("/log")
public class LogController {


    private LogQueryService logQueryService;

    private LogSaveService logSaveService;

    @Autowired
    public void setLogQueryService(LogQueryService logQueryService) {
        this.logQueryService = logQueryService;
    }

    @Autowired
    public void setLogSaveService(LogSaveService logSaveService) {
        this.logSaveService = logSaveService;
    }

    /**
     * 保存日志
     */
    @Operation(summary = "保存日志")
    @SysLog("保存日志")
    @PostMapping("/saveLog")
    public void saveLog(@RequestBody LogDTO logDTO) {
        logSaveService.save(logDTO);
    }

    /**
     * 查询登陆日志
     */
    @Operation(summary = "查询登陆日志")
    @PostMapping("/listLogin")
    @PreAuthorize("hasAuthority('sys:log:list')")
    public Result<Page> listLoginLog(@RequestBody PageCommand pageCommand) {
        pageCommand.setKey("mobile");
        pageCommand.setValue(WebUtils.getUserName());
        Page page = logQueryService.queryPage(pageCommand);
        return Result.ok(page);
    }

    /**
     * 列表
     */
    @Operation(summary = "分页查询日志")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:log:list')")
    public Result<Page> list(@RequestParam Map<String, Object> params) {
        Page page = logQueryService.queryPage(params);
        return Result.ok(page);
    }

}
