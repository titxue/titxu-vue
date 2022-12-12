package com.titxu.cloud.sys;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 系统服务
 **/
@EnableDiscoveryClient
@SpringBootApplication
@DubboComponentScan(basePackages = "com.titxu.cloud.sys")
public class CloudSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudSysApplication.class);
    }
}
