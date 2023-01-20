package com.titxu.cloud.management.auth.application.dto;

import com.titxu.cloud.common.core.constant.AuthConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * oauth2 token
 */
@Data
@Schema(name = "oauth2", description = "oauth2")
public class OAuth2Dto {
    /**
     * 手机号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 刷新token
     */
    private String refresh_token;

    /**
     * 客户端类型
     */
    private String grant_type;

    /**
     * 客户端权限范围
     */
    private  String scope;

    /**
     * redirect_uri
     * 重定向url
     */
    private String redirect_uri;

    /**
     * code
     */
    private String code;

    public OAuth2Dto() {
    }


    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private String username;

        private String password;

        private String refresh_token;

        private String grant_type;

        private  String scope;

        private String redirect_uri;

        private String code;


        /**
         * refresh_token 模式
         */
        public Builder refreshToken(String refresh_token) {
            this.refresh_token = refresh_token;
            this.grant_type = AuthConstants.REFRESH_TOKEN;
            return this;
        }

        /**
         * 授权码模式 code
         */
        public Builder code(String grant_type, String code) {
            this.grant_type = grant_type;
            this.redirect_uri = AuthConstants.REDIRECT_URI;
            this.code = code;
            return this;
        }

        /**
         * 密码模式
         */
        public Builder password(String username, String password) {
            this.username = username;
            this.password = password;
            this.grant_type = AuthConstants.GRANT_TYPE_PASSWORD;
            this.scope = AuthConstants.ADMIN_SCOPE;
            return this;
        }
        public OAuth2Dto build() {
            return new OAuth2Dto(this);
        }

    }
    public OAuth2Dto(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.refresh_token = builder.refresh_token;
        this.grant_type = builder.grant_type;
        this.scope = builder.scope;
        this.redirect_uri = builder.redirect_uri;
        this.code = builder.code;
    }
}
