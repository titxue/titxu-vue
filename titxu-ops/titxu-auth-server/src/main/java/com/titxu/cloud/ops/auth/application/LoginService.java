package com.titxu.cloud.ops.auth.application;


import com.titxu.cloud.ops.auth.application.command.LoginPasswordCommand;
import com.titxu.cloud.ops.auth.application.command.RefreshCommand;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import com.titxu.cloud.common.web.util.Result;

import java.util.Map;

/**
 * 登录服务接口
 */
public interface LoginService {

    /**
     * 密码登陆
     * @param loginPasswordCommand 登陆参数
     */
    Map<String, Object> login(LoginPasswordCommand loginPasswordCommand);

    /**
     * 刷新登陆
     * @param refreshCommand 刷新token
     * @return token
     */
    Map<String, Object> refresh(RefreshCommand refreshCommand);
}
