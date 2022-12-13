package com.titxu.cloud.sys.application.impl;

import com.titxu.cloud.sys.application.assembler.LogDTOAssembler;
import com.titxu.cloud.sys.domain.model.log.LogRepository;
import com.titxu.cloud.sys.dto.LogDTO;
import com.titxu.cloud.sys.service.LogSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志保存服务实现
 *

 * @date 2021-06-21
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
