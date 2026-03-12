package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.annotation.Excel;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存盘点明细对象 t_stock_check
 * 单表扁平化设计
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_stock_check", excludeProperty = {"createBy","updateBy"})
public class StockCheck extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 记录ID（主键） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 盘点单编号，格式：PD+yyyyMMdd+4位流水号 */
    @Excel(name = "盘点单编号")
    private String checkId;

    /** 盘点日期（实际执行日期） */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "盘点日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkDate;

    /** 盘点仓库ID */
    @Excel(name = "盘点仓库ID")
    private String warehouseId;

    /** 盘点仓库名称 */
    @Excel(name = "盘点仓库名称")
    private String warehouseName;

    /** 盘点人ID */
    @Excel(name = "盘点人ID")
    private String checkerId;

    /** 盘点人姓名 */
    @Excel(name = "盘点人姓名")
    private String checkerName;

    /** 盘点状态：DRAFT/PENDING/APPROVED/REJECTED/ADJUSTED/CANCELLED */
    @Excel(name = "盘点状态")
    private String checkStatus;

    /** 整张盘点单的总体说明 */
    @Excel(name = "盘点单总体说明")
    private String checkRemark;

    /** 商品ID */
    @Excel(name = "商品ID")
    private String productId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String productName;

    /** 商品大类：化肥/种子/农产品/农药等 */
    @Excel(name = "商品大类")
    private String categoryMajor;

    /** 商品小类：氮肥/玉米等 */
    @Excel(name = "商品小类")
    private String categoryMinor;

    /** 商品生产批次 */
    @Excel(name = "批次号")
    private String batchNo;

    /** 计量单位 */
    @Excel(name = "单位")
    private String unit;

    /** 有效期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiryDate;

    /** 商品状态：AVAILABLE/RESERVED/DAMAGED */
    @Excel(name = "商品状态")
    private String qualityStatus;

    /** 盘点时系统库存数量（自动读取，只读） */
    @Excel(name = "系统库存数量")
    private BigDecimal systemQty;

    /** 实盘数量（盘点人填写） */
    @Excel(name = "实盘数量")
    private BigDecimal actualQty;

    /** 差异类型：SURPLUS/LOSS/NONE（系统计算） */
    @Excel(name = "差异类型")
    private String diffType;

    /** 差异数量 = actual_qty - system_qty（系统计算） */
    @Excel(name = "差异数量")
    private BigDecimal diffQty;

    /** 单条商品盘点说明 */
    @Excel(name = "商品盘点说明")
    private String itemRemark;

    /** 审核人ID */
    @Excel(name = "审核人ID")
    private String reviewerId;

    /** 审核人姓名 */
    @Excel(name = "审核人姓名")
    private String reviewerName;

    /** 审核日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reviewDate;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String reviewOpinion;
}
