package com.titxu.cloud.sys.application.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "分页查询", description = "分页查询")
public class PageCommand {
    @Schema(name = "当前页码")
    private String page;

    @Schema(name = "每页显示记录数")
    private String limit;


    @Schema(name = "排序字段")
    private String orderFiele;


    @Schema(name = "排序方式")
    private String order;

    @Schema(name = "升序")
    private String asc;

    @Schema
    private String key;

    @Schema
    private String value;
}
