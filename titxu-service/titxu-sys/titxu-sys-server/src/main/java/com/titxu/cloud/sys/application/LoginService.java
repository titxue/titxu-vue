package com.titxu.cloud.sys.application;

import com.titxu.cloud.sys.application.command.LoginPasswordCommand;

/**
 * 登录服务接口
 */
public interface LoginService {

    /**
     * 密码登陆
     *
     * @param loginPasswordCommand
     */
    void login(LoginPasswordCommand loginPasswordCommand);

}
