package com.titxu.cloud.sys.api.feign;

import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.sys.api.dto.AuthenticationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "remoteAuthenticationService", value = "titxu-server-sys")
public interface RemoteAuthenticationService {
    /**
     * 验证验证码
     *
     * @param uuid uuid
     * @return success / fail
     */
    @PostMapping(value = "/remote/auth/validateCaptcha", headers = AuthConstants.HEADER_FROM_IN)
    boolean validateCaptcha(@RequestParam("uuid") String uuid, @RequestParam("captchaCode") String captchaCode);

    /**
     * 认证
     *
     * @param userName 用户名
     * @return 认证信息
     */
    @PostMapping(value = "/remote/auth/loginByUserName", headers = AuthConstants.HEADER_FROM_IN)
    AuthenticationDTO loginByUserName(@RequestParam("userName") String userName);
}
