package com.titxu.cloud.sys.infrastructure.api;

import com.titxu.cloud.common.security.annotation.Inner;
import com.titxu.cloud.sys.domain.model.captcha.Captcha;
import com.titxu.cloud.sys.domain.model.captcha.CaptchaRepository;
import com.titxu.cloud.sys.domain.model.captcha.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/remote/captcha")
public class CaptchaApi {
    private CaptchaRepository captchaRepository;

    @Autowired
    public void setCaptchaRepository(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    /**
     * 获取编码
     *
     * @param uuid uuid
     * @return 编码
     */
    @Inner
    @GetMapping("find")
    public Captcha find(@RequestParam Uuid uuid) {
        return captchaRepository.find(uuid);
    }

    /**
     * 保存
     *
     * @param captcha 验证码
     */
    @Inner
    @PostMapping("store")
    public void store(@RequestBody Captcha captcha) {
        captchaRepository.store(captcha);
    }

    /**
     * 删除
     *
     * @param uuid uuid
     */
    @Inner
    @GetMapping("remove")
    public void remove(@RequestParam Uuid uuid) {
        captchaRepository.remove(uuid);
    }
}
