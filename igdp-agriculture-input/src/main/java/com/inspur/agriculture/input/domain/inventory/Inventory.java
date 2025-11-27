package com.inspur.agriculture.input.domain.inventory;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 库存表
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
@TableName("inv_inventory")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 库存记录ID */
    @TableId
    private String inventoryId;

    /** 投入品ID */
    private Long inputId;

    /** 批次号 */
    private String batchNo;

    /** 仓库ID */
    private Long warehouseId;

    /** 当前库存数量 */
    private Integer currentQuantity;

    /** 入库日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inDate;

    /** 过期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiredDate;

    /** 库存状态: 0-正常/1-临期/2-过期 */
    private String stockStatus;

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

    /** 投入品名称(非数据库字段) */
    @TableField(exist = false)
    private String inputName;

    /** 投入品SKU(非数据库字段) */
    @TableField(exist = false)
    private String inputSku;

    /** 投入品类型(非数据库字段) */
    @TableField(exist = false)
    private String inputType;

    /** 投入品类型描述(非数据库字段) */
    @TableField(exist = false)
    private String inputTypeDesc;

    /** 仓库名称(非数据库字段) */
    @TableField(exist = false)
    private String warehouseName;

    /** 库存状态描述(非数据库字段) */
    @TableField(exist = false)
    private String stockStatusDesc;

    /** 距离过期天数(非数据库字段) */
    @TableField(exist = false)
    private Integer daysToExpire;
}
