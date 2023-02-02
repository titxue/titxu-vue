package com.titxu.cloud.management.gateway.util;

import com.google.gson.Gson;
import com.titxu.cloud.common.web.constant.ResultCode;
import com.titxu.cloud.common.web.util.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * web工具类
 **/
public class WebUtils {

    private static final String ALLOWED_HEADERS = "X-Requested-With, tenant_id, Blade-Auth, Content-Type, Authorization, credential, X-XSRF-TOKEN, token, username, client, knfie4j-gateway-request, request-origion";
    private static final String ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS,HEAD";
    private static final String ALLOWED_ORIGIN = "http://localhost:8001";
    private static final String ALLOWED_EXPOSE = "*";
    private static final String MAX_AGE = "18000L";

    public static Mono<Void> getAuthFailResult(ServerHttpResponse response, Integer code) {
        response.setRawStatusCode(HttpServletResponse.SC_OK);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        response.getHeaders().add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        response.getHeaders().add("Access-Control-Allow-Methods", ALLOWED_METHODS);
        response.getHeaders().add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        response.getHeaders().add("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
        response.getHeaders().add("Access-Control-Max-Age", MAX_AGE);
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        byte[] responseByte = new Gson().toJson(Result.error(code, Objects.requireNonNull(ResultCode.getValue(code)).getMsg())).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(responseByte);
        return response.writeWith(Flux.just(buffer));
    }
}
