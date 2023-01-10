package com.titxu.cloud.sys.domain.model.user;

import com.titxu.cloud.common.core.domain.Entity;

/**
 * 账号
 **/
public class Account implements Entity<Account> {

    /**
     * accountId
     */
    private AccountId accountId;

    /**
     * 手机号
     */
    private Mobile mobile;

    /**
     * 邮箱
     */
    private Email email;

    /**
     * 密码
     */
    private Password password;


    public Account(AccountId accountId, Mobile mobile, Email email, Password password) {
        this.accountId = accountId;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
    }

    public Account(Mobile mobile, String password) {
        this.mobile = mobile;
        this.password = Password.create(password);
    }

    public Account(Mobile mobile, Email email) {
        this.mobile = mobile;
        this.email = email;
    }

    public Account(Mobile mobile, Email email, Password password) {
        this.mobile = mobile;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean sameIdentityAs(Account other) {
        return other != null && accountId.sameValueAs(other.accountId);
    }

    /**
     * 密码是否正确
     *
     * @param passwordStr 原始密码
     */
    public boolean checkPassword(String passwordStr) {
        return password == null || !this.password.sameValueAs(Password.create(passwordStr));
    }

    /**
     * 修改密码
     *
     * @param oldPasswordStr 旧
     * @param newPasswordStr 新
     */
    public void changePassword(String oldPasswordStr, String newPasswordStr) {
        if (checkPassword(oldPasswordStr)) {
            throw new RuntimeException("原密码不正确");
        }
        this.password = Password.create(newPasswordStr);
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public Mobile getMobile() {
        return mobile;
    }

    public void setMobile(Mobile mobile) {
        this.mobile = mobile;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Password getPassword() {
        return password;
    }
}
