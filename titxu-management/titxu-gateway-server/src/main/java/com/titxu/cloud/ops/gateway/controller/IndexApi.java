package com.titxu.cloud.management.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 网关
 **/
@RestController
public class IndexApi {

    /**
     * 网关测试
     *
     * @return
     */
    @RequestMapping("/")
    public Mono<String> index() {
        return Mono.just("titxu cloud gateway");
    }
}
