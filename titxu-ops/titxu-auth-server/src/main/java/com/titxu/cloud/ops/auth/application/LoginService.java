package com.titxu.cloud.ops.auth.application;


import com.titxu.cloud.ops.auth.application.command.LoginPasswordCommand;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import com.titxu.cloud.common.web.util.Result;

import java.util.Map;

/**
 * 登录服务接口
 */
public interface LoginService {

    /**
     * 密码登陆
     *
     * @param loginPasswordCommand
     */
    Map<String, Object> login(LoginPasswordCommand loginPasswordCommand);

}
