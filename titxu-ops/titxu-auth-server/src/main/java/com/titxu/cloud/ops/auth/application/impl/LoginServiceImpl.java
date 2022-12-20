package com.titxu.cloud.ops.auth.application.impl;


import com.alibaba.fastjson2.JSON;
import com.titxu.cloud.ops.auth.application.LoginService;
import com.titxu.cloud.ops.auth.application.command.LoginPasswordCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

/**
 * 登录服务实现
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {


    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, Object>  login(LoginPasswordCommand loginPasswordCommand) {
        log.info("登陆手机号:{}", loginPasswordCommand.getMobile());
        // TODO Auto-generated method stub
        //配置请求头
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//        headers.add("authorization", "Bearer 774720e6-8193-48b9-9fb0-7f0591ffbeef");
        headers.add("content-type", "application/json");

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "password");
        parameters.add("client_id", "client");
        parameters.add("client_secret", "123456");
        parameters.add("refresh_token", loginPasswordCommand.getRefreshToken());
        parameters.add("username", loginPasswordCommand.getMobile());
        parameters.add("password", loginPasswordCommand.getPassword());
        parameters.add("key", loginPasswordCommand.getUuid());
        parameters.add("code", loginPasswordCommand.getCode());
        HttpEntity<MultiValueMap<String, Object>> multiValueMapHttpEntity = new HttpEntity<>(parameters);

        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });


        ResponseEntity<OAuth2AccessToken> oAuth2AccessTokenResponseString = restTemplate.exchange(
                "http://localhost:8000/oauth/token",
                HttpMethod.POST,
                multiValueMapHttpEntity,
                OAuth2AccessToken.class);
        // 转换成map
        OAuth2AccessToken oAuth2AccessToken = oAuth2AccessTokenResponseString.getBody();
        Map<String, Object> parseObject = JSON.parseObject(JSON.toJSONString(oAuth2AccessToken), Map.class);
        parseObject.remove("additionalInformation");
        parseObject.remove("scope");
        parseObject.remove("expired");
        parseObject.remove("expired");
        return parseObject;

    }


}
