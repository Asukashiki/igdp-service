package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.bean.BeanUtils;
import com.inspur.seed.constant.ApplicationStatusEnum;
import com.inspur.seed.constant.AuditResultEnum;
import com.inspur.seed.constant.OrgTypeEnum;
import com.inspur.seed.domain.dto.registration.AuditApproveDTO;
import com.inspur.seed.domain.dto.registration.AuditRejectDTO;
import com.inspur.seed.domain.dto.registration.EnterprisePageDTO;
import com.inspur.seed.domain.entity.OrgEnterpriseInfo;
import com.inspur.seed.domain.entity.OrgEnterpriseLocation;
import com.inspur.seed.domain.entity.OrgRegistrationAudit;
import com.inspur.seed.domain.vo.registration.AuditRecordVO;
import com.inspur.seed.domain.vo.registration.EnterprisePageVO;
import com.inspur.seed.mapper.OrgEnterpriseInfoMapper;
import com.inspur.seed.mapper.OrgEnterpriseLocationMapper;
import com.inspur.seed.mapper.OrgRegistrationAuditMapper;
import com.inspur.seed.service.IRegistrationAuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 机构注册审核服务实现类
 *
 * @author system
 */
@Service
public class RegistrationAuditServiceImpl extends ServiceImpl<OrgRegistrationAuditMapper, OrgRegistrationAudit> implements IRegistrationAuditService {

    @Resource(name = "orgEnterpriseInfoMapper")
    private OrgEnterpriseInfoMapper enterpriseInfoMapper;

    @Resource(name = "orgEnterpriseLocationMapper")
    private OrgEnterpriseLocationMapper locationMapper;

    @Resource(name = "orgRegistrationAuditMapper")
    private OrgRegistrationAuditMapper auditMapper;

