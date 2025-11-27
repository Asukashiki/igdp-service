package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.EnterpriseAudit;
import com.inspur.seed.domain.EnterpriseInfo;
import com.inspur.seed.domain.vo.EnterpriseAuditVO;
import com.inspur.seed.mapper.EnterpriseAuditMapper;
import com.inspur.seed.mapper.EnterpriseInfoMapper;
import com.inspur.seed.service.IEnterpriseAuditService;
import com.inspur.seed.service.IEnterpriseCertifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 企业审核服务实现类
 *
 * @author system
 */
@Service
public class EnterpriseAuditServiceImpl extends ServiceImpl<EnterpriseAuditMapper, EnterpriseAudit> implements IEnterpriseAuditService {

    @Resource
    private IEnterpriseCertifyService enterpriseCertifyService;

    @Resource
    private EnterpriseInfoMapper enterpriseInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handleAudit(EnterpriseAudit enterpriseAudit) {
        // 校验审核ID是否存在
        if (StringUtils.isEmpty(enterpriseAudit.getAuditId())) {
            throw new ServiceException("审核ID不能为空");
        }
        
        // 查询现有审核记录
        EnterpriseAudit existingAudit = getById(enterpriseAudit.getAuditId());
        if (existingAudit == null) {
            throw new ServiceException("审核记录不存在");
        }
        
        // 校验企业是否存在
        if (enterpriseCertifyService.queryByEnterpriseId(enterpriseAudit.getEnterpriseId()) == null) {
            throw new ServiceException("企业信息不存在");
        }

        // 校验驳回时必须填写驳回原因
        if (enterpriseAudit.getAuditResult() == 2 && StringUtils.isEmpty(enterpriseAudit.getRejectReason())) {
            throw new ServiceException("驳回时必须填写驳回原因");
        }

        // 设置审核时间
        enterpriseAudit.setAuditTime(LocalDateTime.now());

        // 设置更新信息
        enterpriseAudit.setUpdateBy(LoginHelper.getUsername());
        enterpriseAudit.setUpdateTime(LocalDateTime.now());

        // 保留原有创建信息
        enterpriseAudit.setCreateBy(existingAudit.getCreateBy());
        enterpriseAudit.setCreateTime(existingAudit.getCreateTime());

        // 更新审核记录
        updateById(enterpriseAudit);

        // 更新企业认证状态
        Integer certificationStatus = enterpriseAudit.getAuditResult();
        enterpriseCertifyService.updateCertificationStatus(enterpriseAudit.getEnterpriseId(), certificationStatus);

        return enterpriseAudit.getAuditId();
    }

    @Override
    public List<EnterpriseAudit> queryAuditList(String enterpriseName, Integer certificationStatus) {
        LambdaQueryWrapper<EnterpriseAudit> wrapper = new LambdaQueryWrapper<>();

        // 使用apply方法写原生SQL实现内连接查询
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("EXISTS (SELECT 1 FROM enterprise_info ei WHERE ei.enterprise_id = enterprise_audit.enterprise_id");

        // 添加企业名称筛选条件
        if (StringUtils.isNotEmpty(enterpriseName)) {
            sqlBuilder.append(" AND ei.enterprise_name LIKE '%").append(enterpriseName).append("%'");
        }

        // 添加认证状态筛选条件
        if (certificationStatus != null) {
            sqlBuilder.append(" AND ei.certification_status = ").append(certificationStatus);
        }

        sqlBuilder.append(")");
        wrapper.apply(sqlBuilder.toString());

        // 按审核时间倒序排列
        wrapper.orderByDesc(EnterpriseAudit::getAuditTime);

        return list(wrapper);
    }
    
    @Override
    public List<EnterpriseAuditVO> queryAuditListWithFilter(String enterpriseName, String enterpriseId, String licenseNo, Integer certificationStatus) {
        // 先查询符合条件的企业信息
        LambdaQueryWrapper<EnterpriseInfo> infoWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotEmpty(enterpriseName)) {
            infoWrapper.like(EnterpriseInfo::getEnterpriseName, enterpriseName);
        }
        if (StringUtils.isNotEmpty(enterpriseId)) {
            infoWrapper.like(EnterpriseInfo::getEnterpriseId, enterpriseId);
        }
        if (StringUtils.isNotEmpty(licenseNo)) {
            infoWrapper.like(EnterpriseInfo::getSeedLicenseNo, licenseNo);
        }
        if (certificationStatus != null) {
            infoWrapper.eq(EnterpriseInfo::getCertificationStatus, certificationStatus);
        }
        
