package com.titxu.cloud.wechat.infrastructure.feign.wechat.factory;

import com.titxu.cloud.wechat.infrastructure.feign.wechat.fallback.RemoteWechatServiceFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 回调工厂
 */
@Component
public class RemoteWechatServiceFallbackFactory implements FallbackFactory<RemoteWechatServiceFallback> {

    @Override
    public RemoteWechatServiceFallback create(Throwable throwable) {
        return new RemoteWechatServiceFallback(throwable);
    }


}
