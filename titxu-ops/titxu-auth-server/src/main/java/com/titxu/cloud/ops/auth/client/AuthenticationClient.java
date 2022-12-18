package com.titxu.cloud.ops.auth.client;

import com.titxu.cloud.sys.dto.AuthenticationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "titxu-server-sys")
public interface AuthenticationClient {
    /**
     * 验证验证码
     *
     * @param uuid
     * @return
     */
    @PostMapping("validateCaptcha")
    boolean validateCaptcha(@RequestParam String uuid, @RequestParam String captchaCode);

    /**
     * 认证
     *
     * @param userName 用户名
     * @return
     */
    @PostMapping("loginByUserName")
    AuthenticationDTO loginByUserName(@RequestParam String userName);
}
