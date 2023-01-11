package com.titxu.cloud.ops.auth.support.core;

import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.ops.auth.domain.User;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * token 输出增强
 *
 * @author lengleng
 * @date 2022/6/3
 */
public class CustomeOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    /**
     * Customize the OAuth 2.0 Token attributes.
     *
     * @param context the context containing the OAuth 2.0 Token attributes
     */
    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        OAuth2TokenClaimsSet.Builder claims = context.getClaims();
        claims.claim(AuthConstants.DETAILS_LICENSE, AuthConstants.PROJECT_LICENSE);
        String clientId = context.getAuthorizationGrant().getName();
        claims.claim(AuthConstants.CLIENT_ID_KEY, clientId);
        // 客户端模式不返回具体用户信息
        if (AuthConstants.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
            return;
        }

        User user = (User) context.getPrincipal().getPrincipal();
        claims.claim(AuthConstants.DETAILS_USER, user);
    }

}
