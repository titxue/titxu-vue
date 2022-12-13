package com.titxu.cloud.sys.infrastructure.api;

import com.titxu.cloud.sys.domain.model.log.Log;
import com.titxu.cloud.sys.domain.model.log.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.titxu.cloud.common.web.util.Result;

//@RestController
public class LogApi {

    private LogRepository logRepository;

    @Autowired
    public void setLogRepository(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @PostMapping("store")
    public void store(Log log){
        logRepository.store(log);
    }

}
