package com.titxu.cloud.sys.application.command;

import com.titxu.cloud.common.web.util.validator.group.AddGroup;
import com.titxu.cloud.common.web.util.validator.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 用户Command
 **/
@Data
@Schema(name = "用户", description = "用户")
public class UserCommand {

    /**
     * id
     */
    @Schema(name = "用户id")
    @NotBlank(message = "用户id不能为空", groups = UpdateGroup.class)
    private String id;

    /**
     * 用户名
     */
    @Schema(name = "用户名")
    @NotBlank(message = "用户名不能为空", groups = AddGroup.class)
    private String userNick;

    /**
     * 手机号
     */
    @Schema(name = "手机号")
    @NotBlank(message = "手机号不能为空", groups = AddGroup.class)
    private String mobile;

    /**
     * 邮箱
     */
    @Schema(name = "邮箱")
//    @NotBlank(message = "邮箱不能为空", groups = AddGroup.class)
    private String email;

    /**
     * 角色列表
     */
    @Schema(name = "角色列表")
    private List<String> roleIdList;

    /**
     * 状态
     */
    @Schema(name = "状态")
    private String status;
    /**
     * 签名信息
     */
    @Schema(name = "签名信息")
    private String remarks;
}
