package com.titxu.cloud.sys.application;

import com.titxu.cloud.sys.application.dto.UserDTO;
import com.titxu.cloud.common.mybatis.util.Page;

import java.util.Map;

/**
 * 用户查询服务接口
 **/
public interface UserQueryService {

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 通过用户ID获取用户
     *
     * @param userId
     * @return
     */
    UserDTO find(String userId);
}
