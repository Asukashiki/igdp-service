package com.inspur.agriculture.input.dto.inventory;

import lombok.Data;

/**
 * 库存查询 DTO
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
public class InventoryQueryDTO {

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库名称(模糊查询) */
    private String warehouseName;

    /** 投入品ID */
    private Long inputId;

    /** 投入品名称(模糊查询) */
    private String inputName;

    /** 批次号(模糊查询) */
    private String batchNo;

    /** 库存状态 */
    private String stockStatus;

    /** 最小库存量 */
    private Integer quantityMin;

    /** 最大库存量 */
    private Integer quantityMax;
}
