package com.inspur.agriculture.input.dto.inventory;

import lombok.Data;

/**
 * 出库单查询 DTO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class OutboundOrderQueryDTO {

    /** 出库状态：all-全部/pending-待出库/completed-已出库/cancelled-已取消 */
    private String outboundStatus;

    /** 出库类型：all-全部/1-销售出库/2-调拨出库 */
    private String outboundType;

    /** 出库单ID */
    private String outboundOrderId;

    /** 关联单号 */
    private String relatedOrderNo;

    /** 部门编码(用于权限过滤) */
    private String organCode;

    /** 页码 */
    private Integer page;

    /** 每页数量 */
    private Integer pageSize;
}
