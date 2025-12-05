package com.inspur.agriculture.input.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 出库明细 VO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class OutboundOrderDetailVO {

    /** 数据ID */
    private String id;

    /** 出库明细ID */
    private String detailId;

    /** 出库单ID */
    private String outboundOrderId;

    /** 投入品ID */
    private String materialId;

    /** 投入品名称 */
    private String materialName;

    /** 投入品批次ID */
    private String materialBatchId;

    /** 投入品类型 */
    private String materialType;

    /** 规格型号 */
    private String specModel;

    /** 计量单位 */
    private String unitOfMeasure;

    /** 数量 */
    private BigDecimal quantity;

    /** 出库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outboundTime;

    /** 经办人 */
    private String operator;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 备注 */
    private String remark;

    /** 批次拆分列表 */
    private List<BatchSplitVO> batchSplits;
}
