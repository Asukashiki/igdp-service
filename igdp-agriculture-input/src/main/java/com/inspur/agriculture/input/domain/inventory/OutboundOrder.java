package com.inspur.agriculture.input.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 出库单表
 *
 * @author igdp
 */
@Data
@TableName("outbound_order")
public class OutboundOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 出库单ID
     */
    private String outboundOrderId;

    /**
     * 出库批次号
     */
    private String outboundBatchId;

    /**
     * 出库状态(pending/completed/cancelled)
     */
    private String outboundStatus;

    /**
     * 出库类型(1:销售出库 2:调拨出库)
     */
    private Integer outboundType;

    /**
     * 出库仓库ID
     */
    private String warehouseId;

    /**
     * 关联单号
     */
    private String relatedOrderNo;

    /**
     * 出库对象ID
     */
    private String outboundObjectId;

    /**
     * 出库对象名称
     */
    private String outboundObjectName;

    /**
     * 出库员
     */
    private String outboundUser;

    /**
     * 出库部门
     */
    private String outboundDept;

    /**
     * 出库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outboundTime;

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
}
