package com.inspur.agriculture.input.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存日志 VO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class StockLogVO {

    /** 日志ID */
    private String id;

    /** 仓库ID */
    private String warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 投入品ID */
    private String materialId;

    /** 投入品名称 */
    private String materialName;

    /** 投入品批次ID */
    private String materialBatchId;

    /** 操作类型：inbound-入库 outbound-出库 */
    private String operationType;

    /** 变动数量 */
    private BigDecimal changeQuantity;

    /** 变动前数量 */
    private BigDecimal beforeQuantity;

    /** 变动后数量 */
    private BigDecimal afterQuantity;

    /** 关联单号 */
    private String referenceOrderId;

    /** 操作人 */
    private String operator;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
