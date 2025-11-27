package com.inspur.agriculture.input.vo.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 今日出入库VO
 *
 * @author igdp
 */
@Data
public class TodayStockVO {

    /** 单据ID */
    private String billId;

    /** 单据类型: in-入库/out-出库 */
    private String billType;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 批次号 */
    private String batchNo;

    /** 经办人 */
    private String operator;

    /** 供应商ID(入库) */
    private Long supplierId;

    /** 供应商名称(入库) */
    private String supplierName;

    /** 客户(出库) */
    private String customer;

    /** 状态: 0-未入库/1-已入库/2-作废 */
    private String status;

    /** 状态描述 */
    private String statusDesc;

    /** 总数量 */
    private Integer totalQuantity;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;

    /** 备注 */
    private String remarks;
}
