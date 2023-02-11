/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.titxu.cloud.management.auth.infrastructure.support.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.core.util.SpringContextHolder;
import com.titxu.cloud.common.log.event.SysLogEvent;
import com.titxu.cloud.common.log.util.SysLogUtils;
import com.titxu.cloud.common.security.domain.AuthUser;
import com.titxu.cloud.sys.api.dto.LogDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功处理器
 */
@Slf4j
public class AuthenticationSuccessEventHandler implements AuthenticationSuccessHandler {

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     *                       the authentication process.
     */
    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
        Map<String, Object> map = accessTokenAuthentication.getAdditionalParameters();
        if (MapUtil.isNotEmpty(map)) {
            // 发送异步日志事件
            AuthUser authUserInfo = (AuthUser) map.get(AuthConstants.DETAILS_USER);
            log.info("用户：{} 登录成功", authUserInfo.getUsername());
            SecurityContextHolder.getContext().setAuthentication(accessTokenAuthentication);
            LogDTO logDTO = SysLogUtils.getSysLog();
            logDTO.setMobile(authUserInfo.getUsername());
            logDTO.setUserNick(authUserInfo.getUserNick());
            logDTO.setTenantId(authUserInfo.getTenantId());

            logDTO.setOperation("登录成功");
            String startTimeStr = request.getHeader(AuthConstants.REQUEST_START_TIME);
            if (StrUtil.isNotBlank(startTimeStr)) {
                Long startTime = Long.parseLong(startTimeStr);
                Long endTime = System.currentTimeMillis();
                logDTO.setTime(endTime - startTime);
            }

            SpringContextHolder.publishEvent(new SysLogEvent(logDTO));
        }

        // 输出token
        sendAccessTokenResponse(response, authentication);
    }

    private void sendAccessTokenResponse(HttpServletResponse response,
                                         Authentication authentication) throws IOException {

        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = new HashMap<>(accessTokenAuthentication.getAdditionalParameters());
        /*
         * aud：audience，表示接收token的一方；
         * nbf：not before，表示token有效期开始时间；
         * sub：subject，表示token的主题，可以用来标识token的用户；
         * iss：issuer，表示token的签发者；
         * jti：jti，表示token的唯一标识，避免重放攻击；
         * iat：issued at，表示token的签发时间；
         * exp：expires，表示token的过期时间；
         * expires_in：表示token的有效期；
         * scope：表示权限范围。
         */
        // 删除敏感的参数
        additionalParameters.remove("aud");
        additionalParameters.remove("nbf");
        additionalParameters.remove("sub");
        additionalParameters.remove("iss");
        additionalParameters.remove("jti");
        additionalParameters.remove("iat");
        additionalParameters.remove("exp");
        additionalParameters.remove("scope");


        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType()).scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        // 无状态 注意删除 context 上下文的信息
        SecurityContextHolder.clearContext();
        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
    }


}
