package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

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
     * 备注
     */
    private String remark;
}
