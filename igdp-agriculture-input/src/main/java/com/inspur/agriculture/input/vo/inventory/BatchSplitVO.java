package com.inspur.agriculture.input.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 批次拆分 VO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class BatchSplitVO {

    /** 拆分记录ID */
    private String id;

    /** 出库明细ID */
    private String outboundDetailId;

    /** 原入库批次号 */
    private String inboundBatchId;

    /** 拆分出库数量 */
    private BigDecimal splitQuantity;

    /** 该批次剩余库存 */
    private BigDecimal remainingQuantity;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