    @Override
    public IPage<EnterprisePageVO> pageAuditList(EnterprisePageDTO dto) {
        Page<OrgEnterpriseInfo> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        LambdaQueryWrapper<OrgEnterpriseInfo> wrapper = new LambdaQueryWrapper<>();

        // 只查询待审核状态
        wrapper.eq(OrgEnterpriseInfo::getApplicationStatus, ApplicationStatusEnum.PENDING.getCode());

        // 企业名称模糊查询
        if (StringUtils.isNotEmpty(dto.getEnterpriseName())) {
            wrapper.like(OrgEnterpriseInfo::getEnterpriseName, dto.getEnterpriseName());
        }

        // 机构类型
        if (StringUtils.isNotEmpty(dto.getOrgType())) {
            wrapper.eq(OrgEnterpriseInfo::getOrgType, dto.getOrgType());
        }

        // 创建时间范围
        if (dto.getCreatedTimeStart() != null) {
            wrapper.ge(OrgEnterpriseInfo::getCreatedTime, dto.getCreatedTimeStart());
        }
        if (dto.getCreatedTimeEnd() != null) {
            wrapper.le(OrgEnterpriseInfo::getCreatedTime, dto.getCreatedTimeEnd());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(OrgEnterpriseInfo::getCreatedTime);

        IPage<OrgEnterpriseInfo> entityPage = enterpriseInfoMapper.selectPage(page, wrapper);

        // 转换为VO
        IPage<EnterprisePageVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        List<EnterprisePageVO> voList = entityPage.getRecords().stream().map(entity -> {
            EnterprisePageVO vo = new EnterprisePageVO();
            BeanUtils.copyBeanProp(vo, entity);

            // 设置机构类型名称
            OrgTypeEnum orgType = OrgTypeEnum.getByCode(entity.getOrgType());
            if (orgType != null) {
                vo.setOrgTypeName(orgType.getDesc());
            }

            // 设置申请状态名称
            ApplicationStatusEnum status = ApplicationStatusEnum.getByCode(entity.getApplicationStatus());
            if (status != null) {
                vo.setApplicationStatusName(status.getDesc());
            }

            // 查询位置信息获取woreda和zone
            LambdaQueryWrapper<OrgEnterpriseLocation> locationWrapper = new LambdaQueryWrapper<>();
            locationWrapper.eq(OrgEnterpriseLocation::getEnterpriseId, entity.getId());
            OrgEnterpriseLocation location = locationMapper.selectOne(locationWrapper);
            if (location != null) {
                vo.setWoreda(location.getWoreda());
                vo.setZone(location.getZone());
            }

            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(AuditApproveDTO dto) {
        // 1. 校验机构ID存在且状态为pending
        OrgEnterpriseInfo enterprise = enterpriseInfoMapper.selectById(dto.getId());
        if (enterprise == null) {
            throw new ServiceException("Enterprise not found");
        }

        if (!ApplicationStatusEnum.PENDING.getCode().equals(enterprise.getApplicationStatus())) {
            throw new ServiceException("Only pending applications can be approved");
        }

        // 2. 校验版本号
        if (!dto.getVersion().equals(enterprise.getVersion())) {
            throw new ServiceException("Data has been modified, please refresh and try again");
        }

        // 3. 更新申请状态为approved
        OrgEnterpriseInfo updateEntity = new OrgEnterpriseInfo();
        updateEntity.setId(dto.getId());
        updateEntity.setApplicationStatus(ApplicationStatusEnum.APPROVED.getCode());
        updateEntity.setUpdatedBy(LoginHelper.getUserId().toString());
        updateEntity.setUpdatedTime(LocalDateTime.now());
        updateEntity.setVersion(dto.getVersion());

        int result = enterpriseInfoMapper.updateById(updateEntity);
        if (result == 0) {
            throw new ServiceException("Approval failed, data may have been modified");
        }

        // 4. 创建审核记录
        OrgRegistrationAudit audit = new OrgRegistrationAudit();
        audit.setEnterpriseId(dto.getId());
        audit.setAuditUserId(LoginHelper.getUserId().toString());
        audit.setAuditUserName(LoginHelper.getUsername());
        audit.setAuditTime(LocalDateTime.now());
        audit.setAuditResult(AuditResultEnum.PASSED.getCode());
        audit.setAuditOpinion(null);
        audit.setRemark(dto.getRemark());
        audit.setCreatedBy(LoginHelper.getUserId().toString());
        audit.setCreatedTime(LocalDateTime.now());
        audit.setIsDeleted(0);

        auditMapper.insert(audit);

        // TODO: 5. 为机构开通系统权限（调用权限服务）
        // 这里需要调用权限服务，暂时不实现
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(AuditRejectDTO dto) {
        // 1. 校验机构ID存在且状态为pending
        OrgEnterpriseInfo enterprise = enterpriseInfoMapper.selectById(dto.getId());
        if (enterprise == null) {
            throw new ServiceException("Enterprise not found");
        }

        if (!ApplicationStatusEnum.PENDING.getCode().equals(enterprise.getApplicationStatus())) {
            throw new ServiceException("Only pending applications can be rejected");
        }

        // 2. 校验版本号
        if (!dto.getVersion().equals(enterprise.getVersion())) {
            throw new ServiceException("Data has been modified, please refresh and try again");
        }

        // 3. 校验审核意见必填
        if (StringUtils.isEmpty(dto.getAuditOpinion())) {
            throw new ServiceException("Audit opinion is required when rejecting");
        }

        // 4. 更新申请状态为rejected
        OrgEnterpriseInfo updateEntity = new OrgEnterpriseInfo();
        updateEntity.setId(dto.getId());
        updateEntity.setApplicationStatus(ApplicationStatusEnum.REJECTED.getCode());
        updateEntity.setUpdatedBy(LoginHelper.getUserId().toString());
        updateEntity.setUpdatedTime(LocalDateTime.now());
        updateEntity.setVersion(dto.getVersion());

        int result = enterpriseInfoMapper.updateById(updateEntity);
        if (result == 0) {
            throw new ServiceException("Rejection failed, data may have been modified");
        }

        // 5. 创建审核记录
        OrgRegistrationAudit audit = new OrgRegistrationAudit();
        audit.setEnterpriseId(dto.getId());
        audit.setAuditUserId(LoginHelper.getUserId().toString());
        audit.setAuditUserName(LoginHelper.getUsername());
        audit.setAuditTime(LocalDateTime.now());
        audit.setAuditResult(AuditResultEnum.REJECTED.getCode());
        audit.setAuditOpinion(dto.getAuditOpinion());
        audit.setRemark(dto.getRemark());
        audit.setCreatedBy(LoginHelper.getUserId().toString());
        audit.setCreatedTime(LocalDateTime.now());
        audit.setIsDeleted(0);

        auditMapper.insert(audit);
    }

    @Override
    public List<AuditRecordVO> listAuditRecords(String enterpriseId) {
        LambdaQueryWrapper<OrgRegistrationAudit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgRegistrationAudit::getEnterpriseId, enterpriseId);
        wrapper.orderByDesc(OrgRegistrationAudit::getAuditTime);

        List<OrgRegistrationAudit> audits = auditMapper.selectList(wrapper);

        return audits.stream().map(audit -> {
            AuditRecordVO vo = new AuditRecordVO();
            BeanUtils.copyBeanProp(vo, audit);

            AuditResultEnum auditResult = AuditResultEnum.getByCode(audit.getAuditResult());
            if (auditResult != null) {
                vo.setAuditResultName(auditResult.getDesc());
            }

            return vo;
        }).collect(Collectors.toList());
    }
}
