package com.titxu.cloud.sys.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.titxu.cloud.common.mybatis.util.BaseDO;
import lombok.Data;

/**
 * 角色权限关联DO
 **/
@Data
@TableName("sys_role_permission")
public class SysRolePermissionDO extends BaseDO {

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 权限ID
     */
    private String permissionId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 备注
     */
    private String remarks;
}
