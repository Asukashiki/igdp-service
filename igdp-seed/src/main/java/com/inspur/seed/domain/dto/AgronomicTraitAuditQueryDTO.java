package com.inspur.seed.domain.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 农艺性状审核查询DTO
 */
@Data
public class AgronomicTraitAuditQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNum;

    private Integer pageSize;

    private String auditStatus;

    private String batchId;

    private String trialId;

    private String plotId;

    private String growthStage;

    private String observationDateStart;

    private String observationDateEnd;

    private String traitName;

    private String workflowStatus;
}