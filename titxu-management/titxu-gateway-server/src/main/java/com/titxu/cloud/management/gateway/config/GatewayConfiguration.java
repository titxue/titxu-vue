//package com.titxu.cloud.management.gateway.config;
//
//import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
//import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.codec.ServerCodecConfigurer;
//import org.springframework.web.reactive.result.view.ViewResolver;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * 资源服务器配置
// **/
//
//@Slf4j
//@Configuration
//public class GatewayConfiguration {
//    private final List<ViewResolver> viewResolvers;
//    private final ServerCodecConfigurer serverCodecConfigurer;
//
//
//    public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
//        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
//        this.serverCodecConfigurer = serverCodecConfigurer;
//    }
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
//        // Register the block exception handler for Spring Cloud Gateway.
//        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(SentinelGatewayFilter.class)
//    public SentinelGatewayFilter sentinelGatewayFilter() {
//        return new SentinelGatewayFilter();
//    }
//
//    /**
//     * Gateway 跨域处理
//     *
//     * @return WebFilter
//     */
////    @Bean
////    public WebFilter corsFilter() {
////        return (ServerWebExchange ctx, WebFilterChain chain) -> {
////            ServerHttpRequest request = ctx.getRequest();
////            if (CorsUtils.isCorsRequest(request)) {
////                HttpHeaders requestHeaders = request.getHeaders();
////                ServerHttpResponse response = ctx.getResponse();
////                HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
////                HttpHeaders headers = response.getHeaders();
////                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
////                headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
////                if (requestMethod != null) {
////                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
////                }
////                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
////                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
////                headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
////                if (request.getMethod() == HttpMethod.OPTIONS) {
////                    response.setStatusCode(HttpStatus.OK);
////                    return Mono.empty();
////                }
////
////            }
////            return chain.filter(ctx);
////        };
////    }
//
//
//}
