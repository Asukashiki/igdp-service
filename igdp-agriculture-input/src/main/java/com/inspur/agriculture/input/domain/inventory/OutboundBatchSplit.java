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
 * 出库批次拆分表
 *
 * @author igdp
 */
@Data
@TableName("outbound_batch_split")
public class OutboundBatchSplit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 拆分记录ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 出库明细ID
     */
    private String outboundDetailId;

    /**
     * 原入库批次号
     */
    private String inboundBatchId;

    /**
     * 拆分出库数量
     */
    private BigDecimal splitQuantity;

    /**
     * 该批次剩余库存
     */
    private BigDecimal remainingQuantity;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
