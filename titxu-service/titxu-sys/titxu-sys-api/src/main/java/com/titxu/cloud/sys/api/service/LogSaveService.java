package com.titxu.cloud.sys.api.service;

import com.titxu.cloud.sys.api.dto.LogDTO;

/**
 * 日志保存服务接口
 **/
public interface LogSaveService {

    /**
     * 保存日志
     *
     * @param logDTO 日志
     */
    void save(LogDTO logDTO);
}
