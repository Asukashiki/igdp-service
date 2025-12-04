package com.inspur.agriculture.input.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 出库单 VO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class OutboundOrderVO {

    /** 数据ID */
    private String id;

    /** 出库单ID */
    private String outboundOrderId;

    /** 出库批次号 */
    private String outboundBatchId;

    /** 出库状态 */
    private String outboundStatus;

    /** 出库类型：1-销售出库 2-调拨出库 */
    private Integer outboundType;

    /** 出库类型名称 */
    private String outboundTypeName;

    /** 出库仓库ID */
    private String warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 关联单号 */
    private String relatedOrderNo;

    /** 出库对象ID */
    private String outboundObjectId;

    /** 客户名称 */
    private String customerName;

    /** 出库员 */
    private String outboundUser;

    /** 出库部门 */
    private String outboundDept;

    /** 出库总数量 */
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

    /** 出库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outboundTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 备注 */
    private String remark;

    /** 出库明细列表 */
    private List<OutboundOrderDetailVO> details;
}
