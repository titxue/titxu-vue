package com.titxu.cloud.ops.auth.service;

import com.titxu.cloud.ops.auth.client.AuthenticationClient;
import com.titxu.cloud.ops.auth.domain.User;
import com.titxu.cloud.sys.dto.AuthenticationDTO;
import com.titxu.cloud.sys.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义用户认证和授权
 **/
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    private AuthenticationClient authenticationClient;

    @Autowired
    public void setAuthenticationClient(AuthenticationClient authenticationClient) {
        this.authenticationClient = authenticationClient;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticationDTO authenticationDTO = authenticationClient.loginByUserName(username);
        if (authenticationDTO != null) {
            return new User(authenticationDTO);
        } else {
            throw new UsernameNotFoundException("用户不存在");
        }
    }
}
