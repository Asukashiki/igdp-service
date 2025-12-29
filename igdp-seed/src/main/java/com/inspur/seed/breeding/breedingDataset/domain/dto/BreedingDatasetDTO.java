package com.inspur.seed.breeding.breedingDataset.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 育种数据集DTO
 *
 * @author system
 * @date 2025-01-30
 */
@Data
public class BreedingDatasetDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 试验ID
     */
    @NotBlank(message = "试验ID不能为空")
    private String trialId;

    /**
     * 各模块行级备注（JSON字符串）
     */
    private String moduleRowRemarks;

    /**
     * 育种批次ID
     */
    @NotBlank(message = "育种批次ID不能为空")
    private String batchId;

    /**
     * 育种批次名称
     */
    private String batchName;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 版本号（同一试验下每次提交自动递增）
     */
    private Integer versionNo;

    /**
     * 编制人
     */
    @NotBlank(message = "编制人不能为空")
    private String compiledBy;

    /**
     * 编制人姓名
     */
    private String compiledByName;

    /**
     * 编制时间
     */
    @NotNull(message = "编制时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime compiledAt;

    /**
     * 记录数量
     */
    private Integer recordCount;

    /**
     * 数据集状态
     */
    private String datasetStatus;

    /**
     * 备注
     */
    private String remark;
}
