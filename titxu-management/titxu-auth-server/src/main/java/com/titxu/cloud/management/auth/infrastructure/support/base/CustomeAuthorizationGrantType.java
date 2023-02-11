package com.titxu.cloud.management.auth.infrastructure.support.base;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.Serializable;

public final class CustomeAuthorizationGrantType implements Serializable {
    public static final AuthorizationGrantType PASSWORD = new AuthorizationGrantType("password");
}
