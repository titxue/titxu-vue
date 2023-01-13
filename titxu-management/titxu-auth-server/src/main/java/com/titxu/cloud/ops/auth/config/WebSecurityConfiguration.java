package com.titxu.cloud.ops.auth.config;

import com.titxu.cloud.ops.auth.support.core.DaoAuthenticationProvider;
import com.titxu.cloud.ops.auth.support.core.FormIdentityLoginConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static com.titxu.cloud.common.security.service.CsrfRequestMatcherImpl.CSRF_REQUEST_MATCHER_BEAN_NAME;

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
            "/*/*.css",
            "/*/*.js",
            "/error",
            "/favicon.ico",
    };
    private AccessDeniedHandler accessDeniedHandler;
    private RequestMatcher requestMatcher;

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
     *
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.ignoring().requestMatchers(URL_WHITELIST);
        };
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

        // 异常处理
        http.exceptionHandling(exceptionHandlingCustomizer -> {
            exceptionHandlingCustomizer
                    // 访问被拒绝处理程序
                    .accessDeniedHandler(accessDeniedHandler)
                    // 身份验证入口点
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(401);
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write("未登录或登录已过期");
                    });
        });

        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers("/oauth2/**").permitAll()

                        .requestMatchers("/token/*").permitAll()
                        // 放行端点
                        .requestMatchers("/actuator/**").permitAll()
                        // 放行获取PublicKey请求
                        .requestMatchers("/getPublicKey").permitAll()
                        .requestMatchers("/auth/**").permitAll()

                        // 其他请求需要认证
                        .anyRequest().authenticated())
                .headers()
                // 避免iframe同源无法登录
                .frameOptions().sameOrigin()
                // 表单登录个性化
                .and().apply(new FormIdentityLoginConfigurer());

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new DaoAuthenticationProvider());

        // CSRF 配置
        http.csrf().requireCsrfProtectionMatcher(requestMatcher);

        return http.build();
    }

    /**
     * 暴露静态资源
     * <p>
     * <a href="https://github.com/spring-projects/spring-security/issues/10938">...</a>
     *
     * @param http security注入点
     * @return SecurityFilterChain
     * @throws Exception 异常
     */
//    @Bean
//    @Order(0)
//    SecurityFilterChain resources(HttpSecurity http) throws Exception {
//        // 放行SWAGGER_WHITELIST 配置的资源 不需要认证
//        http.securityMatchers((matchers) -> matchers.requestMatchers(URL_WHITELIST))
//                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll()).requestCache().disable()
//                .securityContext().disable().sessionManagement().disable();
//        return http.build();
//    }


}

