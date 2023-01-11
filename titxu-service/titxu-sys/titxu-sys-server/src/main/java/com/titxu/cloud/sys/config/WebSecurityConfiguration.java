package com.titxu.cloud.sys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全配置
 **/
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
    };


    /**
     * 暴露静态资源
     * <p>
     * https://github.com/spring-projects/spring-security/issues/10938
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        // 放行SWAGGER_WHITELIST 配置的资源 不需要认证
        http.securityMatchers((matchers) -> matchers.requestMatchers(URL_WHITELIST))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll()).requestCache().disable()
                .securityContext().disable()
                .cors().disable();
        return http.build();
    }
}

