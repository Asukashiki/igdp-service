package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖批次信息VO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingBatchVO {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 繁殖批次编号
     */
    private String batchId;

    /**
     * 关联接收记录ID
     */
    private String receivedId;

    /**
     * 分发ID
     */
    private String distributionId;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 作物类型名称
     */
    private String cropTypeName;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 繁殖级别
     */
    private String breedingLevel;

    /**
     * 繁殖级别名称
     */
    private String breedingLevelName;

    /**
     * 亲本种子来源
     */
    private String parentSeedSource;

    /**
     * 育种目标
     */
    private String objective;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    /**
     * 批次状态
     */
    private String batchStatus;

    /**
     * 批次状态名称
     */
    private String batchStatusName;

    /**
     * 预期产量
     */
    private BigDecimal expectedYield;

    /**
     * 实际产量
     */
    private BigDecimal actualYield;

    /**
     * 操作机构ID
     */
    private String orgId;

    /**
     * 操作机构名称
     */
    private String orgName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 跟踪记录数量
     */
    private Integer trackingCount;

    /**
     * 检测记录数量
     */
    private Integer testCount;

    /**
     * 待扩繁数量
     */
    private BigDecimal toMultiplyQuantity;

}
