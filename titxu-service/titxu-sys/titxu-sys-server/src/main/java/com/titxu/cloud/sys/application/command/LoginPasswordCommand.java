package com.titxu.cloud.sys.application.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import com.titxu.cloud.common.web.util.validator.group.AddGroup;

/**
 * 登录，手机号密码登陆Command
 */
@Data
@ApiModel(value = "密码登陆", description = "密码登陆")
public class LoginPasswordCommand {
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空", groups = AddGroup.class)
    private String mobile;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空", groups = AddGroup.class)
    private String password;

    /**
     * 验证码uuid
     */
    @ApiModelProperty(value = "验证码uuid")
    @NotBlank(message = "验证码uuid不能为空", groups = AddGroup.class)
    private String uuid;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空", groups = AddGroup.class)
    private String code;
}
