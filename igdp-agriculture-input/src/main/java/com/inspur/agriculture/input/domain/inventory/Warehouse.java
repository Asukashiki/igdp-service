package com.inspur.agriculture.input.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 仓库表
 *
 * @author inspur
 * @date 2025-11-26
 */

@Data
@TableName("inv_warehouse")
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 仓库ID */
    @TableId(type = IdType.AUTO)
    private Long warehouseId;

    /** 仓库编号 */
    private String warehouseCode;

    /** 仓库名称 */
    private String warehouseName;

    /** 仓库类型: normal-普通仓库/cold-冷藏仓库/dangerous-危险品仓库 */
    private String warehouseType;

    /** 仓库位置 */
    private String location;

    /** 仓库容量 */
    private BigDecimal capacity;

    /** 已用容量 */
    private BigDecimal usedCapacity;


    /** 状态: 0-停用/1-启用 */
    private String status;

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

    /** 删除标志: 0-正常/2-删除 */
    private String delFlag;

    /** 仓库类型描述(非数据库字段) */
    @TableField(exist = false)
    private String warehouseTypeDesc;

    /** 可用容量(非数据库字段) */
    @TableField(exist = false)
    private BigDecimal availableCapacity;

    /** 状态描述(非数据库字段) */
    @TableField(exist = false)
    private String statusDesc;

    private String organName;

    private String organCode;

    private BigDecimal warehouseArea;

    private String siteCertificate;
}