package com.inspur.agriculture.input.dto.inventory;

import lombok.Data;

/**
 * 入库单查询 DTO
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
public class StockInQueryDTO {

    /** 入库单号(模糊查询) */
    private String stockInId;

    /** 仓库ID */
    private Long warehouseId;

    /** 供应商ID */
    private Long supplierId;

    /** 入库类型 */
    private String type;

    /** 状态 */
    private String status;

    /** 批次号(模糊查询) */
    private String batchNo;

    /** 创建开始时间 */
    private String createTimeStart;

    /** 创建结束时间 */
    private String createTimeEnd;
}
