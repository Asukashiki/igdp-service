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

    /** 部门编码(用于权限过滤) */
    private String organCode;

    /** 页码 */
    private Integer page;

    /** 每页数量 */
    private Integer pageSize;
}
