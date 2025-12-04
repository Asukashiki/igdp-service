package com.inspur.agriculture.input.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存变动日志表
 *
 * @author igdp
 */
@Data
@TableName("stock_log")
public class StockLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 仓库ID
     */
    private String warehouseId;

    /**
     * 投入品ID
     */
    private String materialId;

    /**
     * 投入品批次ID
     */
    private String materialBatchId;

    /**
     * 操作类型(inbound/outbound)
     */
    private String operationType;

    /**
     * 变动数量
     */
    private BigDecimal changeQuantity;

    /**
     * 变动前数量
     */
    private BigDecimal beforeQuantity;

    /**
     * 变动后数量
     */
    private BigDecimal afterQuantity;

    /**
     * 关联单号
     */
    private String referenceOrderId;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
