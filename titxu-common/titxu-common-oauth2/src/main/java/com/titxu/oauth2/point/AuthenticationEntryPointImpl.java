package com.titxu.oauth2.point;


import com.titxu.web.util.CodeEnums;
import com.titxu.web.util.Response;
import com.titxu.web.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
			AuthenticationException authException) throws IOException, ServletException {
		log.error("身份验证入口点", authException);

		Response<?> error = Response.error(CodeEnums.T00000.code, CodeEnums.T00000.msg);

		error.setExplain(authException.getMessage());

		ResponseUtils.response(response, error);
	}

}
