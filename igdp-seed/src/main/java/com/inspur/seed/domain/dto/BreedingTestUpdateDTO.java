package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖检测信息修改DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingTestUpdateDTO {

    /**
     * 主键ID
     */
    @NotBlank(message = "主键ID不能为空")
    private String id;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 发芽率
     */
    @DecimalMin(value = "0", message = "发芽率最小值为0")
    @DecimalMax(value = "100", message = "发芽率最大值为100")
    private BigDecimal germinationRate;

    /**
     * 纯度
     */
    @DecimalMin(value = "0", message = "纯度最小值为0")
    @DecimalMax(value = "100", message = "纯度最大值为100")
    private BigDecimal purity;

    /**
     * 水分含量
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
     * 检测结论
     */
    private String testResult;

    /**
     * 检测报告附件路径
     */
    private String testReportUrl;

    /**
     * 备注
     */
    private String remark;
}
