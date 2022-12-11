package com.titxu.core.sys.application;

/**
 * 验证码应用服务
 **/
public interface CodeApplicationService {

    /**
     * 发送验证码
     */
    void sendCode(String mobile);
}
