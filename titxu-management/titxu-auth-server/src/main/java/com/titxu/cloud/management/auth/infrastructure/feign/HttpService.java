package com.titxu.cloud.management.auth.infrastructure.feign;

import com.titxu.cloud.common.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Component
@Slf4j
public class HttpService {
    private static final String SERVICE_NAME = "titxu-server-auth";
    private ReactorLoadBalancerExchangeFilterFunction reactorLoadBalancerExchangeFilterFunction;


    @Autowired
    public void setReactorLoadBalancerExchangeFilterFunction(ReactorLoadBalancerExchangeFilterFunction reactorLoadBalancerExchangeFilterFunction) {
        this.reactorLoadBalancerExchangeFilterFunction = reactorLoadBalancerExchangeFilterFunction;
    }

    /**
     * 获取http Service 接口
     *
     * @param clazz       接口类型
     * @param ServiceName 服务名称
     * @return 接口实例
     */
    public <T> T getService(Class<T> clazz, String ServiceName) {


        // 根据微服务名称返回，服务实例
        // 创建webClient
        WebClient client = WebClient.builder()
                .filter(reactorLoadBalancerExchangeFilterFunction)
                .baseUrl(String.format("http://%s/", ServiceName))
                .defaultStatusHandler(HttpStatusCode::isError, resp -> {
                    log.error("请求失败:{}", resp.statusCode().value());
                    if (resp.statusCode().is4xxClientError()){
                        throw new BaseException("资源无法访问",resp.statusCode().value());
                    }
                    throw new BaseException("请求出现错误",resp.statusCode().value());
                })
                .build();

        // WebClientAdapter
        WebClientAdapter clientAdapter = WebClientAdapter.forClient(client);
        // 创建webClient 适配器
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(clientAdapter).build();

        // 创建接口实例
        return factory.createClient(clazz);
    }

    @Bean
    public RemoteAuthService remoteAuthLoadBalancer() {
        return getService(RemoteAuthService.class, SERVICE_NAME);
    }
}
