package com.titxu.cloud.sys.service;

import com.titxu.cloud.sys.dto.LogDTO;

/**
 * 日志保存服务接口
 **/
public interface LogSaveService {

    /**
     * 保存
     *
     * @param logDTO
     */
    void save(LogDTO logDTO);
}
