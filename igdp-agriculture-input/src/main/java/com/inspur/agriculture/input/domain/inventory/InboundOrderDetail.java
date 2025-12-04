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
 * 入库单明细表
 *
 * @author igdp
 */
@Data
@TableName("inbound_order_detail")
public class InboundOrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 入库明细ID
     */
    private String detailId;

    /**
     * 入库单ID
     */
    private String inboundOrderId;

    /**
     * 投入品ID
     */
    private String materialId;

    /**
     * 投入品批次ID
     */
    private String materialBatchId;

    /**
     * 投入品名称
     */
    private String materialName;
    /**
     * 投入品类型
     */
    private String materialType;

    /**
     * 规格型号
     */
    private String specModel;

    /**
     * 计量单位
     */
    private String unitOfMeasure;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inboundTime;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    /**
     * 二维码
     */
    private String qrCode;

    /**
     * 经办人
     */
    private String operator;

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

    /**
     * 备注
     */
    private String remark;
}
