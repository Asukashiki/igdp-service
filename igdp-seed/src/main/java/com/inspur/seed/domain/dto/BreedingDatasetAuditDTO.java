package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 育种数据集审核DTO
 *
 * @author system
 * @since 2025-01-30
 */
@Data
public class BreedingDatasetAuditDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据集ID
     */
    @NotBlank(message = "数据集ID不能为空")
    private String datasetId;

    /**
     * 审核状态:approved/rejected
     */
    @NotBlank(message = "审核状态不能为空")
    private String auditStatus;

    /**
     * 审核意见(驳回时必填)
     */
    private String auditOpinion;

    /**
     * 审核节点
     */
    private String auditNode;

    /**
     * 审核顺序
     */
    private Integer auditOrder;
}
