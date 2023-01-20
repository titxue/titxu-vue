package com.titxu.cloud.sys.infrastructure.persistence.converter;

import com.titxu.cloud.sys.domain.model.log.Log;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysLogDO;

/**
 * 日志转换类
 **/
public class LogConverter {

    public static SysLogDO fromLog(Log log) {
        SysLogDO sysLogDO = new SysLogDO();
        sysLogDO.setUserNick(log.getUserNick() == null ? null : log.getUserNick().getName());
        sysLogDO.setMobile(log.getMobile() == null ? null : log.getMobile().getMobile());
        sysLogDO.setIp(log.getIp());
        sysLogDO.setMethod(log.getMethod());
        sysLogDO.setOperation(log.getOperation());
        sysLogDO.setTime(log.getTime());
        return sysLogDO;
    }
}
