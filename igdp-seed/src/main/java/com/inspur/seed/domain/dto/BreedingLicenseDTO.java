package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 育种许可数据录入DTO
 *
 * @author system
 * @since 2025-01-30
 */
@Data
public class BreedingLicenseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID(编辑时必填)
     */
    private String id;

    /**
     * 育种批次ID
     */
    @NotBlank(message = "育种批次ID不能为空")
    private String batchId;

    /**
     * 育种批次名称(冗余字段,从数据集带过来)
     */
    private String batchName;

    /**
     * 数据集ID
     */
    @NotBlank(message = "数据集ID不能为空")
    private String datasetId;

    /**
     * 数据集编号(冗余字段,从数据集带过来)
     */
    private String datasetCode;

    /**
     * 作物类型(冗余字段,从数据集带过来)
     */
    private String cropType;

    /**
     * 品种名称(冗余字段,从数据集带过来)
     */
    private String varietyName;

    /**
     * 许可证号
     */
    @NotBlank(message = "许可证号不能为空")
    private String licenseNo;

    /**
     * 审批机构
     */
    @NotBlank(message = "审批机构不能为空")
    private String approvalOrg;

    /**
     * 批准日期
     */
    @NotNull(message = "批准日期不能为空")
    private String approvalDate;

    /**
     * 有效期开始日期
     */
    private String validStartDate;

    /**
     * 有效期结束日期
     */
    private String validEndDate;

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

    // ============ 物种特性 ============

    /**
     * 物种特性ID(编辑时使用)
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
     * 抗病性(JSON字符串)
     */
    private String diseaseResistance;

    /**
     * 压力耐受性(JSON字符串)
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
     * 其他特性(JSON字符串)
     */
    private String otherTraits;
}
