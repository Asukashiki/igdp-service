package com.inspur.seed.service;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.AgronomicTraitAuditDTO;
import com.inspur.seed.domain.dto.AgronomicTraitAuditQueryDTO;

/**
 * 农艺性状审核Service
 */
public interface IAgronomicTraitAuditService {

    AjaxResult getAuditList(AgronomicTraitAuditQueryDTO queryDTO);

    AjaxResult getAuditById(String id);

    AjaxResult getAuditByTraitId(String traitId);

    AjaxResult performAudit(AgronomicTraitAuditDTO auditDTO);

    AjaxResult getAuditHistory(String traitId);
}