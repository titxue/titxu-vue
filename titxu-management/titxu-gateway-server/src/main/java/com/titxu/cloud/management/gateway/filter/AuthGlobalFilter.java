package com.titxu.cloud.management.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nimbusds.jose.JWSObject;
import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.redis.util.RedisService;
import com.titxu.cloud.common.web.constant.ResultCode;
import com.titxu.cloud.management.gateway.util.WebUtils;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 全局过滤器
 **/
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {


    private RedisService redisService;

    public static void main(String[] args) {
        String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxODY2NjY2NjY2NiIsInNjb3BlIjpbImFsbCJdLCJ0ZW5hbnRJZCI6IjEiLCJleHAiOjE2NzE1NzU4NTAsInVzZXJOYW1lIjoiMTg2NjY2NjY2NjYiLCJ1c2VySWQiOiIxNDA4NDAzODAwMzQ1MzAwOTk0IiwianRpIjoiODBKNzlWU3U2ajlzb3JDTDlKTEpwUlZmQ3pJIiwiY2xpZW50X2lkIjoiY2xpZW50In0.s6afHe8hw-KAKmd-QriL2JzGa3pAWLViaLUSJREsV42ePeyeVRH4tYtQ4ldpq_7MVm0-yi9GwtpNwvF5ybGbcfyKo-SEC4Tuhb-wrLTMG2rM7ACkMbCggcQwaIFqK-jVvjjMXBXVeJqCmDn81gLB-D0FWHBfZKzi8lfmWKEqwPPuueP10QOdRYlBofNV399oW43MzR4jqjMHaHl55SpEW3fCx0weRAFxHI-LC67s-2mCeJ3ubjvtT5j4NCsguVe9J6jgOYhuku5Kc9fVxNgkFhu4SJckpsqz9IopCGLoQ4L2eneM4bIDlqXMF-4Yh1zLqhTzYk2-XqIaibvOGrVO5A";
        boolean b = token.startsWith(AuthConstants.AUTHORIZATION_PREFIX);
        System.out.println(b);
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 非JWT或者JWT为空不作处理
        String token = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_KEY);
        if (StrUtil.isBlank(token) || !token.startsWith(AuthConstants.AUTHORIZATION_PREFIX)) {
            return chain.filter(exchange);
        }

        // 解析JWT获取jti，以jti为key判断redis的黑名单列表是否存在，存在拦截响应token失效
        token = token.replace(AuthConstants.AUTHORIZATION_PREFIX, Strings.EMPTY);
        JWSObject jwsObject = JWSObject.parse(token);
        String payload = jwsObject.getPayload().toString();
        JSONObject jsonObject = JSONObject.parseObject(payload);
        String userInfo = jsonObject.getString("user_info");
        String jti = jsonObject.getString(AuthConstants.JWT_JTI);
        Boolean isBlack = redisService.hasKey(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti);
        if (isBlack) {
            response.setRawStatusCode(ResultCode.UNAUTHORIZED.getCode());
            return WebUtils.getAuthFailResult(response, ResultCode.UNAUTHORIZED.getCode());
        }

        // 存在token且不是黑名单，request写入JWT的载体信息
        // 避免userInfo信息有中文，继续URLEncoder转码
        String encodeUserInfo = URLEncoder.encode(userInfo, StandardCharsets.UTF_8);
        request = exchange.getRequest().mutate()
                .header(AuthConstants.JWT_PAYLOAD_KEY, encodeUserInfo)
                .build();
        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
