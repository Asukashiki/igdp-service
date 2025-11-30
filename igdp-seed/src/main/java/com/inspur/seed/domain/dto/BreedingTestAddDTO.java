package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖检测信息新增DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingTestAddDTO {

    /**
     * 关联跟踪编号
     */
    @NotBlank(message = "跟踪编号不能为空")
    private String trackingId;

    /**
     * 关联繁殖批次编号
     */
    private String batchId;

    /**
     * 作物种类（枚举值）
     */
    @NotBlank(message = "作物种类不能为空")
    private String cropType;

    /**
     * 发芽率（%）
     */
    @DecimalMin(value = "0", message = "发芽率最小值为0")
    @DecimalMax(value = "100", message = "发芽率最大值为100")
    private BigDecimal germinationRate;

    /**
     * 纯度（%）
     */
    @DecimalMin(value = "0", message = "纯度最小值为0")
    @DecimalMax(value = "100", message = "纯度最大值为100")
    private BigDecimal purity;

    /**
     * 水分含量（%）
     */
    @DecimalMin(value = "0", message = "水分含量最小值为0")
    @DecimalMax(value = "100", message = "水分含量最大值为100")
    private BigDecimal moistureContent;

    /**
     * 病虫害检测结果
     */
    private String pestDetection;

    /**
     * 检测日期
     */
    @NotNull(message = "检测日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date testDate;

    /**
     * 检测机构
     */
    private String testOrg;

    /**
     * 检测人员
     */
    private String testPerson;

    /**
     * 检测结论（01/02/03）
     */
    private String testResult;

    /**
     * 检测报告附件路径
     */
    private String testReportUrl;

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
}
