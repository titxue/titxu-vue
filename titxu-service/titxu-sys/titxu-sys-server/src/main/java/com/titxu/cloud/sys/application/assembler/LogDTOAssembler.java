package com.titxu.cloud.sys.application.assembler;

import com.titxu.cloud.sys.domain.model.log.Log;
import com.titxu.cloud.sys.domain.model.user.UserNick;
import com.titxu.cloud.sys.dto.LogDTO;

/**
 * 日志Assembler
 **/
public class LogDTOAssembler {

    public static Log toLog(final LogDTO logDTO) {
        Log log = new Log(null, logDTO.getUserNick() == null ? null : new UserNick(logDTO.getUserNick()), logDTO.getOperation(), logDTO.getMethod(),
                logDTO.getParams(), logDTO.getTime(), logDTO.getIp());

        return log;
    }
}
