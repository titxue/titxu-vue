package com.titxu.cloud.sys.domain.model.captcha;

/**
 * 验证码Repository
 **/
public interface CaptchaRepository {

    /**
     * 获取编码
     *
     * @param uuid
     * @return
     */
    Captcha find(Uuid uuid);

    /**
     * 保存
     *
     * @param captcha
     */
    void store(Captcha captcha);

    /**
     * 删除
     *
     * @param uuid
     */
    void remove(Uuid uuid);
}
