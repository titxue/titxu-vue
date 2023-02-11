package com.titxu.cloud.management.auth.config;


import com.titxu.cloud.common.core.util.PublicKeyUtils;
import com.titxu.cloud.management.auth.infrastructure.support.core.DaoAuthenticationProvider;
import com.titxu.cloud.management.auth.infrastructure.support.core.FormIdentityLoginConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.interfaces.RSAPublicKey;

import static com.titxu.cloud.common.security.service.CsrfRequestMatcherImpl.CSRF_REQUEST_MATCHER_BEAN_NAME;

/**
 * 安全配置
 **/
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    /**
     * 放行Swagger
     */
    public static final String[] URL_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/swagger-config",
            "/webjars/**",
            "/doc.html",
            "/*/*.css",
            "/*/*.js",
            "/error",
            "/favicon.ico",
            "/account/**",
            "/getPublicKey"
    };
    private AccessDeniedHandler accessDeniedHandler;
    private RequestMatcher requestMatcher;
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Autowired
    public void setAccessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Autowired
    @Qualifier(CSRF_REQUEST_MATCHER_BEAN_NAME)
    public void setRequestMatcher(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    /**
     * 暴露静态资源
     * 在 WebSecurity 暴露
     *
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(URL_WHITELIST);
    }

    /**
     * spring security 默认的安全策略
     *
     * @param http security注入点
     * @return SecurityFilterChain
     * @throws Exception 异常
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {


        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers("/account/**").permitAll()
                        // 授权请求
                        .requestMatchers("/token/**").permitAll()
                        // 放行端点
                        .requestMatchers("/actuator/**").permitAll()
                        // 放行获取PublicKey请求
                        .requestMatchers("/getPublicKey").permitAll()

                        // 其他请求需要认证
                        .anyRequest().authenticated())
                .headers().cacheControl().disable()
                // 避免iframe同源无法登录
                .frameOptions().sameOrigin()
                // 表单登录个性化
                .and().apply(new FormIdentityLoginConfigurer());

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new DaoAuthenticationProvider());

        // 资源服务配置秘钥
        http.oauth2ResourceServer().jwt(oauth2ResourceServer -> {
            RSAPublicKey rsaPublicKey = PublicKeyUtils.loadPublicKey();
            NimbusJwtDecoder.PublicKeyJwtDecoderBuilder publicKeyJwtDecoderBuilder = NimbusJwtDecoder
                    .withPublicKey(rsaPublicKey);
            NimbusJwtDecoder nimbusJwtDecoder = publicKeyJwtDecoderBuilder.build();
            oauth2ResourceServer.decoder(nimbusJwtDecoder);
        });

        // 异常处理
        http.exceptionHandling(exceptionHandlingCustomizer -> exceptionHandlingCustomizer
                // 访问被拒绝处理程序
                .accessDeniedHandler(accessDeniedHandler)
                // 身份验证入口点
                .authenticationEntryPoint(authenticationEntryPoint));

        // csrf
        http.csrf().disable();
        return http.build();
    }


}

