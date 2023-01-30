package com.titxu.cloud.common.security.point;

import com.titxu.cloud.common.core.util.ResponseUtils;
import com.titxu.cloud.common.core.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 身份验证入口点
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("身份验证入口点", authException);
        Result<?> error = Result.error(401, "身份未认证");
        response.setStatus(401);
        ResponseUtils.response(response, error);
    }

}
