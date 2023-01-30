package com.titxu.cloud.sys.application;

import com.titxu.cloud.common.mybatis.util.Page;
import com.titxu.cloud.sys.application.command.PageCommand;

import java.util.Map;

/**
 * 系统日志查询服务接口
 **/
public interface LogQueryService {

    /**
     * 分页查询
     * @param params 查询参数
     * @return 分页数据
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 分页查询
     * @param command 查询参数
     * @return 分页数据
     */
    Page queryPage(PageCommand command);


}
