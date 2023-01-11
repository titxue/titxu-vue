package com.titxu.cloud.sys.feign;

import com.titxu.cloud.sys.dto.AuthenticationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(contextId = "remoteAuthenticationService", value = "titxu-server-sys")
public interface RemoteAuthenticationService {
    /**
     * 验证验证码
     *
     * @param uuid uuid
     * @return success / fail
     */
    @PostMapping("validateCaptcha")
    boolean validateCaptcha(@RequestParam("uuid") String uuid, @RequestParam("captchaCode") String captchaCode);

    /**
     * 认证
     *
     * @param userName 用户名
     * @return 认证信息
     */
    @PostMapping("loginByUserName")
    AuthenticationDTO loginByUserName(@RequestParam("userName") String userName);
}
