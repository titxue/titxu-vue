package com.titxu.cloud.common.security.annotation;

import cn.hutool.core.util.StrUtil;
import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.core.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;


/**
 * 服务间接口不鉴权处理逻辑
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class SecurityInnerAspect implements Ordered {

    private final HttpServletRequest request;

    @SneakyThrows
    @Around("@within(inner) || @annotation(inner)")
    public Object around(ProceedingJoinPoint point, Inner inner) {
        // 先判断 inner 是否为空, 为空则获取类上注解
        if (inner == null) {
            Class<?> aClass = point.getTarget().getClass();
            inner = AnnotationUtils.findAnnotation(aClass, Inner.class);
        }
        // inner false 则不需要鉴权 直接放行
        if (inner == null || !inner.value()) {
            return point.proceed();
        }

        String header = request.getHeader(AuthConstants.FROM);
        if (inner.value() && !StrUtil.equals(AuthConstants.FROM_IN, header)) {
            log.warn("访问接口 {} 没有权限", inner.value());
            throw new BaseException("微服务内部接口，禁止访问", 5000);
        }
        return point.proceed();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
