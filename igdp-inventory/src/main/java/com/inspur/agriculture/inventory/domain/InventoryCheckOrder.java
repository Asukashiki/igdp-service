package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 盘点单主表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_check_order")
public class InventoryCheckOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String checkNo;
    private String warehouseCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDate;
    private String checkBy;
    private String status;
    private String auditBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;
    private String auditComment;
    private String remark;

    /** 仓库名称（非数据库字段，查询时通过JOIN获取） */
    @TableField(exist = false)
    private String warehouseName;

    /** 明细列表 */
    @TableField(exist = false)
    private List<InventoryCheckOrderDetail> detailList;
}
