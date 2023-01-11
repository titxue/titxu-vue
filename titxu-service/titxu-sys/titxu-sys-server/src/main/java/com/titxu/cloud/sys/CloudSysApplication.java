package com.titxu.cloud.sys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统服务
 **/
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
//@DubboComponentScan(basePackages = "com.titxu.cloud.sys")
public class CloudSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudSysApplication.class);
    }
}