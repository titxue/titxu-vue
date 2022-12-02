package com.titxu.cloud.sys.application;

import java.awt.image.BufferedImage;

/**
 * 验证码应用服务
 **/
public interface CodeApplicationService {

    /**
     * 发送验证码
     */
    void sendCode(String mobile);
}
