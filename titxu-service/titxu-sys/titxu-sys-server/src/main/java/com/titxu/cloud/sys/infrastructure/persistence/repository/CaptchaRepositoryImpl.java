package com.titxu.cloud.sys.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.titxu.cloud.sys.domain.model.captcha.Captcha;
import com.titxu.cloud.sys.domain.model.captcha.CaptchaRepository;
import com.titxu.cloud.sys.domain.model.captcha.Uuid;
import com.titxu.cloud.sys.infrastructure.persistence.converter.CaptchaConverter;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysCaptchaDO;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysCaptchaMapper;
import org.springframework.stereotype.Repository;

/**
 * 验证码-Repository实现类
 **/
@Repository
public class CaptchaRepositoryImpl extends ServiceImpl<SysCaptchaMapper, SysCaptchaDO> implements CaptchaRepository, IService<SysCaptchaDO> {

    @Override
    public Captcha find(Uuid uuid) {
        SysCaptchaDO sysCaptchaDO = this.getById(uuid.getId());
        return CaptchaConverter.toCaptcha(sysCaptchaDO);
    }

    @Override
    public void store(Captcha captcha) {
        this.save(CaptchaConverter.fromCaptcha(captcha));
    }

    @Override
    public void remove(Uuid uuid) {
        this.removeById(uuid.getId());
    }
}
