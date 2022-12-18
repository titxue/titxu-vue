package com.titxu.cloud.sys.application.impl;

import com.google.code.kaptcha.Producer;
import com.titxu.cloud.sys.domain.model.captcha.Captcha;
import com.titxu.cloud.sys.domain.model.captcha.CaptchaCode;
import com.titxu.cloud.sys.domain.model.captcha.CaptchaRepository;
import com.titxu.cloud.sys.domain.model.captcha.Uuid;
import com.titxu.cloud.sys.application.CaptchaApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;

/**
 * 验证码应用服务实现类
 *


 **/
@Service
public class CaptchaApplicationServiceImpl implements CaptchaApplicationService {

    @Autowired
    private Producer producer;

    @Autowired
    private CaptchaRepository captchaRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BufferedImage getCaptcha(String uuid) {
        //生成文字验证码
        String code = producer.createText();
        captchaRepository.store(Captcha.createCaptcha(new Uuid(uuid), new CaptchaCode(code)));
        return producer.createImage(code);
    }
}
