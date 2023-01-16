package com.titxu.cloud.management.auth.controller;


import com.alibaba.nacos.common.utils.CollectionUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户登陆
 **/
@Tag(name = "用户登陆")
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private DiscoveryClient discoveryClient;

    @Autowired
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /**
     * 获取微服务 服务列表
     */
    @GetMapping("/getServices")
    public String getServices() {
        List<String> services = discoveryClient.getServices();
        log.info("Services: {}", services);
        if (CollectionUtils.isNotEmpty(services)) {
            for (String service : services) {
                List<ServiceInstance> instances = discoveryClient.getInstances(service);
                log.info("********{}-{}", service, instances);
                instances.stream().forEach(instance -> log.info("{}", instance));
            }
        }
        return "ok";

    }


}
