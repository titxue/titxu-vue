package com.titxu.cloud.sys.domain.model.user;

/**
 * 验证码缓存
 */
public interface CodeCache {
    void save(Account account);
}
