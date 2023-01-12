package com.titxu.cloud.ops.auth.service;

import com.titxu.cloud.ops.auth.domain.User;
import com.titxu.cloud.sys.dto.AuthenticationDTO;
import com.titxu.cloud.sys.feign.RemoteAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义用户认证和授权
 **/
@Service
@Slf4j
public class UserDetailsServiceImpl implements IUserDetailsService {


    private RemoteAuthenticationService remoteAuthenticationService;

    @Autowired
    public void setRemoteAuthenticationService(RemoteAuthenticationService remoteAuthenticationService) {
        this.remoteAuthenticationService = remoteAuthenticationService;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticationDTO authenticationDTO = remoteAuthenticationService.loginByUserName(username);
        if (authenticationDTO != null) {
            return new User(authenticationDTO);
        } else {
            throw new UsernameNotFoundException("用户不存在");
        }
    }
    /**
     * 是否支持此客户端校验
     * 手机端验证
     * @param clientId 目标客户端
     * @return true/false
     */
//    public boolean support(String clientId, String grantType) {
//        return AuthConstants.MOBILE.equals(grantType);
//    }
}
