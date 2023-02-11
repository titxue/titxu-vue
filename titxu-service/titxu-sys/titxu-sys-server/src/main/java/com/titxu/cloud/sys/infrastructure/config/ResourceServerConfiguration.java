package com.titxu.cloud.sys.infrastructure.config;

import com.titxu.cloud.common.core.util.PublicKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.security.interfaces.RSAPublicKey;


/**
 * 资源服务配置
 *
 * @author xuxiaowei
 * @see <a href="http://127.0.0.1:1701/sns/userinfo?access_token=">访问资源</a>
 * @see <a href=
 * "https://docs.spring.io/spring-security/reference/6.0.0-M3/servlet/oauth2/resource-server/jwt.html#oauth2resourceserver-jwt-authorization-extraction">手动提取权限-6.0.0-M3</a>
 * @since 0.0.1
 */
@Configuration
public class ResourceServerConfiguration {

    private AccessDeniedHandler accessDeniedHandler;
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public void setAccessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Autowired
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/favicon.ico");
    }

    /**
     * @see <a href=
     * "https://docs.spring.io/spring-security/reference/servlet/authentication/index.html">用于身份验证</a>
     * 的 Spring Security 过滤器链。
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        // 路径权限控制
        http.authorizeHttpRequests((authorize) -> authorize
                // 放行端点
                .requestMatchers("/actuator/**").permitAll()
                // 放行错误地址
                .requestMatchers("/error").permitAll()
                // 放行登陆微服务接口
                .requestMatchers("/remote/**").permitAll()
                // 其他路径均需要授权
                .anyRequest().authenticated());

        // 禁用 form 登录
        http.formLogin().disable();

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

        // CSRF 配置
        http.csrf().disable();

        return http.build();
    }

}
