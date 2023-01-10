package com.titxu.cloud.sys.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.titxu.cloud.common.mybatis.util.BaseDO;
import lombok.Data;

/**
 * 角色DO
 **/
@Data
@TableName("sys_role")
public class SysRoleDO extends BaseDO {

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remarks;
}
