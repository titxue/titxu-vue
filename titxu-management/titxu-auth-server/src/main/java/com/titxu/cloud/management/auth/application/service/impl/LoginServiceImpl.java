package com.titxu.cloud.management.auth.application.service.impl;


import com.alibaba.fastjson2.JSON;
import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.management.auth.application.service.LoginService;
import com.titxu.cloud.management.auth.application.command.LoginPasswordCommand;
import com.titxu.cloud.management.auth.application.command.RefreshCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
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
    public Map<String, Object> login(LoginPasswordCommand loginPasswordCommand) {
        log.info("登陆手机号:{}", loginPasswordCommand.getMobile());
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add(AuthConstants.GRANT_TYPE_KEY, AuthConstants.GRANT_TYPE_PASSWORD);
        parameters.add(AuthConstants.CLIENT_ID_KEY, "client");
        parameters.add(AuthConstants.CLIENT_SECRET_KEY, "123456");
        parameters.add(AuthConstants.REFRESH_TOKEN_KEY, loginPasswordCommand.getRefreshToken());
        parameters.add(AuthConstants.USER_NAME_TOKEN_KEY, loginPasswordCommand.getMobile());
        parameters.add(AuthConstants.PASSWORD, loginPasswordCommand.getPassword());
        parameters.add(AuthConstants.VALIDATE_CODE_KEY, loginPasswordCommand.getUuid());
        parameters.add(AuthConstants.VALIDATE_CODE_CODE, loginPasswordCommand.getCode());
        return getAuthToken(parameters);

    }

    private Map<String, Object> getAuthToken(MultiValueMap<String, Object> parameters) {
        HttpEntity<MultiValueMap<String, Object>> multiValueMapHttpEntity = new HttpEntity<>(parameters);

        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401 时候也要正常响应，不要抛出异常
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
        Map parseObject = JSON.parseObject(JSON.toJSONString(oAuth2AccessToken), Map.class);
        parseObject.remove("additionalInformation");
        parseObject.remove("scope");
        parseObject.remove("expired");
        parseObject.remove("expired");
        return parseObject;
    }

    /**
     * 刷新登陆
     *
     * @param refreshCommand 刷新token
     * @return token
     */
    @Override
    public Map<String, Object> refresh(RefreshCommand refreshCommand) {
        log.info("刷新token:{}", refreshCommand.getRefreshToken().substring(0, 16));
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add(AuthConstants.GRANT_TYPE_KEY, AuthConstants.REFRESH_TOKEN);
        parameters.add(AuthConstants.CLIENT_ID_KEY, "client");
        parameters.add(AuthConstants.CLIENT_SECRET_KEY, "123456");
        parameters.add(AuthConstants.REFRESH_TOKEN_KEY, refreshCommand.getRefreshToken());
        return getAuthToken(parameters);
    }


}
