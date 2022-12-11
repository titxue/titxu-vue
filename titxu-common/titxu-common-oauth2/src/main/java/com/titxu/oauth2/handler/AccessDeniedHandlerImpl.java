package com.titxu.oauth2.handler;



import com.titxu.web.util.CodeEnums;
import com.titxu.web.util.Response;
import com.titxu.web.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问被拒绝处理程序
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		Response<?> error;

		if (accessDeniedException instanceof InvalidCsrfTokenException) {
			log.error("无效的 Csrf 令牌异常：", accessDeniedException);
			error = Response.error(CodeEnums.A20005.code, CodeEnums.A20005.msg);
		}
		else {
			log.error("访问被拒绝异常：", accessDeniedException);
			error = Response.error(CodeEnums.T00002.code, CodeEnums.T00002.msg);
		}

		ResponseUtils.response(response, error);
	}

}
