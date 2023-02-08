package com.titxu.cloud.sys.application.command;

import com.titxu.cloud.common.web.util.validator.group.AddGroup;
import com.titxu.cloud.common.web.util.validator.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 权限Command
 **/
@Data
@Schema(name = "权限", description = "权限")
public class PermissionCommand {

    /**
     * id
     */
    @Schema(name = "ID")
    @NotBlank(message = "ID不能为空", groups = UpdateGroup.class)
    private String id;

    /**
     * 父级ID
     */
    @Schema(name = "父级ID")
    @NotBlank(message = "父级ID不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String parentId;

    /**
     * 权限名称
     */
    @Schema(name = "权限名称")
    @NotBlank(message = "权限名称不能为空", groups = {AddGroup.class})
    private String permissionName;

    /**
     * 权限类型
     */
    @Schema(name = "权限类型")
    @NotBlank(message = "权限类型不能为空", groups = {AddGroup.class})
    private String permissionType;

    /**
     * 权限级别
     */
    @Schema(name = "权限级别")
    @NotBlank(message = "权限级别不能为空", groups = {AddGroup.class})
    private String permissionLevel;

    /**
     * 权限编码
     */
    @Schema(name = "权限编码")
    private String permissionCodes;

    /**
     * 菜单图标
     */
    @Schema(name = "菜单图标")
    private String menuIcon;

    /**
     * 排序
     */
    @Schema(name = "排序")
    private int orderNum;

    /**
     * 菜单url
     */
    @Schema(name = "菜单url")
    private String menuUrl;

    /**
     * 状态
     */
    @Schema(name = "状态")
    private String status;
}
