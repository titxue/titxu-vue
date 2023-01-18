package com.titxu.cloud.management.auth.api;

import cn.hutool.json.JSONObject;
import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.core.util.WebUtils;
import com.titxu.cloud.common.redis.util.RedisService;
import com.titxu.cloud.common.web.util.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "注销")
@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
@Slf4j
public class LogoutController {

    private RedisService redisService;

    @PostMapping("/logout")
    public Result logout() {
        JSONObject jsonObject = WebUtils.getJwtPayload();
        String jti = jsonObject.getStr(AuthConstants.JWT_JTI);
        long exp = jsonObject.getLong(AuthConstants.JWT_EXP);

        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        if (exp < currentTimeSeconds) {
            return Result.ok();
        }
        redisService.set(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti, null, (exp - currentTimeSeconds));
        return Result.ok();
    }

}
