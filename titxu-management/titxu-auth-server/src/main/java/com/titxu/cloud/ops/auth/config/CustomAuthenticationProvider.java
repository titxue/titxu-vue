/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.titxu.cloud.ops.auth.config;

import com.titxu.cloud.ops.auth.domain.User;
import com.titxu.cloud.sys.dto.AuthenticationDTO;
import com.titxu.cloud.sys.feign.RemoteAuthenticationService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * auth provider.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private RemoteAuthenticationService remoteAuthenticationService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        AuthenticationDTO authenticationDTO = remoteAuthenticationService.loginByUserName(username);

        if (null != authenticationDTO) {
            return new UsernamePasswordAuthenticationToken(new User(authenticationDTO), authentication.getCredentials(), authentication.getAuthorities());
        } else {
            throw new UsernameNotFoundException("用户不存在");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

}
