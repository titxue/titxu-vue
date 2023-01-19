package com.titxu.cloud.management.auth.api;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.titxu.cloud.common.core.util.jose.KeyGeneratorUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 获取公钥接口
 **/
@Tag(name = "获取公钥接口")
@RestController
@RequestMapping
@Slf4j
public class PublicKeyController {

    @GetMapping("/getPublicKey")
    public Map<String, Object> loadPublicKey() throws Exception {
        KeyPair keyPair = KeyGeneratorUtils.loadRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
