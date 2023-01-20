package com.titxu.cloud.common.core.constant;

/**
 * 认证常量类
 **/
public interface AuthConstants {
    /**
     * 内部
     */
    String FROM_IN = "Y";

    /**
     * 标志
     */
    String FROM = "from";

    /**
     * 请求header
     */
    String HEADER_FROM_IN = FROM + "=" + FROM_IN;

    /**
     * 认证请求头key
     */
    String AUTHORIZATION_KEY = "Authorization";

    String AUTHORIZATION_BASIC = "Basic ";

    /**
     * JWT令牌前缀
     */
    String AUTHORIZATION_PREFIX = "bearer ";
    String REDIRECT_URI = "https://www.baidu.com";

    /**
     * Basic认证前缀
     */
    String BASIC_PREFIX = "Basic ";

    /**
     * JWT载体key
     */
    String JWT_PAYLOAD_KEY = "payload";

    /**
     * JWT ID 唯一标识
     */
    String JWT_JTI = "jti";

    /**
     * JWT ID 唯一标识
     */
    String JWT_EXP = "exp";

    /**
     * Redis缓存权限规则key
     */
    String PERMISSION_ROLES_KEY = "auth:permission:roles";

    /**
     * 黑名单token前缀
     */
    String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";

    String CLIENT_DETAILS_FIELDS = "client_id, CONCAT('{noop}',client_secret) as client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    String BASE_CLIENT_DETAILS_SQL = "select " + CLIENT_DETAILS_FIELDS + " from oauth_client_details";

    String FIND_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " order by client_id";

    String SELECT_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " where client_id = ?";


    /**
     * 认证地址
     */
    String OAUTH_TOKEN = "/oauth/token";
    /**
     * 授权码模式confirm
     */
    String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";
    /**
     * 项目的license
     */
    String PROJECT_LICENSE = "https://nas.titxu.com";
    /**
     * 协议字段
     */
    String DETAILS_LICENSE = "license";
    /**
     * 密码加密方式
     */
    String BCRYPT = "{bcrypt}";

    String USER_ID_KEY = "userId";

    String USER_NAME_KEY = "userName";
    String USERNAME_KEY = "username";

    String USER_NAME_TOKEN_KEY = "username";

    String TENANT_ID_KEY = "tenantId";

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT存储权限属性
     */
    String JWT_AUTHORITIES_KEY = "authorities";


    String GRANT_TYPE_KEY = "grant_type";

    String GRANT_TYPE_PASSWORD = "password";

    String CLIENT_ID_KEY = "client_id";

    String CLIENT_SECRET_KEY = "client_secret";

    String SCOPE_KEY = "scope";
    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";


    String REFRESH_TOKEN_KEY = "refresh_token";
    String REFRESH_TOKEN = "refresh_token";
    /**
     * 用户信息
     */
    String DETAILS_USER = "user_info";
    /**
     * 密码
     */
    String PASSWORD = "password";
    /**
     * 用户信息缓存
     */
    String USER_DETAILS = "user_details";
    /**
     * 验证码
     */
    String VALIDATE_CODE_CODE = "code";

    /**
     * 验证码 key
     */
    String VALIDATE_CODE_KEY = "key";
    /**
     * 手机号登录
     */
    String MOBILE = "mobile";
    /**
     * 请求开始时间
     */
    String REQUEST_START_TIME = "REQUEST-START-TIME";


    String ADMIN_CLIENT_ID = "admin";

    String ADMIN_CLIENT_SECRET = "admin";

    String ADMIN_SCOPE = "server";
}
