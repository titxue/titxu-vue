package com.titxu.cloud.management.auth.infrastructure.client;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;


@HttpExchange
public interface RemoteAuthService {

    @PostExchange("/oauth2/token")
    Map<String, Object> login(@RequestParam("grant_type") String grantType,
                              @RequestParam("scope") String scope,
                              @RequestParam("username") String username,
                              @RequestParam("password") String password,
                              @RequestHeader("Authorization") String authorization);
}
