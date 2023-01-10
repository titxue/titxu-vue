package com.titxu.cloud.sys.infrastructure.persistence.converter;

import com.titxu.cloud.sys.domain.model.captcha.Captcha;
import com.titxu.cloud.sys.domain.model.captcha.CaptchaCode;
import com.titxu.cloud.sys.domain.model.captcha.Uuid;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysCaptchaDO;

/**
 * 验证码转换类
 **/
public class CaptchaConverter {

    public static SysCaptchaDO fromCaptcha(Captcha captcha) {
        SysCaptchaDO sysCaptchaDO = new SysCaptchaDO();
        sysCaptchaDO.setUuid(captcha.getUuid().getId());
        sysCaptchaDO.setCode(captcha.getCaptchaCode().getCode());
        sysCaptchaDO.setExpireTime(captcha.getExpireTime());
        return sysCaptchaDO;
    }

    public static Captcha toCaptcha(SysCaptchaDO sysCaptchaDO) {
        if (sysCaptchaDO == null) {
            return null;
        }
        Captcha captcha = new Captcha(new Uuid(sysCaptchaDO.getUuid()), new CaptchaCode(sysCaptchaDO.getCode()),
                sysCaptchaDO.getExpireTime());
        return captcha;
    }
}
