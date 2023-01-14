package com.titxu.cloud.management.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 认证服务
 **/
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.titxu.cloud.sys.api.feign"})
public class CloudAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudAuthApplication.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
