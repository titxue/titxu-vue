package com.titxu.cloud.sys.infrastructure.persistence.converter;

import com.titxu.cloud.sys.domain.model.log.Log;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysLogDO;

/**
 * 日志转换类
 **/
public class LogConverter {

    public static SysLogDO fromLog(Log log) {
        SysLogDO sysLogDO = new SysLogDO();
        sysLogDO.setUserName(log.getUserName() == null ? null : log.getUserName().getName());
        sysLogDO.setIp(log.getIp());
        sysLogDO.setMethod(log.getMethod());
        sysLogDO.setOperation(log.getOperation());
        sysLogDO.setTime(log.getTime());
        return sysLogDO;
    }
}
