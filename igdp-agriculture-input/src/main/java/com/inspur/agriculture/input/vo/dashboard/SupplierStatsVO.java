package com.inspur.agriculture.input.vo.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应商统计VO
 *
 * @author igdp
 */
@Data
public class SupplierStatsVO {

    /** 供应商ID */
    private String userId;

    /** 供应商名称 */
    private String orgName;

    /** 认证状态 */
    private Integer status;

    /** 认证状态描述 */
    private String statusDesc;

    /** 供应投入品数量 */
    private Long productCount;

    /** 本月入库次数 */
    private Long monthStockInCount;

    /** 本月入库总量 */
    private Long monthStockInQuantity;

    /** 合作时长(天) */
    private Long cooperationDays;

    /** 质量评级 */
    private String qualityRating;

    /** 联系人 */
    private String contactName;

    /** 联系电话 */
    private String contactPhone;

    /** 认证时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date approveTime;
}
