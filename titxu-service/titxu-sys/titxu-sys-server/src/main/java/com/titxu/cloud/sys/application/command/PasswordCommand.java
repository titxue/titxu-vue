package com.titxu.cloud.sys.application.command;

import lombok.Data;

/**
 * 密码Command
 **/
@Data
public class PasswordCommand {

    /**
     * 原密码
     */
    private String password;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 用户ID
     */
    private String userId;

}
