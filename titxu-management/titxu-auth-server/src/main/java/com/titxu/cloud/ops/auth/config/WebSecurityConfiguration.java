package com.titxu.cloud.ops.auth.config;

import com.titxu.cloud.ops.auth.support.core.DaoAuthenticationProvider;
import com.titxu.cloud.ops.auth.support.core.FormIdentityLoginConfigurer;
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
     * spring security 默认的安全策略
     *
     * @param http security注入点
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/token/*").permitAll()// 开放自定义的部分端点
                        .anyRequest().authenticated()).headers().frameOptions().sameOrigin()// 避免iframe同源无法登录
                .and().apply(new FormIdentityLoginConfigurer()); // 表单登录个性化
        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new DaoAuthenticationProvider());
        return http.build();
    }

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
        http.securityMatchers((matchers) -> matchers.requestMatchers("/actuator/**", "/css/**", "/error"))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll()).requestCache().disable()
                .securityContext().disable()
                .cors().disable();
        return http.build();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
//                .and()
//                .authorizeRequests().antMatchers("/getPublicKey", "/oauth/logout", "/account/**").permitAll()
//                .antMatchers("/oauth/**", "/webjars/**", "/doc.html", "/swagger-resources/**", "/v2/api-docs").permitAll()
//                .antMatchers("/error").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .csrf().disable();
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

    /**
     * 自定义认证异常响应数据
     */
//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint() {
//        return (request, response, e) -> {
//            response.setStatus(HttpStatus.OK.value());
//            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Cache-Control", "no-cache");
//            response.getWriter().print(new Gson().toJson(Result.error(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMsg())));
//            response.getWriter().flush();
//        };
//    }
}

