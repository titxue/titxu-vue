package com.titxu.cloud.sys.application.impl;

import com.titxu.cloud.sys.api.dto.LogDTO;
import com.titxu.cloud.sys.api.service.LogSaveService;
import com.titxu.cloud.sys.application.assembler.LogDTOAssembler;
import com.titxu.cloud.sys.domain.model.log.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志保存服务实现
 *

 
 **/
@Service
public class LogSaveServiceImpl implements LogSaveService {


    private LogRepository logRepository;

    @Autowired
    public void setLogRepository(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void save(LogDTO logDTO) {
        logRepository.store(LogDTOAssembler.toLog(logDTO));
    }
}
