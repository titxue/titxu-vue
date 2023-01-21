package com.titxu.cloud.sys.api.service;


import com.titxu.cloud.sys.api.dto.AuthenticationDTO;

/**
 * 认证服务接口
 **/
public interface AuthenticationService {

    /**
     * 验证验证码
     * @param uuid uuid
     * @return 成功返回true，失败返回false
     */
    boolean validateCaptcha(String uuid, String captchaCode);

    /**
     * 认证
     * @param userName 用户名
     * @return 认证信息 一般为手机号
     */
    AuthenticationDTO loginByUserName(String userName);
}
