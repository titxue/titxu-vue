package com.titxu.cloud.management.auth.application.command;

import com.titxu.cloud.common.web.util.validator.group.AddGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 登录，手机号密码登陆Command
 */
@Data
@Schema(name = "密码登陆", description = "密码登陆")
public class LoginPasswordCommand {
    /**
     * 手机号
     */
    @Schema(name = "手机号")
    @NotBlank(message = "手机号不能为空", groups = AddGroup.class)
    private String mobile;

    /**
     * 密码
     */
    @Schema(name = "密码")
    @NotBlank(message = "密码不能为空", groups = AddGroup.class)
    private String password;

    /**
     * 刷新token
     */
    @Schema(name = "刷新token")
    private String refreshToken;

    /**
     * 验证码uuid
     */
    @Schema(name = "验证码uuid")
    @NotBlank(message = "验证码uuid不能为空", groups = AddGroup.class)
    private String uuid;

    /**
     * 验证码
     */
    @Schema(name = "验证码")
    @NotBlank(message = "验证码不能为空", groups = AddGroup.class)
    private String code;
}
