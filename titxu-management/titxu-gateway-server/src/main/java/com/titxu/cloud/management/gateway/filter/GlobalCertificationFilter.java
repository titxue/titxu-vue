package com.titxu.cloud.management.gateway.filter;

import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.core.util.Result;
import com.titxu.cloud.management.gateway.config.WhiteListConfig;
import com.titxu.cloud.management.gateway.util.WebFluxUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;

import static org.springframework.cloud.gateway.filter.WebsocketRoutingFilter.SEC_WEBSOCKET_PROTOCOL;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.ACCESS_DENIED;


@Component
public class GlobalCertificationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(GlobalCertificationFilter.class);

    private WhiteListConfig whiteListConfig;


    @Autowired
    public void setWhiteListConfig(WhiteListConfig whiteListConfig) {
        this.whiteListConfig = whiteListConfig;
    }

    /**
     * 白名单配置
     *
     * @param exchange 服务器网络交换
     * @return 返回匹配结果
     */
    private boolean whiteList(ServerWebExchange exchange) {

        ServerHttpRequest request = exchange.getRequest();
        HttpMethod method = request.getMethod();
        URI uri = request.getURI();
        String path = uri.getPath();

        if (method.matches(HttpMethod.OPTIONS.name())) {
            log.debug("放行：{}：{}", method, path);
            return true;
        }

        AntPathMatcher antPathMatcher = new AntPathMatcher();

        List<String> ignores = whiteListConfig.getUrls();
        for (String ignore : ignores) {
            boolean match = antPathMatcher.match(ignore, path);
            if (match) {
                return true;
            }
        }

        InetSocketAddress remoteAddress = request.getRemoteAddress();

        if (remoteAddress == null) {
            return false;
        }


        return false;
    }


    /**
     * 说明：目前所有请求均是通过Gateway进行访问。
     * /oauth/check_token，是比较特殊的地址，不是使用token的方式进行鉴权。
     * 虽然目前使用的是“permitAll”的方式，不够安全。但是不管什么情况，在Gateway这一端，不应该进行拦截。
     * 后续可以根据IP，以及OAuth2鉴权的方式进行安全控制。
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.debug("[Herodotus] |- Gateway Global Certification Filter in use!");

        // 1.检查是否是免登陆连接
        if (this.whiteList(exchange)) {
            return chain.filter(exchange);
        }

        // 2.外部进入的请求，如果包含 HEADER_FROM_IN 请求头，认为是非法请求，直接拦截。HEADER_FROM_IN 只能用于内部 Feign 间忽略权限使用
        String fromIn = exchange.getRequest().getHeaders().getFirst(AuthConstants.HEADER_FROM_IN);
        if (ObjectUtils.isNotEmpty(fromIn)) {
            log.warn("[Herodotus] |- Illegal request to disable access!");
            return WebFluxUtils.writeJsonResponse(exchange.getResponse(), Result.error(HttpStatus.SC_FORBIDDEN, "Illegal request to disable access!"));
        }

        String webSocketToken = exchange.getRequest().getHeaders().getFirst(SEC_WEBSOCKET_PROTOCOL);
        if (StringUtils.isNotBlank(webSocketToken) && StringUtils.endsWith(webSocketToken, ".stomp")) {
            return chain.filter(exchange);
        }

        // 3.非免登陆地址，获取token 检查token，如果为空，或者不是 Bearer XXX形式，则认为未授权。
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!isTokenWellFormed(token)) {
            log.warn("[Herodotus] |- Token is not Well Formed!");
            return WebFluxUtils.writeJsonResponse(exchange.getResponse(), Result.error(ACCESS_DENIED));
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }

    private boolean isTokenWellFormed(String token) {
        return !StringUtils.isBlank(token) && !StringUtils.containsOnly(token, AuthConstants.AUTHORIZATION_PREFIX);
    }
}