package com.titxu.cloud.sys.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 认证信息DTO
 *


 **/
@Data
public class AuthenticationDTO implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * password
     */
    private String password;

    /**
     * status
     */
    private String status;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 权限编码
     */
    private Set<String> permissionCodes;

}
