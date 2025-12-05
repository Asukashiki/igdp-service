package com.inspur.agriculture.input.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 入库单 VO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class InboundOrderVO {

    /** 数据ID */
    private String id;

    /** 入库单ID */
    private String inboundOrderId;

    /** 入库批次号 */
    private String inboundBatchId;

    /** 入库状态 */
    private String inboundStatus;

    /** 入库类型：0-生产入库 1-采购入库 2-调拨入库 */
    private Integer inboundType;

    /** 入库类型名称 */
    private String inboundTypeName;

    /** 入库仓库ID */
    private String warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 关联单号 */
    private String relatedOrderNo;

    /** 供应商类型 */
    private String supplierType;

    /** 供应商ID */
    private String supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 入库员 */
    private String inboundUser;

    /** 入库总数量 */
    private BigDecimal totalQuantity;

    /** 经办人 */
    private String operator;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /** 审核人 */
    private String auditUser;

    /** 入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inboundTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 备注 */
    private String remark;

    /** 入库明细列表 */
    private List<InboundOrderDetailVO> details;
}
