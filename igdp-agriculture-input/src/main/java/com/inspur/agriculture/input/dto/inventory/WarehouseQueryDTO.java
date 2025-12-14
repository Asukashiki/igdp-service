package com.inspur.agriculture.input.dto.inventory;

import lombok.Data;

/**
 * 仓库查询 DTO
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
public class WarehouseQueryDTO {

    /** 仓库名称(模糊查询) */
    private String warehouseName;

    /** 仓库编号(模糊查询) */
    private String warehouseCode;

    /** 仓库类型 */
    private String warehouseType;

    /** 关联供应商ID */
    private Long supplierId;

    /** 状态 */
    private String status;

    /** 关键词搜索 */
    private String keyword;

    /** 部门编码(用于权限过滤) */
    private String organCode;
}
