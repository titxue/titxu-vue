package com.titxu.cloud.management.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.titxu.cloud.management.auth.support.core.CustomeOAuth2TokenCustomizer;
import com.titxu.cloud.management.auth.support.core.DaoAuthenticationProvider;
import com.titxu.cloud.management.auth.support.core.FormIdentityLoginConfigurer;
import com.titxu.cloud.management.auth.support.generator.CustomeOAuth2AccessTokenGenerator;
import com.titxu.cloud.management.auth.support.handler.AuthenticationFailureEventHandler;
import com.titxu.cloud.management.auth.support.handler.AuthenticationSuccessEventHandler;
import com.titxu.cloud.management.auth.support.handler.ClientAuthenticationFailureEventHandler;
import com.titxu.cloud.management.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.titxu.cloud.management.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.Arrays;

import static com.titxu.cloud.management.auth.jose.Jwks.generateLoadRsa;

/**
 * 授权服务配置
 *
 * @see org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
 * @see org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
 **/
@Configuration
public class AuthorizationServerConfig {


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http.apply(authorizationServerConfigurer.tokenEndpoint((tokenEndpoint) -> {// 个性化认证授权端点
                    tokenEndpoint
                            .accessTokenRequestConverter(accessTokenRequestConverter()) // 注入自定义的授权认证Converter
                            .accessTokenResponseHandler(new AuthenticationSuccessEventHandler()) // 登录成功处理器
                            .errorResponseHandler(new AuthenticationFailureEventHandler());// 登录失败处理器
                }).clientAuthentication(oAuth2ClientAuthenticationConfigurer -> // 个性化客户端认证
                        oAuth2ClientAuthenticationConfigurer
                                .errorResponseHandler(new ClientAuthenticationFailureEventHandler()))// 处理客户端认证异常
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                        .consentPage("/token/confirm_access")));

        DefaultSecurityFilterChain securityFilterChain = http.authorizeHttpRequests(authorizeRequests -> {
                    // 自定义接口、端点暴露
                    authorizeRequests.requestMatchers("/token/**", "/actuator/**", "/css/**", "/error").permitAll();
                    authorizeRequests.anyRequest().authenticated();
                })
                // 授权码登录的登录页个性化
                .apply(new FormIdentityLoginConfigurer()).and().build();

        // 注入自定义授权模式实现
        addCustomOAuth2GrantAuthenticationProvider(http);
        return securityFilterChain;
    }

    /**
     * 自定义的授权认证Converter
     *
     * @return DelegatingAuthenticationConverter
     */
    private AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(Arrays.asList(
                // 自定义密码模式认证转换器
                new OAuth2ResourceOwnerPasswordAuthenticationConverter(),
                // 默认刷新令牌认证转换器
                new OAuth2RefreshTokenAuthenticationConverter(),
                // 默认客户端凭据身份验证转换器
                new OAuth2ClientCredentialsAuthenticationConverter(),
                // 默认授权码身份验证转换器 -> 用于授权码模式获取token
                new OAuth2AuthorizationCodeAuthenticationConverter(),
                // 默认授权请求(或同意) 授权码授权转换器 -> 用于授权码模式获取code
                new OAuth2AuthorizationCodeRequestAuthenticationConverter()));
    }

    /**
     * 令牌生成规则实现 </br>
     * client:username:uuid
     *
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator oAuth2TokenGenerator() {
        CustomeOAuth2AccessTokenGenerator accessTokenGenerator = new CustomeOAuth2AccessTokenGenerator();
        // 注入Token 增加关联用户信息
        accessTokenGenerator.setAccessTokenCustomizer(new CustomeOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }

    /**
     * 注入授权模式实现提供方
     * <p>
     * 1. 密码模式 </br>
     * 2. 短信登录 </br>
     */
    @SuppressWarnings("unchecked")
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator());

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new DaoAuthenticationProvider());
        // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        // http://127.0.0.1:8000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=message.read&redirect_uri=https://www.titxu.com
//        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("messaging-client")
//                .clientSecret("{noop}secret")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
//                .redirectUri("http://127.0.0.1:8080/oauth2/authorized")
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)
//                .scope("message.read")
//                .scope("message.write")
//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//                .build();

        // Save registered client in db as if in-memory
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
//        registeredClientRepository.save(registeredClient);

        return registeredClientRepository;
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }
    /**
     * 配置客户端详情(数据库)
     */
//    @Override
//    @SneakyThrows
//    public void configure(ClientDetailsServiceConfigurer clients) {
//        JdbcClientDetailsServiceImpl jdbcClientDetailsService = new JdbcClientDetailsServiceImpl(dataSource);
//        jdbcClientDetailsService.setFindClientDetailsSql(AuthConstants.FIND_CLIENT_DETAILS_SQL);
//        jdbcClientDetailsService.setSelectClientDetailsSql(AuthConstants.SELECT_CLIENT_DETAILS_SQL);
//        clients.withClientDetails(jdbcClientDetailsService);
//    }


    /**
     * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
//        tokenEnhancers.add(tokenEnhancer());
//        tokenEnhancers.add(jwtAccessTokenConverter());
//        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
//        endpoints
//                .authenticationManager(authenticationManager)
//                .accessTokenConverter(jwtAccessTokenConverter())
//                .tokenEnhancer(tokenEnhancerChain)
//                .userDetailsService(userDetailsService)
//                // refresh token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
//                //      1 重复使用：access token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
//                //      2 非重复使用：access token过期刷新时， refresh token过期时间延续，在refresh token有效期内刷新便永不失效达到无需再次登录的目的
//                .reuseRefreshTokens(true);
//    }


    /**
     * 使用非对称加密算法对token签名
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {
        RSAKey rsaKey = generateLoadRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }


    //    /**
//     * JWT内容增强
//     */
//    @Bean
//    public TokenEnhancer tokenEnhancer() {
//        return (accessToken, authentication) -> {
//            // todo details 信息
//            // LinkedHashMap details = (LinkedHashMap) authentication.getUserAuthentication().getDetails();
//            Map<String, Object> map = new HashMap<>(8);
//            User user = (User) authentication.getUserAuthentication().getPrincipal();
//            map.put(AuthConstants.USER_ID_KEY, user.getId());
//            map.put(AuthConstants.USER_NAME_KEY, user.getId());
//            map.put(AuthConstants.TENANT_ID_KEY, user.getTenantId());
//            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
//            return accessToken;
//        };
//    }
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}
