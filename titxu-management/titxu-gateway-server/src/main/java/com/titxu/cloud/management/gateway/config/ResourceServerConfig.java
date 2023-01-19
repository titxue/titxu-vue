package com.titxu.cloud.management.gateway.config;

import cn.hutool.core.util.ArrayUtil;
import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.core.util.PublicKeyUtils;
import com.titxu.cloud.common.web.constant.ResultCode;
import com.titxu.cloud.management.gateway.security.AuthorizationManager;
import com.titxu.cloud.management.gateway.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

import java.security.interfaces.RSAPublicKey;

/**
 * 资源服务器配置
 **/
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {

    private WhiteListConfig whiteListConfig;

    private AuthorizationManager authorizationManager;

    @Autowired
    public void setWhiteListConfig(WhiteListConfig whiteListConfig) {
        this.whiteListConfig = whiteListConfig;
    }

    @Autowired
    public void setAuthorizationManager(AuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        // 临时禁用 跨站请求伪造 CSRF
        // 待转化为配置文件
        http.csrf().disable();

        http.oauth2ResourceServer(oAuth2ResourceServerSpec -> {
            // 资源服务配置秘钥
            // 启用 OAuth2 JWT 资源服务器支持
            RSAPublicKey rsaPublicKey = PublicKeyUtils.loadPublicKey();
            oAuth2ResourceServerSpec.jwt().publicKey(rsaPublicKey);


            // 资源服务异常切入点（验证Token异常）
            oAuth2ResourceServerSpec.authenticationEntryPoint(authenticationEntryPoint());
        });

        http.authorizeExchange()
                .pathMatchers(ArrayUtil.toArray(whiteListConfig.getUrls(), String.class)).permitAll()
                .anyExchange().access(authorizationManager);

        // 身份验证入口点
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());

        // 服务器访问被拒绝处理程序
        http.oauth2ResourceServer().accessDeniedHandler(accessDeniedHandler());

        return http.build();
    }

    /**
     * 未授权
     *
     * @return
     */
    @Bean
    ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> {
            Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> WebUtils.getAuthFailResult(response, ResultCode.UNAUTHORIZED.getCode()));
            return mono;
        };
    }

    /**
     * token无效或者已过期自定义响应
     */
    @Bean
    ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (exchange, e) -> Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> WebUtils.getAuthFailResult(response, ResultCode.UNAUTHORIZED.getCode()));
    }

    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstants.AUTHORITY_PREFIX);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstants.JWT_AUTHORITIES_KEY);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
