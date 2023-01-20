package com.titxu.cloud.sys.domain.model.log;

import com.titxu.cloud.common.core.domain.Entity;
import com.titxu.cloud.sys.domain.model.tenant.TenantId;
import com.titxu.cloud.sys.domain.model.user.Mobile;
import com.titxu.cloud.sys.domain.model.user.UserNick;

/**
 * 日志实体
 **/
public class Log implements Entity<Log> {

    /**
     * logId
     */
    private LogId logId;

    /**
     * 用户名
     */
    private UserNick userNick;

    /**
     * 手机号
     */
    private Mobile mobile;

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
    private TenantId tenantId;

    public Log(LogId logId, UserNick userNick, String operation, String method, String params, Long time, String ip) {
        this.logId = logId;
        this.userNick = userNick;
        this.operation = operation;
        this.method = method;
        this.params = params;
        this.time = time;
        this.ip = ip;
    }

    public Log(LogId logId, UserNick userNick, Mobile mobile, String operation, String method, String params, Long time, String ip) {
        this.logId = logId;
        this.userNick = userNick;
        this.mobile = mobile;
        this.operation = operation;
        this.method = method;
        this.params = params;
        this.time = time;
        this.ip = ip;
    }

    @Override
    public boolean sameIdentityAs(Log other) {
        return other != null && logId.sameValueAs(other.logId);

    }

    public LogId getLogId() {
        return logId;
    }

    public String getOperation() {
        return operation;
    }

    public String getMethod() {
        return method;
    }

    public String getParams() {
        return params;
    }

    public Long getTime() {
        return time;
    }

    public String getIp() {
        return ip;
    }

    public UserNick getUserNick() {
        return userNick;
    }

    public Mobile getMobile() {
        return mobile;
    }

    public TenantId getTenantId() {
        return tenantId;
    }
}
