package com.inspur.seed.Institution.multiplication.domain.vo;

import com.inspur.seed.Institution.multiplication.domain.entity.BreedingAuditLog;
import com.inspur.seed.Institution.multiplication.domain.entity.BreedingOrgRegistration;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 注册申请详情 VO（含审核历史）
 *
 * @author igdp
 */
@Data
public class BreedingOrgRegistrationDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 基础信息
     */
    private BreedingOrgRegistration baseInfo;

    /**
     * 审核历史列表
     */
    private List<BreedingAuditLog> auditLogs;
}
