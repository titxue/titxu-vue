package com.titxu.cloud.sys.domain.model.user;

import com.titxu.cloud.common.core.domain.Entity;
import com.titxu.cloud.common.core.domain.StatusEnum;
import com.titxu.cloud.sys.domain.model.role.RoleId;
import com.titxu.cloud.sys.domain.model.tenant.TenantId;

import java.util.List;

/**
 * 用户
 **/
public class User implements Entity<User> {

    /**
     * UserId
     */
    private UserId userId;


    /**
     * 用户名
     */
    private UserName userName;

    /**
     * 状态
     */
    private StatusEnum status;

    /**
     * 账号
     */
    private Account account;

    /**
     * 当前租户
     */
    private TenantId tenantId;

    /**
     * 角色Id列表
     */
    private List<RoleId> roleIds;


    public User(UserId userId, UserName userName, StatusEnum status, Account account, TenantId tenantId, List<RoleId> roleIds) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.account = account;
        this.tenantId = tenantId;
        this.roleIds = roleIds;
    }

    public User(UserName userName, Account account, List<RoleId> roleIds) {
        this.userName = userName;
        this.account = account;
        this.roleIds = roleIds;
        this.status = StatusEnum.ENABLE;
    }

    /**
     * 是否有效
     */
    public boolean isEnable() {
        return status == StatusEnum.ENABLE;
    }

    @Override
    public boolean sameIdentityAs(User other) {
        return other != null && userId.sameValueAs(other.userId);
    }

    /**
     * 禁用
     */
    public void disable() {
        this.status = this.status == StatusEnum.DISABLE ? StatusEnum.ENABLE : StatusEnum.DISABLE;
    }

    public void changePassword(String oldPasswordStr, String newPasswordStr) {
        account.changePassword(oldPasswordStr, newPasswordStr);
    }

    public UserId getUserId() {
        return userId;
    }

    public UserName getUserName() {
        return userName;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public List<RoleId> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<RoleId> roleIds) {
        this.roleIds = roleIds;
    }
}