package com.titxu.cloud.management.auth.support.core;

import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.security.domain.AuthUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * token 输出增强
 */
public class JwtCustomeOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    /**
     * Customize the OAuth 2.0 Token attributes.
     *
     * @param context the context containing the OAuth 2.0 Token attributes
     */
    @Override
    public void customize(JwtEncodingContext context) {
        JwtClaimsSet.Builder claims = context.getClaims();
        claims.claim(AuthConstants.DETAILS_LICENSE, AuthConstants.PROJECT_LICENSE);
//        String clientId = context.getAuthorizationGrant().getName();
//        claims.claim(AuthConstants.CLIENT_ID_KEY, clientId);
        // 客户端模式不返回具体用户信息、
        if (AuthConstants.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
            return;
        }
        // 判断是否是User类型，如果是则返回User
        if (context.getPrincipal().getPrincipal() instanceof User user && "refresh_token".equals(context.getAuthorizationGrantType().getValue())) {
            claims.claim(AuthConstants.DETAILS_USER, user);
            return;
        }
        AuthUser authUser = (AuthUser) context.getPrincipal().getPrincipal();

        // 操作 claims 删除信息 、增加信息 、修改信息
        claims.claims(map -> {
//            map.remove("aud");
//            map.remove("nbf");
//            map.remove("iss");
//            map.remove("sub");
//            map.remove("exp");
//            map.remove("iat");
//            map.remove("jti");
//            map.remove("expires_in");
//            map.remove("scope");
        });
        claims.claim(AuthConstants.DETAILS_USER, authUser);
    }

}
