package com.titxu.cloud.sys.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 租户DTO
 **/
@Data
public class TenantDTO implements Serializable {

    /**
     * 租户id
     */
    private String id;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 状态
     */
    private String status;



}
