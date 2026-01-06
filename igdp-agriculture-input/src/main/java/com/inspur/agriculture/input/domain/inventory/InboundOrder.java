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
 * 入库单表
 *
 * @author igdp
 */
@Data
@TableName("inbound_order")
public class InboundOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 入库单ID
     */
    private String inboundOrderId;

    /**
     * 入库批次号
     */
    private String inboundBatchId;

    /**
     * 入库状态(pending/completed/cancelled)
     */
    private String inboundStatus;

    /**
     * 入库类型(0:生产入库 1:采购入库 2:调拨入库)
     */
    private Integer inboundType;

    /**
     * 入库仓库ID
     */
    private String warehouseId;

    /**
     * 关联单号
     */
    private String relatedOrderNo;

    /**
     * 供应商类型
     */
    private String supplierType;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商联系人
     */
    private String supplierContact;

    /**
     * 供应商电话
     */
    private String supplierPhone;

    /**
     * 入库员
     */
    private String inboundUser;

    /**
     * 入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inboundTime;

    /**
     * 入库总数量
     */
    private BigDecimal totalQuantity;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /**
     * 审核人
     */
    private String auditUser;

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
    
    /**
     * 表单备注
     */
    private String formRemark;

}
