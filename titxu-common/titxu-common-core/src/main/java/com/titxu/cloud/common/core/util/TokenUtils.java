package com.titxu.cloud.common.core.util;

import com.titxu.cloud.common.core.constant.AuthConstants;

/**
 * token 处理
 */
public class TokenUtils {
    /**
     * 去掉token前缀
     */
    public static String removeTokenPrefix(String token) {
        return token.replace(AuthConstants.AUTHORIZATION_PREFIX, "");
    }
}
