package com.titxu.cloud.sys.application;

import com.titxu.cloud.sys.application.dto.RoleDTO;
import com.titxu.cloud.common.mybatis.util.Page;

import java.util.List;
import java.util.Map;

/**
 * 角色查询服务接口
 **/
public interface RoleQueryService {

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 查询列表
     *
     * @return
     */
    List<RoleDTO> listAll();

    /**
     * 通过ID获取
     *
     * @param id
     * @return
     */
    RoleDTO getById(String id);
}
