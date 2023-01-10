package com.titxu.cloud.ops.auth.application.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 刷新登陆Command
 */
@Data
@AllArgsConstructor
@ApiModel(value = "刷新登陆", description = "刷新登陆")
public class RefreshCommand {


    /**
     * 刷新token
     */
    @ApiModelProperty(value = "刷新token")
    private String refreshToken;

}
