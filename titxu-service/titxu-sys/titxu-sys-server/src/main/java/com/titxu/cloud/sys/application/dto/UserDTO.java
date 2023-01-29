package com.titxu.cloud.sys.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 用户DTO
 **/
@Data
public class UserDTO implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 用户名
     */
    private String userNick;

    /**
     * email
     */
    private String email;

    /**
     * mobile
     */
    private String mobile;

    /**
     * status
     */
    private String status;

    /**
     * 当前租户
     */
    private String tenantName;

    /**
     * 角色列表
     */
    private List<String> roleIdList;

    /**
     * 权限编码
     */
    private Set<String> permissionCodes;

    /**
     * 权限id
     */
    private Set<String> permissionIds;

    /**
     * 所有租户列表
     */
    private List<TenantDTO> tenants;

    /**
     * 签名信息
     */
    private String remarks;

    public UserDTO() {
    }

    public UserDTO(String id, String userNick, String email, String mobile, String status, List<String> roleIdList,String remarks) {
        this.id = id;
        this.userNick = userNick;
        this.email = email;
        this.mobile = mobile;
        this.status = status;
        this.roleIdList = roleIdList;
        this.remarks = remarks;
    }
}
