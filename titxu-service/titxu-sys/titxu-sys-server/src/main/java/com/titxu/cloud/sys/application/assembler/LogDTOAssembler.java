package com.titxu.cloud.sys.application.assembler;

import com.titxu.cloud.sys.api.dto.LogDTO;
import com.titxu.cloud.sys.domain.model.log.Log;
import com.titxu.cloud.sys.domain.model.user.Mobile;
import com.titxu.cloud.sys.domain.model.user.UserNick;


/**
 * 日志Assembler
 **/
public class LogDTOAssembler {

    public static Log toLog(final LogDTO logDTO) {

        return new Log(
                null,
                logDTO.getUserNick() == null ? null : new UserNick(logDTO.getUserNick()),
                logDTO.getMobile() == null ? null : new Mobile(logDTO.getMobile()),
                logDTO.getOperation(),
                logDTO.getMethod(),
                logDTO.getParams(),
                logDTO.getTime(),
                logDTO.getIp());
    }
}
