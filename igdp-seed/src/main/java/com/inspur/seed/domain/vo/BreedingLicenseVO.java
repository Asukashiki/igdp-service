package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 育种许可信息VO
 *
 * @author system
 * @since 2025-01-30
 */
@Data
public class BreedingLicenseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 育种批次ID
     */
    private String batchId;

    /**
     * 育种批次名称
     */
    private String batchName;

    /**
     * 数据集ID
     */
    private String datasetId;

    /**
     * 数据集编号
     */
    private String datasetCode;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 许可证号
     */
    private String licenseNo;

    /**
     * 审批机构
     */
    private String approvalOrg;

    /**
     * 批准日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate approvalDate;

    /**
     * 有效期开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate validStartDate;

    /**
     * 有效期结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate validEndDate;

    /**
     * 认证文件路径
     */
    private String certificateFile;

    /**
     * 许可状态
     */
    private String licenseStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;

    /**
     * 创建人姓名
     */
    private String createdByName;

    // ============ 物种特性 ============

    /**
     * 物种特性ID
     */
    private String traitsId;

    /**
     * 最小产量潜力
     */
    private BigDecimal minYieldPotential;

    /**
     * 最大产量潜力
     */
    private BigDecimal maxYieldPotential;

    /**
     * 抗病性(JSON)
     */
    private String diseaseResistance;

    /**
     * 压力耐受性(JSON)
     */
    private String stressTolerance;

    /**
     * 成熟期(天)
     */
    private Integer maturityDays;

    /**
     * 株高(cm)
     */
    private BigDecimal plantHeight;

    /**
     * 谷物品质性状
     */
    private String grainQualityTraits;

    /**
     * 其他特性(JSON)
     */
    private String otherTraits;
}
