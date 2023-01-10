package com.titxu.cloud.ops.auth.service;

import lombok.SneakyThrows;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * 客户端信息配置
 **/
public class JdbcClientDetailsServiceImpl extends JdbcClientDetailsService {

    public JdbcClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @SneakyThrows
    public ClientDetails loadClientByClientId(String clientId) {
        return super.loadClientByClientId(clientId);
    }
}
