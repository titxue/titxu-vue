package com.titxu.cloud.common.core.constant;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 返回数据定义
 */
@AllArgsConstructor
@NoArgsConstructor
public enum ResultCode {

    /**
     * success
     */
    SUCCESS(0, "success"),

    /**
     * 系统异常
     */
    SERVER_ERROR(500, "系统异常"),

    /**
     * 禁止删除系统管理员
     */
    DELETE_SYSTEM_ERROR(500, "禁止删除系统管理员"),

    /**
     * 认证失败
     */
    UNAUTHORIZED(401, "认证失败");

    private int code;

    private String msg;

    public static ResultCode getValue(int code) {
        for (ResultCode value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":\"" + code + '\"' +
                ", \"msg\":\"" + msg + '\"' +
                '}';
    }
}
