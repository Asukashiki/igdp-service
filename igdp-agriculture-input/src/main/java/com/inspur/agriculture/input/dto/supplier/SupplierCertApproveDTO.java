package com.inspur.agriculture.input.dto.supplier;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 供应商认证审批DTO
 *
 * @author igdp
 */
@Data
public class SupplierCertApproveDTO {

    /** 审批人ID */
    @NotNull(message = "审批人ID不能为空")
    private Long approverId;

    /** 审核结果(1-通过/0-驳回) */
    @NotNull(message = "审核结果不能为空")
    private Integer auditResult;

    /** 审核意见/驳回原因 */
    @NotBlank(message = "审核意见不能为空")
    private String auditOpinion;
}
