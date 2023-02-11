package com.titxu.cloud.management.auth.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.titxu.cloud.common.security.domain.AuthUser;
import com.titxu.cloud.common.security.domain.AuthUserMixin;
import com.titxu.cloud.management.auth.infrastructure.support.base.CustomeAuthorizationGrantType;
import com.titxu.cloud.management.auth.infrastructure.support.core.CustomeOAuth2TokenCustomizer;
import com.titxu.cloud.management.auth.infrastructure.support.core.DaoAuthenticationProvider;
import com.titxu.cloud.management.auth.infrastructure.support.core.FormIdentityLoginConfigurer;
import com.titxu.cloud.management.auth.infrastructure.support.core.JwtCustomeOAuth2TokenCustomizer;
import com.titxu.cloud.management.auth.infrastructure.support.generator.CustomeOAuth2AccessTokenGenerator;
import com.titxu.cloud.management.auth.infrastructure.support.handler.AuthenticationFailureEventHandler;
import com.titxu.cloud.management.auth.infrastructure.support.handler.AuthenticationSuccessEventHandler;
import com.titxu.cloud.management.auth.infrastructure.support.handler.ClientAuthenticationFailureEventHandler;
import com.titxu.cloud.management.auth.infrastructure.support.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.titxu.cloud.management.auth.infrastructure.support.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.titxu.cloud.common.core.util.jose.Jwks.generateLoadRsa;


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
                                .consentPage("/token/confirm_access"))
        );
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
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource()));
        // Base64 96位 注入Token 增加关联用户信息
        accessTokenGenerator.setAccessTokenCustomizer(new CustomeOAuth2TokenCustomizer());
        // JWT注入Token 增加关联用户信息
        jwtGenerator.setJwtCustomizer(new JwtCustomeOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, jwtGenerator, new OAuth2RefreshTokenGenerator());
    }

    /**
     * 注入授权模式实现提供方
     * <p>
     * 1. 密码模式 </br>
     * 2. 短信登录 </br>
     */
    @SuppressWarnings("unchecked")
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {

        /*
        AuthenticationManager 是用于安全认证的核心接口，它可以帮助应用程序提供安全认证服务，如身份认证和授权。
        它可以通过支持不同类型的认证模式，如基于凭据的认证和基于角色的认证来实现安全认证和授权。
         */
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        /*
        OAuth2AuthorizationService是一个专门用于授权的服务，它使用OAuth 2.0协议来向用户提供访问特定资源的权限。
        它包含一系列API，用于从资源所有者（例如用户）那里请求和管理访问令牌。
         */

        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        // 密码模式 -> 个性化实现
        OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator());

        // 处理 UsernamePasswordAuthenticationToken
        /*
          UsernamePasswordAuthenticationToken是Spring Security提供的一种安全认证方式，它将用户名和密码封装为一个令牌，以便在系统中进行认证。
         */
        http.authenticationProvider(new DaoAuthenticationProvider());
        // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
    }


    /**
     * 客户端信息存储
     * RegisteredClientRepository是一个存储已注册客户端(客户端证书)的仓库。
     * 它可以用来存储和检索客户端的信息，以便在客户端请求服务器资源时进行验证。
     *
     * @param jdbcTemplate 数据源
     * @return RegisteredClientRepository
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {

        /*
          重定向地址
          http://127.0.0.1:8000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=message.read&redirect_uri=http://127.0.0.1:8000/token/login
          http://127.0.0.1:8000/oauth2/authorize?response_type=code&client_id=admin&scope=server&redirect_uri=https://www.baidu.com
         */

        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("admin")
                .clientSecret("{noop}admin")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(CustomeAuthorizationGrantType.PASSWORD)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://127.0.0.1:8080/token/login")
                .redirectUri("https://www.baidu.com")
                .scope("server")
                // client 设置
                .clientSettings(ClientSettings.builder()
                        // 是否手动授权
                        .requireAuthorizationConsent(false)
                        .build())
                // token 设置
                .tokenSettings(TokenSettings.builder()
                        // 访问令牌格式，支持OAuth2TokenFormat.SELF_CONTAINED（自包含的令牌使用受保护的、有时间限制的数据结构，例如JWT）；OAuth2TokenFormat.REFERENCE（不透明令牌）
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        // token 签名算法
                        .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                        // 授权token过期时间
                        .accessTokenTimeToLive(Duration.ofSeconds(60 * 60 * 12))
                        // 刷新token过期时间
                        .refreshTokenTimeToLive(Duration.ofSeconds(60 * 60 * 24 * 7))
                        // 刷新token是否可重复使用
                        .reuseRefreshTokens(false)
                        .build())
                .build();
        JdbcRegisteredClientRepository jdbcRegisteredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
        // Save registered client in db as if in-memory
        // 保存一次就行了
//        jdbcRegisteredClientRepository.save(registeredClient);

        return jdbcRegisteredClientRepository;
    }

    /**
     * 授权信息存储
     * OAuth2AuthorizationService 是一个用于授权访问OAuth2资源的服务。它允许用户授权应用程序以访问特定资源，同时保护资源所有者的隐私。
     * 通过使用该服务，用户可以授权应用程序访问他们的资源，而不会暴露自己的凭据给应用程序。
     *
     * @param jdbcTemplate               数据源
     * @param registeredClientRepository 客户端信息存储
     * @return OAuth2AuthorizationService 授权信息存储
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
                                                           RegisteredClientRepository registeredClientRepository) {
        JdbcOAuth2AuthorizationService service = new JdbcOAuth2AuthorizationService(jdbcTemplate,
                registeredClientRepository);
        JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper authorizationRowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(
                registeredClientRepository);
        authorizationRowMapper.setLobHandler(new DefaultLobHandler());

        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.addMixIn(AuthUser.class, AuthUserMixin.class);
        authorizationRowMapper.setObjectMapper(objectMapper);
        service.setAuthorizationRowMapper(authorizationRowMapper);
        return service;
    }

    /**
     * 授权同意服务
     * <p>
     * OAuth2AuthorizationConsentService是一种授权同意服务，
     * 它让客户端应用程序能够与OAuth2.0协议兼容的资源服务器进行交互，以获得用户的授权，从而允许客户端应用程序访问保护资源。
     *
     * @param jdbcTemplate               数据源
     * @param registeredClientRepository 客户端信息存储
     * @return OAuth2AuthorizationConsentService 授权同意服务
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }


    /**
     * JWKSource 是一种用于存储JSON Web 密钥（JWK）的抽象源的接口。
     * 它被用于从一个安全的源获取JWK。
     * JWKSource有助于在应用程序中实现安全的密钥管理，以确保应用程序的安全性。
     *
     * @return JwtEncoder
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey;
        try {
            rsaKey = generateLoadRsa();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    /**
     * 对JWKSource 进行解码验证
     * JwtDecoder是一个用于解码JSON Web Token（JWT）的库，它支持多种编码方案，可以处理多种JWT类型，并提供灵活的API。
     * 它可以帮助用户解码和验证JWT令牌，以便在应用程序中正确使用它们。
     *
     * @param jwkSource JWKSource
     * @return JwtDecoder
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * AuthorizationServerSettings 授权服务器设置
     * OAuth 2.0 的 AuthorizationServerSettings 是一组设置，用于控制 OAuth 2.0 认证服务器的行为。
     * 这些设置可以帮助认证服务器管理用户的访问权限，以及如何验证客户端的凭证。
     * 它还可以控制服务器如何保护访问令牌，令牌的有效期限制，以及认证过程中允许的客户端类型。
     *
     * @return AuthorizationServerSettings
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
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


}
