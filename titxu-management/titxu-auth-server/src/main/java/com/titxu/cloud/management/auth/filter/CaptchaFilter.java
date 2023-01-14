package com.titxu.cloud.management.auth.filter;


import com.alibaba.fastjson2.JSONObject;
import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.web.util.Result;
import com.titxu.cloud.sys.api.feign.RemoteAuthenticationService;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 验证码过滤器
 **/
@Slf4j
@Component
public class CaptchaFilter extends OncePerRequestFilter {


    private RemoteAuthenticationService remoteAuthenticationService;

    @Resource
    public void setRemoteAuthenticationService(RemoteAuthenticationService remoteAuthenticationService) {
        this.remoteAuthenticationService = remoteAuthenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        RequestMatcher matcher = new AntPathRequestMatcher(AuthConstants.OAUTH_TOKEN, HttpMethod.POST.toString());
        // 是否启用验证码
        boolean isEnableCaptcha = false;
        if (isEnableCaptcha && matcher.matches(httpServletRequest)
                && StringUtils.equalsIgnoreCase(httpServletRequest.getParameter(AuthConstants.GRANT_TYPE_KEY), AuthConstants.PASSWORD)) {
            String code = httpServletRequest.getParameter(AuthConstants.VALIDATE_CODE_CODE);
            String key = httpServletRequest.getParameter(AuthConstants.VALIDATE_CODE_KEY);
            if (!remoteAuthenticationService.validateCaptcha(key, code)) {
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                httpServletResponse.getOutputStream().write(JSONObject.toJSONString(Result.error("验证码不正确")).getBytes());
            } else {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
