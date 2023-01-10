package com.titxu.cloud.sys.application;

import java.awt.image.BufferedImage;

/**
 * 验证码应用服务
 **/
public interface CaptchaApplicationService {

    /**
     * 生成ø验证码
     *
     * @param uuid
     * @return
     */
    BufferedImage getCaptcha(String uuid);
}
