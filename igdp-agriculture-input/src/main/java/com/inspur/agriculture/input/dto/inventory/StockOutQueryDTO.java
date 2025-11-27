package com.inspur.agriculture.input.dto.inventory;

import lombok.Data;

/**
 * 出库单查询 DTO
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
public class StockOutQueryDTO {

    /** 出库单号(模糊查询) */
    private String stockOutId;

    /** 仓库ID */
    private Long warehouseId;

    /** 出库类型 */
    private String type;

    /** 状态 */
    private String status;

    /** 批次号(模糊查询) */
    private String batchNo;

    /** 客户(模糊查询) */
    private String customer;

    /** 创建开始时间 */
    private String createTimeStart;

    /** 创建结束时间 */
    private String createTimeEnd;
}
