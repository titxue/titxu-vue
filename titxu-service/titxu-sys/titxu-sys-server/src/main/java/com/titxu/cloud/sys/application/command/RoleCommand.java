package com.titxu.cloud.sys.application.command;

import com.titxu.cloud.common.web.util.validator.group.AddGroup;
import com.titxu.cloud.common.web.util.validator.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 角色Command
 **/
@Data
@Schema(name = "角色", description = "角色")
public class RoleCommand {

    /**
     * id
     */
    @Schema(name = "角色id")
    @NotBlank(message = "角色id不能为空", groups = UpdateGroup.class)
    private String id;

    /**
     * 角色编码
     */
    @Schema(name = "角色编码")
    @NotBlank(message = "角色编码不能为空", groups = AddGroup.class)
    private String roleCode;

    /**
     * 角色名称
     */
    @Schema(name = "角色名称")
    @NotBlank(message = "角色名称不能为空", groups = AddGroup.class)
    private String roleName;

    /**
     * 备注
     */
    @Schema(name = "备注")
    private String remarks;

    /**
     * 状态
     */
    @Schema(name = "状态")
    private String status;

    /**
     * 权限
     */
    @Schema(name = "权限")
    private List<String> permissionIdList;
}
