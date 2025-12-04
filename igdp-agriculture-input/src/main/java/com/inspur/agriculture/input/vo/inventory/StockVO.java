package com.inspur.agriculture.input.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存 VO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class StockVO {

    /** 库存ID */
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

    /** 库存数量 */
    private BigDecimal quantity;

    /** 入库数量 */
    private BigDecimal inboundQuantity;

    /** 出库数量 */
    private BigDecimal outboundQuantity;

    /** 过期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    /** 二维码 */
    private String qrCode;

    /** 状态 */
    private String status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
