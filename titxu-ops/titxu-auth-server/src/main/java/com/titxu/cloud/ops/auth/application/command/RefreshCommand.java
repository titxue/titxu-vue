package com.titxu.cloud.ops.auth.application.command;

import com.titxu.cloud.common.web.util.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 刷新登陆Command
 */
@Data
@ApiModel(value = "刷新登陆", description = "刷新登陆")
public class RefreshCommand {


    /**
     * 刷新token
     */
    @ApiModelProperty(value = "刷新token")
    private String refreshToken;

}
