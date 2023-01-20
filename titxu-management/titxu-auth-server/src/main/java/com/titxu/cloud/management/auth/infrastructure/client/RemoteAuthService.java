package com.titxu.cloud.management.auth.infrastructure.client;

import com.titxu.cloud.common.core.constant.AuthConstants;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;


@HttpExchange
public interface RemoteAuthService {

    @PostExchange("/oauth2/token")
    Map<String, Object> login(@RequestParam(AuthConstants.GRANT_TYPE_KEY) String grantType,
                              @RequestParam(AuthConstants.SCOPE_KEY) String scope,
                              @RequestParam(AuthConstants.USERNAME_KEY) String username,
                              @RequestParam(AuthConstants.PASSWORD) String password,
                              @RequestHeader(AuthConstants.AUTHORIZATION_KEY) String authorization);

    @PostExchange("/oauth2/token")
    Map<String, Object> refreshToken(@RequestParam(AuthConstants.GRANT_TYPE_KEY) String grantType,
                                     @RequestParam(AuthConstants.REFRESH_TOKEN) String refreshToken,
                                     @RequestHeader(AuthConstants.AUTHORIZATION_KEY) String authorization);

}
