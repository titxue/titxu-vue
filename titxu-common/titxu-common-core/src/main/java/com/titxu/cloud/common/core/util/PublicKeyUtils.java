package com.titxu.cloud.common.core.util;


import com.titxu.cloud.common.core.util.jose.KeyGeneratorUtils;
import lombok.experimental.UtilityClass;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

@UtilityClass
public class PublicKeyUtils {

    /**
     * 获取本地公钥
     *
     * @return 公钥
     * @throws Exception 异常
     */
    public RSAPublicKey loadPublicKey() {
        try {
            KeyPair keyPair = KeyGeneratorUtils.loadRsaKey();
            return (RSAPublicKey) keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
