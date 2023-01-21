### 核心依赖

| 依赖                          | 版本             |
|-----------------------------|----------------|
| Spring Boot                 | 3.0.1          |
| Spring Cloud                | 2022.0.0       |
| Spring Cloud Alibaba        | 2022.0.0.0-RC1 |
| Spring Authorization Server | 1.0.0          |
| Mybatis Plus                | 3.5.3.1        |
| hutool                      | 5.8.10         |

```shell
# 测试接口
curl --location --request POST 'http://127.0.0.1:8001/auth/loginByUserName' -d 'userName=18555555555'

# 测试授权接口
curl --location --request GET \
'http://127.0.0.1:8000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=message.read&redirect_uri=https://www.baidu.com'
```

```mysql
INSERT INTO `nft`.`notice` (`id`, `class_id`, `class_name`, `cover_img`, `time`, `title`, `url`)
VALUES (1, 1, '1', '
1', '2022-11-03 18:54:25', '1', '1')
# 插入 oauth2_register_client 表数据
INSERT INTO `titxu-cloud`.`oauth2_registered_client` (`id`, `client_id`, `client_id_issued_at`, `client_secret`,
                                                      `client_secret_expires_at`, `client_name`,
                                                      `client_authentication_methods`, `authorization_grant_types`,
                                                      `redirect_uris`, `scopes`, `client_settings`, `token_settings`)
VALUES ('2866656a-514d-4f02-88f0-1e31f45d25a6', 'messaging-client', '2023-01-15 13:17:34', '{noop}secret', NULL,
        '2866656a-514d-4f02-88f0-1e31f45d25a6', 'client_secret_basic',
        'refresh_token,client_credentials,authorization_code', 'http://127.0.0.1:8000/token/login',
        'openid,profile,message.read,message.write',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');
```

```shell
# 修改为统一推送
vim .git/config
# 远程url添加到 [remote "origin"] 下 url 
# 删除旧url

# 提交代码流程
git add .
git commit -m "提交信息"
git push origin main
```
