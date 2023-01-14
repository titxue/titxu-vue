package com.titxu.cloud.management.auth.application.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 刷新登陆Command
 */
@Data
@AllArgsConstructor
@Schema(name = "刷新登陆", description = "刷新登陆")
public class RefreshCommand {


    /**
     * 刷新token
     */
    @Schema(name = "刷新token")
    private String refreshToken;

}
