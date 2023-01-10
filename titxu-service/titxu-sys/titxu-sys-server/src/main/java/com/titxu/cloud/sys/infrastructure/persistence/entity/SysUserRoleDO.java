package com.titxu.cloud.sys.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.titxu.cloud.common.mybatis.util.BaseDO;
import lombok.Data;

/**
 * 用户角色关联DO
 **/
@Data
@TableName("sys_user_role")
public class SysUserRoleDO extends BaseDO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 备注
     */
    private String remarks;
}
