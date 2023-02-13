package com.titxu.cloud.management.auth.application.service;

import com.titxu.cloud.common.security.domain.AuthUser;
import com.titxu.cloud.sys.api.dto.AuthenticationDTO;
import org.springframework.core.Ordered;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;


public interface IUserDetailsService extends UserDetailsService, Ordered {
    /**
     * 构建 UserDetails
     *
     * @param authenticationDTO 用户信息
     * @return UserDetails
     */
    default UserDetails getUserDetails(AuthenticationDTO authenticationDTO) {
        AuthenticationDTO info = Optional.of(authenticationDTO).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        // 构造security用户
        return new AuthUser(info);
    }

    /**
     * 是否支持此客户端校验
     *
     * @param clientId 目标客户端
     * @return true/false
     */
    default boolean support(String clientId, String grantType) {
        return true;
    }

    /**
     * 排序值 默认取最大的
     *
     * @return 排序值
     */
    default int getOrder() {
        return 0;
    }

    /**
     * 通过用户实体查询
     */
    default UserDetails loadUserByUser(User user) {
        return this.loadUserByUsername(user.getUsername());
    }

}
