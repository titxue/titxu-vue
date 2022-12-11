package com.titxu.core.sys.infrastructure.cache;

import com.titxu.core.common.redis.util.RedisService;
import com.titxu.core.sys.domain.model.user.Account;
import com.titxu.core.sys.domain.model.user.CodeCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@AllArgsConstructor
public class CodeCacheImpl implements CodeCache {

    private RedisService redisService;

    private final static String CODE_KEY = "user:sms:code:";

    @Override
    public void save(Account account) {

        String key = CODE_KEY + account.getMobile().getMobile();
        String code = account.getCode().getCode();
        log.info("redis:{}", key);
        log.info("验证码:{}", code);
        redisService.set(key, code,60L);

        String codeStr = (String)redisService.get(key);
        log.info("redis_value:{}",codeStr);
    }
}
