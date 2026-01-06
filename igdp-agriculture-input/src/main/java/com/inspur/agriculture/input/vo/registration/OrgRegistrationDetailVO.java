package com.inspur.agriculture.input.vo.registration;

import com.inspur.agriculture.input.domain.registration.AuditLog;
import com.inspur.agriculture.input.domain.registration.OrgRegistration;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 注册申请详情 VO（含审核历史）
 *
 * @author igdp
 */
@Data
public class OrgRegistrationDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 基础信息
     */
    private OrgRegistration baseInfo;

    /**
     * 审核历史列表
     */
    private List<AuditLog> auditLogs;
}
