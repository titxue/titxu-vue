package com.titxu.cloud.management.auth.infrastructure.support.core;

import com.titxu.cloud.management.auth.infrastructure.support.handler.FormAuthenticationFailureHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * 基于授权码模式 统一认证登录 spring security & sas 都可以使用 所以抽取成 HttpConfigurer
 */
public final class FormIdentityLoginConfigurer
        extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.formLogin(formLogin -> formLogin
                // 登录页面地址
                .loginPage("/token/login")
                // 登录请求地址
                .loginProcessingUrl("/token/form")
                // 身份验证失败处理程序
                .failureHandler(new FormAuthenticationFailureHandler()))
                // 登出成功处理
                .logout().and().csrf().disable();
    }

}
