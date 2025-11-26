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
 * 出库单表
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
@TableName("inv_stock_out")
public class StockOut implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 出库单号 */
    @TableId
    private String stockOutId;

    /** 出库仓库ID */
    private Long warehouseId;

    /** 批次号 */
    private String batchNo;

    /** 经办人 */
    private String operator;

    /** 出库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outTime;

    /** 客户 */
    private String customer;

    /** 出库类型: 0-销售出库 */
    private String type;

    /** 状态: 0-未出库/1-已出库/2-作废 */
    private String status;

    /** 总数量 */
    private Integer totalQuantity;

    /** 备注 */
    private String remark;

    /** 作废原因 */
    private String cancelReason;

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

    /** 类型描述(非数据库字段) */
    @TableField(exist = false)
    private String typeDesc;

    /** 状态描述(非数据库字段) */
    @TableField(exist = false)
    private String statusDesc;

    /** 出库商品明细列表(非数据库字段) */
    @TableField(exist = false)
    private List<StockOutItem> items;
}