        List<EnterpriseInfo> enterpriseInfos = enterpriseInfoMapper.selectList(infoWrapper);
        
        if (enterpriseInfos.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取企业ID列表
        List<String> enterpriseIdList = enterpriseInfos.stream()
                .map(EnterpriseInfo::getEnterpriseId)
                .collect(java.util.stream.Collectors.toList());
        
        // 查询这些企业的审核记录
        LambdaQueryWrapper<EnterpriseAudit> auditWrapper = new LambdaQueryWrapper<>();
        auditWrapper.in(EnterpriseAudit::getEnterpriseId, enterpriseIdList);
        auditWrapper.orderByDesc(EnterpriseAudit::getCreateTime);
        
        List<EnterpriseAudit> audits = list(auditWrapper);
        
        // 创建企业信息映射，方便快速查找
        Map<String, EnterpriseInfo> enterpriseInfoMap = enterpriseInfos.stream()
                .collect(java.util.stream.Collectors.toMap(EnterpriseInfo::getEnterpriseId, info -> info));
        
        // 转换为VO对象
        List<EnterpriseAuditVO> result = new ArrayList<>();
        for (EnterpriseAudit audit : audits) {
            EnterpriseAuditVO vo = new EnterpriseAuditVO();
            
            // 填充审核记录相关字段
            vo.setAuditId(audit.getAuditId());
            vo.setEnterpriseId(audit.getEnterpriseId());
            vo.setAuditStage(audit.getAuditStage());
            vo.setAuditor(audit.getAuditor());
            vo.setAuditResult(audit.getAuditResult());
            vo.setAuditOpinion(audit.getAuditOpinion());
            vo.setRejectReason(audit.getRejectReason());

            
            // 填充企业信息
            EnterpriseInfo info = enterpriseInfoMap.get(audit.getEnterpriseId());
            if (info != null) {
                vo.setEnterpriseName(info.getEnterpriseName());
                vo.setUnifiedSocialCreditCode(info.getUnifiedSocialCreditCode());
                vo.setSeedLicenseNo(info.getSeedLicenseNo());
                vo.setEnterpriseType(info.getEnterpriseType());
                vo.setLicenseStartDate(info.getLicenseStartDate());
                vo.setLicenseEndDate(info.getLicenseEndDate());
                vo.setDetailedAddress(info.getDetailedAddress());
                vo.setEstablishmentDate(info.getEstablishmentDate());
                vo.setLegalPersonName(info.getLegalPersonName());
                vo.setLegalPersonId(info.getLegalPersonId());
                vo.setContactPerson(info.getContactPerson());
                vo.setContactPhone(info.getContactPhone());
                vo.setContactEmail(info.getContactEmail());
                vo.setBusinessLicenseUrl(info.getBusinessLicenseUrl());
                vo.setSeedLicenseUrl(info.getSeedLicenseUrl());
                vo.setTaxRegistrationUrl(info.getTaxRegistrationUrl());
                vo.setFactoryLicenseUrl(info.getFactoryLicenseUrl());
                vo.setCertificationStatus(info.getCertificationStatus());
                vo.setCreateTime(info.getCreateTime());
            }
            
            result.add(vo);
        }
        
        return result;
    }


    @Override
    public EnterpriseAudit queryByAuditId(String auditId) {
        return getById(auditId);
    }

    @Override
    public EnterpriseAudit queryLatestByEnterpriseId(String enterpriseId) {
        LambdaQueryWrapper<EnterpriseAudit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseAudit::getEnterpriseId, enterpriseId);
        wrapper.orderByDesc(EnterpriseAudit::getAuditTime);
        wrapper.last("LIMIT 1");
        return getOne(wrapper);
    }

}