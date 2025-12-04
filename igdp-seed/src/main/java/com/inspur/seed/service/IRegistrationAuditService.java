package com.inspur.seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.registration.AuditApproveDTO;
import com.inspur.seed.domain.dto.registration.AuditRejectDTO;
import com.inspur.seed.domain.dto.registration.EnterprisePageDTO;
import com.inspur.seed.domain.entity.OrgRegistrationAudit;
import com.inspur.seed.domain.vo.registration.AuditRecordVO;
import com.inspur.seed.domain.vo.registration.EnterprisePageVO;

import java.util.List;

/**
 * 机构注册审核服务接口
 *
 * @author system
 */
public interface IRegistrationAuditService extends IService<OrgRegistrationAudit> {

    /**
     * 分页查询待审核列表
     *
     * @param dto 查询DTO
     * @return 分页结果
     */
    IPage<EnterprisePageVO> pageAuditList(EnterprisePageDTO dto);

    /**
     * 审核通过
     *
     * @param dto 审核通过DTO
     */
    void approve(AuditApproveDTO dto);

    /**
     * 审核驳回
     *
     * @param dto 审核驳回DTO
     */
    void reject(AuditRejectDTO dto);

    /**
     * 查询审核记录列表
     *
     * @param enterpriseId 机构ID
     * @return 审核记录列表
     */
    List<AuditRecordVO> listAuditRecords(String enterpriseId);
}
