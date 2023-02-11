package com.titxu.cloud.common.feign.config;

import cn.hutool.core.collection.CollUtil;
import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.core.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.Collection;
import java.util.Enumeration;

/**
 * oauth2 feign 授权传递
 * <p>
 * 重新 OAuth2FeignRequestInterceptor ，官方实现部分常见不适用
 */
@Slf4j
public class FeignConfiguration implements RequestInterceptor {


    /**
     * Create a template with the header of provided name and extracted extract </br>
     * <p>
     * 1. 如果使用 非web 请求，header 区别 </br>
     * <p>
     * 2. 根据authentication 还原请求headers </br>
     */
    @Override
    public void apply(RequestTemplate template) {
        Collection<String> fromHeader = template.headers().get(AuthConstants.FROM);
        // 带from 请求直接跳过
        if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(AuthConstants.FROM_IN)) {
            log.info("feign 调用，跳过 header 传递");
            return;
        }
        // 非web 请求直接跳过
        if (WebUtils.getRequest().isPresent()) {
            return;
        }
        HttpServletRequest request = WebUtils.getRequest().orElse(null);
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();

                // 注意：不要传递 Content-Length，防止接收不到响应

                if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(headerName)) {
                    String headerValue = request.getHeader(headerName);
                    template.header(headerName, headerValue);
                }
            }
        }
    }
}
