package com.inspur.agriculture.input.dto.inventory;

import lombok.Data;

/**
 * 库存查询 DTO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class StockQueryDTO {

    /** 仓库ID */
    private String warehouseId;

    /** 投入品ID */
    private String materialId;

    /** 投入品批次ID */
    private String materialBatchId;

    /** 投入品类型 */
    private String materialType;

    /** 农资类型(投入品品类) */
    private String agriculturalInputType;

    /** 部门编码(用于权限过滤) */
    private String organCode;

    /** 最小库存数量(用于过滤库存为0的记录) */
    private Double minQuantity;

    /** 页码 */
    private Integer page;

    /** 每页数量 */
    private Integer pageSize;
}