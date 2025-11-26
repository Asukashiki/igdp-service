package com.inspur.agriculture.input.domain.inventory;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 出库商品明细表
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
@TableName("inv_stock_out_item")
public class StockOutItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 出库商品明细ID */
    @TableId
    private String stockOutItemId;

    /** 出库单号 */
    private String stockOutId;

    /** 投入品ID */
    private Long inputId;

    /** 出库仓库ID */
    private Long warehouseId;

    /** 批次号 */
    private String batchNo;

    /** 出库数量 */
    private Integer quantity;

    /** 备注 */
    private String remarks;

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

    /** 仓库名称(非数据库字段) */
    @TableField(exist = false)
    private String warehouseName;
}
