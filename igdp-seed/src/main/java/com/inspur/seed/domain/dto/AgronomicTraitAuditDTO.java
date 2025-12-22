package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 农艺性状审核操作DTO
 */
@Data
public class AgronomicTraitAuditDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "农艺性状ID不能为空")
    private String traitId;

    @NotBlank(message = "审核状态不能为空")
    private String auditStatus;

    private String auditOpinion;

    private Integer lockedFlag;

    private String auditNode;

    private Integer auditOrder;
}