package com.inspur.agriculture.input.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 仓库 VO
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
public class WarehouseVO {

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库编号 */
    private String warehouseCode;

    /** 仓库名称 */
    private String warehouseName;

    /** 仓库类型 */
    private String warehouseType;

    /** 仓库类型描述 */
    private String warehouseTypeDesc;

    /** 仓库位置 */
    private String location;

    /** 仓库容量 */
    private BigDecimal capacity;

    /** 已用容量 */
    private BigDecimal usedCapacity;

    /** 可用容量 */
    private BigDecimal availableCapacity;

    /** 拥有者 */
    private String belongs;

    /** 关联供应商ID */
    private String supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 状态 */
    private String status;

    /** 状态描述 */
    private String statusDesc;

    /** 联系人 */
    private String contactPerson;

    /** 联系电话 */
    private String contactPhone;

    /** 备注 */
    private String remark;

    /** 创建人 */
    private String createPeople;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改人 */
    private String updatePeople;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
