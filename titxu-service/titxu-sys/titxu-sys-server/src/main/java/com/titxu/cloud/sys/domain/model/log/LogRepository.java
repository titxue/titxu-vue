package com.titxu.cloud.sys.domain.model.log;

/**
 * 日志-Repository接口
 **/
public interface LogRepository {

    /**
     * 保存日志
     *
     * @param log
     */
    void store(Log log);
}
