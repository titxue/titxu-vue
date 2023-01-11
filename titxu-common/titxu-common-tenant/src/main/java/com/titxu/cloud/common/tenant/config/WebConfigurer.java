package com.titxu.cloud.common.tenant.config;

import com.titxu.cloud.common.tenant.util.TenantHandlerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 **/
@Configuration
@RequiredArgsConstructor
public class WebConfigurer implements WebMvcConfigurer {


    private final TenantHandlerInterceptor tenantHandlerInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantHandlerInterceptor)
                .excludePathPatterns("/auth/*");
    }
}
