package com.inspur.seed.dto.registration;

import lombok.Data;

import java.io.Serializable;

/**
 * 繁殖机构审核操作的请求体 DTO
 *
 * @author igdp
 */
@Data
public class BreedingAuditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 注册申请ID
     */
    private String registrationId;

    /**
     * 审核结果：1-通过, 2-驳回
     */
    private Integer auditResult;

    /**
     * 审核意见/驳回原因
     */
    private String auditComment;
}
