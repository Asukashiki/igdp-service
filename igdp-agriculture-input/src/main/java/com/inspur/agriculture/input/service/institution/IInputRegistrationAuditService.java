package com.inspur.agriculture.input.service.institution;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.input.domain.institution.dto.InputAuditApproveDTO;
import com.inspur.agriculture.input.domain.institution.dto.InputAuditRejectDTO;
import com.inspur.agriculture.input.domain.institution.dto.InputEnterprisePageDTO;
import com.inspur.agriculture.input.domain.institution.entity.InputRegistrationAudit;
import com.inspur.agriculture.input.domain.institution.vo.InputAuditRecordVO;
import com.inspur.agriculture.input.domain.institution.vo.InputEnterprisePageVO;


import java.util.List;

/**
 * 机构注册审核服务接口
 *
 * @author system
 */
public interface IInputRegistrationAuditService extends IService<InputRegistrationAudit> {

    /**
     * 分页查询待审核列表
     *
     * @param dto 查询DTO
     * @return 分页结果
     */
    IPage<InputEnterprisePageVO> pageAuditList(InputEnterprisePageDTO dto);

    /**
     * 审核通过
     *
     * @param dto 审核通过DTO
     */
    void approve(InputAuditApproveDTO dto);

    /**
     * 审核驳回
     *
     * @param dto 审核驳回DTO
     */
    void reject(InputAuditRejectDTO dto);

    /**
     * 查询审核记录列表
     *
     * @param enterpriseId 机构ID
     * @return 审核记录列表
     */
    List<InputAuditRecordVO> listAuditRecords(String enterpriseId);
}
