package com.titxu.cloud.sys.application.command;

import com.titxu.cloud.common.web.util.validator.group.AddGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 注册租户Command
 **/
@Data
@Schema(name = "注册租户", description = "注册租户")
public class RegisterTenantCommand {

    /**
     * 租户名
     */
    @Schema(name = "租户名")
    @NotBlank(message = "租户名不能为空")
    private String tenantName;

    /**
     * 租户编码
     */
    @Schema(name = "租户编码")
    @NotBlank(message = "租户编码不能为空")
    private String tenantCode;

    /**
     * 用户名
     */
    @Schema(name = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String userNick;

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
     * 验证码
     */
    @Schema(name = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    /**
     * uuid
     */
    @Schema(name = "uuid")
    @NotBlank(message = "uuid不能为空")
    private String uuid;
}
