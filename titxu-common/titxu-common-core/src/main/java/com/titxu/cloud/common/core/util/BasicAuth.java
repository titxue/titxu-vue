package com.titxu.cloud.common.core.util;

import com.titxu.cloud.common.core.constant.AuthConstants;
import lombok.experimental.UtilityClass;

import java.util.Base64;


@UtilityClass
public class BasicAuth {
    public String generateBasicAuth(String username, String password) {
        String plainCertificate = username + ":" + password;
        byte[] plainCertificateBytes = plainCertificate.getBytes();
        byte[] base64CertificateBytes = Base64.getEncoder().encode(plainCertificateBytes);
        String base64Certificate = new String(base64CertificateBytes);
        return AuthConstants.AUTHORIZATION_BASIC + base64Certificate;
    }
}