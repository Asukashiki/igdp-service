package com.inspur.agriculture.input.domain.inventory;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 入库单表
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
@TableName("inv_stock_in")
public class StockIn implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 入库单号 */
    @TableId
    private String stockInId;

    /** 入库仓库ID */
    private Long warehouseId;

    /** 批次号 */
    private String batchNo;

    /** 经办人 */
    private String operator;

    /** 供应商ID */
    private Long supplierId;

    /** 入库类型: 0-采购入库/1-退货入库 */
    private String type;

    /** 状态: 0-未入库/1-已入库/2-作废 */
    private String status;

    /** 总数量 */
    private Integer totalQuantity;

    /** 二维码 */
    private String qrCode;

    /** 备注 */
    private String remarks;

    /** 作废原因 */
    private String cancelReason;

    /** 确认入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;

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

    /** 仓库名称(非数据库字段) */
    @TableField(exist = false)
    private String warehouseName;

    /** 供应商名称(非数据库字段) */
    @TableField(exist = false)
    private String supplierName;

    /** 类型描述(非数据库字段) */
    @TableField(exist = false)
    private String typeDesc;

    /** 状态描述(非数据库字段) */
    @TableField(exist = false)
    private String statusDesc;

    /** 入库商品明细列表(非数据库字段) */
    @TableField(exist = false)
    private List<StockInItem> items;
}
