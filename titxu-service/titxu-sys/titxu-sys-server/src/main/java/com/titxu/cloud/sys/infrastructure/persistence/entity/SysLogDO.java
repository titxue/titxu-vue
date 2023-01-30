package com.titxu.cloud.sys.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.titxu.cloud.common.mybatis.util.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 日志DO
 **/

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_log")
public class SysLogDO extends BaseDO {

    /**
     * 用户昵称
     */
    @TableField("user_name")
    private String userNick;

    /**
     * 用户名
     */
    private String mobile;

    /**
     * 用户操作
     */
    private String operation;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 执行时长(毫秒)
     */
    private Long time;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 租户ID
     */
    private String tenantId;
}
