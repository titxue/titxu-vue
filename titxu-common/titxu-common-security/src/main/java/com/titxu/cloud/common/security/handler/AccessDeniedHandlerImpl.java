package com.titxu.cloud.common.security.handler;

import com.titxu.cloud.common.core.util.ResponseUtils;
import com.titxu.cloud.common.core.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 访问被拒绝处理程序
 */
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        Result<?> error;

        if (accessDeniedException instanceof InvalidCsrfTokenException) {
            log.error("无效的 Csrf 令牌异常：", accessDeniedException);
            error = Result.error(1000, "令牌异常");
        } else {
            log.error("访问被拒绝异常：", accessDeniedException);
            error = Result.error(1001, "访问被拒绝");
        }

        ResponseUtils.response(response, error);
    }

}
