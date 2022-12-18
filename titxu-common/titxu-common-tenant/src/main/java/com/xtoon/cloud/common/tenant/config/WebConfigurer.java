package com.titxu.cloud.common.tenant.config;

import com.titxu.cloud.common.tenant.util.TenantHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器配置
 *


 **/
@Configuration
public class WebConfigurer extends WebMvcConfigurerAdapter {

    @Autowired
    private TenantHandlerInterceptor tenantHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantHandlerInterceptor)
                .excludePathPatterns("/auth/*");
    }
}
