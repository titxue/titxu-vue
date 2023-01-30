package com.titxu.cloud.sys.infrastructure.api;

import com.titxu.cloud.common.security.annotation.Inner;
import com.titxu.cloud.sys.api.dto.LogDTO;
import com.titxu.cloud.sys.api.service.LogSaveService;
import com.titxu.cloud.sys.domain.model.log.Log;
import com.titxu.cloud.sys.domain.model.log.LogRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/remote/log")
public class LogApi {
    private LogSaveService logSaveService;
    private LogRepository logRepository;

    @Autowired
    public void setLogRepository(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Autowired
    public void setLogSaveService(LogSaveService logSaveService) {
        this.logSaveService = logSaveService;
    }

    @Inner
    @PostMapping("store")
    public void store(Log log) {
        logRepository.store(log);
    }

    /**
     * 保存日志
     */
    @Inner
    @Operation(summary = "保存日志")
    @PostMapping("/saveLog")
    public void saveLog(@RequestBody LogDTO logDTO) {
        logSaveService.save(logDTO);
    }
}
