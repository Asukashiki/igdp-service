package com.inspur.agriculture.input.dto.inventory;

import lombok.Data;

/**
 * 入库单查询 DTO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class InboundOrderQueryDTO {

    /** 入库状态：all-全部/pending-待入库/completed-已入库/cancelled-已取消 */
    private String inboundStatus;

    /** 入库类型：all-全部/0-生产入库/1-采购入库/2-调拨入库 */
    private String inboundType;

    /** 入库单ID */
    private String inboundOrderId;

    /** 页码 */
    private Integer page;

    /** 每页数量 */
    private Integer pageSize;
}
