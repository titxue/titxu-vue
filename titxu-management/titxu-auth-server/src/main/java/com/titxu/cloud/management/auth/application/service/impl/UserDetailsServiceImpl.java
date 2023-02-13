package com.titxu.cloud.management.auth.application.service.impl;

import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.management.auth.application.service.IUserDetailsService;
import com.titxu.cloud.sys.api.dto.AuthenticationDTO;
import com.titxu.cloud.sys.api.feign.RemoteAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 自定义用户认证和授权
 **/
@Primary
@Slf4j
@Service
public class UserDetailsServiceImpl implements IUserDetailsService {


    private RemoteAuthenticationService remoteAuthenticationService;

    private CacheManager cacheManager;

    @Autowired
    public void setRemoteAuthenticationService(RemoteAuthenticationService remoteAuthenticationService) {
        this.remoteAuthenticationService = remoteAuthenticationService;
    }

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cache cache = cacheManager.getCache(AuthConstants.USER_DETAILS);
        if (cache != null && cache.get(username) != null) {
            return (User) Objects.requireNonNull(Objects.requireNonNull(cache.get(username)).get());
        }
        AuthenticationDTO authenticationDTO = remoteAuthenticationService.loginByUserName(username);
        UserDetails userDetails = getUserDetails(authenticationDTO);
        if (cache != null) {
            cache.put(username, userDetails);
        }
        if (authenticationDTO != null) {
            return userDetails;
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
