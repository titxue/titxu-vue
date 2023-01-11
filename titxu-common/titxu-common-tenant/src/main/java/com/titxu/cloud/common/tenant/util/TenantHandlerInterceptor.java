package com.titxu.cloud.common.tenant.util;

import com.titxu.cloud.common.core.constant.CommonConstant;
import com.titxu.cloud.common.core.util.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.titxu.cloud.common.core.util.WebUtils.getJwtPayload;

/**
 * 租户拦截
 **/
@Component
public class TenantHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object handler) {
        // 多租户支持
        String tenantId = getJwtPayload().getStr(CommonConstant.TENANT_KEY);
        ;
        if (StringUtils.isNoneBlank(tenantId)) {
            TenantContext.setTenantId(tenantId);
        }
        return true;
    }
}
