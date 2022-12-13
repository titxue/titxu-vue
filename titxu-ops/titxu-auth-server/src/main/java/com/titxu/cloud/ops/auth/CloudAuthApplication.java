package com.titxu.cloud.ops.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 认证服务
 *
 * @author haoxin
 * @date 2021-06-15
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class CloudAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudAuthApplication.class);
    }
}
