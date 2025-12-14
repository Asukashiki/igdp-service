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
 * 库存表
 *
 * @author igdp
 */
@Data
@TableName("stock")
public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 库存ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 仓库ID
     */
    private String warehouseId;

    private String warehouseName;

    /**
     * 投入品ID
     */
    private String materialId;

    /**
     * 投入品批次ID
     */
    private String materialBatchId;
    
    private String materialName;

    /**
     * 投入品类型
     */
    private String materialType;
    
    /**
     * 农资类型(投入品品类)
     */
    private String agriculturalInputType;

    /**
     * 库存数量
     */
    private BigDecimal quantity;

    /**
     * 入库数量
     */
    private BigDecimal inboundQuantity;

    /**
     * 出库数量
     */
    private BigDecimal outboundQuantity;

    /**
     * 过期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    /**
     * 二维码
     */
    private String qrCode;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    
    // 为了兼容之前的查询结果，添加一些额外的字段
    private String batchNo;
    private String variety;
}