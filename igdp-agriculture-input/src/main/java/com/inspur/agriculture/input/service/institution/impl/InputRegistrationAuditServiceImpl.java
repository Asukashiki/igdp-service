package com.inspur.agriculture.input.service.institution.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.input.constant.InputApplicationStatusEnum;
import com.inspur.agriculture.input.constant.InputAuditResultEnum;
import com.inspur.agriculture.input.constant.InputOrgTypeEnum;
import com.inspur.agriculture.input.domain.institution.dto.InputAuditApproveDTO;
import com.inspur.agriculture.input.domain.institution.dto.InputAuditRejectDTO;
import com.inspur.agriculture.input.domain.institution.dto.InputEnterprisePageDTO;
import com.inspur.agriculture.input.domain.institution.entity.InputEnterpriseInfo;
import com.inspur.agriculture.input.domain.institution.entity.InputEnterpriseLocation;
import com.inspur.agriculture.input.domain.institution.entity.InputRegistrationAudit;
import com.inspur.agriculture.input.domain.institution.vo.InputAuditRecordVO;
import com.inspur.agriculture.input.domain.institution.vo.InputEnterprisePageVO;
import com.inspur.agriculture.input.mapper.institution.InputEnterpriseInfoMapper;
import com.inspur.agriculture.input.mapper.institution.InputEnterpriseLocationMapper;
import com.inspur.agriculture.input.mapper.institution.InputRegistrationAuditMapper;
import com.inspur.agriculture.input.service.institution.IInputRegistrationAuditService;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.bean.BeanUtils;
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
public class InputRegistrationAuditServiceImpl extends ServiceImpl<InputRegistrationAuditMapper, InputRegistrationAudit> implements IInputRegistrationAuditService {

    @Resource(name = "inputEnterpriseInfoMapper")
    private InputEnterpriseInfoMapper enterpriseInfoMapper;

    @Resource(name = "inputEnterpriseLocationMapper")
    private InputEnterpriseLocationMapper locationMapper;

    @Resource(name = "inputRegistrationAuditMapper")
    private InputRegistrationAuditMapper auditMapper;

    @Override
    public IPage<InputEnterprisePageVO> pageAuditList(InputEnterprisePageDTO dto) {
        Page<InputEnterpriseInfo> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        LambdaQueryWrapper<InputEnterpriseInfo> wrapper = new LambdaQueryWrapper<>();

        // 只查询待审核状态
        wrapper.eq(InputEnterpriseInfo::getApplicationStatus, InputApplicationStatusEnum.PENDING.getCode());

        // 企业名称模糊查询
        if (StringUtils.isNotEmpty(dto.getEnterpriseName())) {
            wrapper.like(InputEnterpriseInfo::getEnterpriseName, dto.getEnterpriseName());
        }

        // 机构类型
        if (StringUtils.isNotEmpty(dto.getOrgType())) {
            wrapper.eq(InputEnterpriseInfo::getOrgType, dto.getOrgType());
        }

        // 创建时间范围
        if (dto.getCreatedTimeStart() != null) {
            wrapper.ge(InputEnterpriseInfo::getCreatedTime, dto.getCreatedTimeStart());
        }
        if (dto.getCreatedTimeEnd() != null) {
            wrapper.le(InputEnterpriseInfo::getCreatedTime, dto.getCreatedTimeEnd());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(InputEnterpriseInfo::getCreatedTime);

        IPage<InputEnterpriseInfo> entityPage = enterpriseInfoMapper.selectPage(page, wrapper);

        // 转换为VO
        IPage<InputEnterprisePageVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        List<InputEnterprisePageVO> voList = entityPage.getRecords().stream().map(entity -> {
            InputEnterprisePageVO vo = new InputEnterprisePageVO();
            BeanUtils.copyBeanProp(vo, entity);

            // 设置机构类型名称
            InputOrgTypeEnum orgType = InputOrgTypeEnum.getByCode(entity.getOrgType());
            if (orgType != null) {
                vo.setOrgTypeName(orgType.getDesc());
            }

            // 设置申请状态名称
            InputApplicationStatusEnum status = InputApplicationStatusEnum.getByCode(entity.getApplicationStatus());
            if (status != null) {
                vo.setApplicationStatusName(status.getDesc());
            }

            // 查询位置信息获取woreda和zone
            LambdaQueryWrapper<InputEnterpriseLocation> locationWrapper = new LambdaQueryWrapper<>();
            locationWrapper.eq(InputEnterpriseLocation::getEnterpriseId, entity.getId());
            InputEnterpriseLocation location = locationMapper.selectOne(locationWrapper);
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
    public void approve(InputAuditApproveDTO dto) {
        // 1. 校验机构ID存在且状态为pending
        InputEnterpriseInfo enterprise = enterpriseInfoMapper.selectById(dto.getId());
        if (enterprise == null) {
            throw new ServiceException("Enterprise not found");
        }

        if (!InputApplicationStatusEnum.PENDING.getCode().equals(enterprise.getApplicationStatus())) {
            throw new ServiceException("Only pending applications can be approved");
        }

        // 2. 校验版本号
        if (!dto.getVersion().equals(enterprise.getVersion())) {
            throw new ServiceException("Data has been modified, please refresh and try again");
        }

        // 3. 更新申请状态为approved
        InputEnterpriseInfo updateEntity = new InputEnterpriseInfo();
        updateEntity.setId(dto.getId());
        updateEntity.setApplicationStatus(InputApplicationStatusEnum.APPROVED.getCode());
        updateEntity.setUpdatedBy(LoginHelper.getUserId().toString());
        updateEntity.setUpdatedTime(LocalDateTime.now());
        updateEntity.setVersion(dto.getVersion());

        int result = enterpriseInfoMapper.updateById(updateEntity);
        if (result == 0) {
            throw new ServiceException("Approval failed, data may have been modified");
        }

        // 4. 创建审核记录
        InputRegistrationAudit audit = new InputRegistrationAudit();
        audit.setEnterpriseId(dto.getId());
        audit.setAuditUserId(LoginHelper.getUserId().toString());
        audit.setAuditUserName(LoginHelper.getUsername());
        audit.setAuditTime(LocalDateTime.now());
        audit.setAuditResult(InputAuditResultEnum.PASSED.getCode());
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
    public void reject(InputAuditRejectDTO dto) {
        // 1. 校验机构ID存在且状态为pending
        InputEnterpriseInfo enterprise = enterpriseInfoMapper.selectById(dto.getId());
        if (enterprise == null) {
            throw new ServiceException("Enterprise not found");
        }

        if (!InputApplicationStatusEnum.PENDING.getCode().equals(enterprise.getApplicationStatus())) {
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
        InputEnterpriseInfo updateEntity = new InputEnterpriseInfo();
        updateEntity.setId(dto.getId());
        updateEntity.setApplicationStatus(InputApplicationStatusEnum.REJECTED.getCode());
        updateEntity.setUpdatedBy(LoginHelper.getUserId().toString());
        updateEntity.setUpdatedTime(LocalDateTime.now());
        updateEntity.setVersion(dto.getVersion());

        int result = enterpriseInfoMapper.updateById(updateEntity);
        if (result == 0) {
            throw new ServiceException("Rejection failed, data may have been modified");
        }

        // 5. 创建审核记录
        InputRegistrationAudit audit = new InputRegistrationAudit();
        audit.setEnterpriseId(dto.getId());
        audit.setAuditUserId(LoginHelper.getUserId().toString());
        audit.setAuditUserName(LoginHelper.getUsername());
        audit.setAuditTime(LocalDateTime.now());
        audit.setAuditResult(InputAuditResultEnum.REJECTED.getCode());
        audit.setAuditOpinion(dto.getAuditOpinion());
        audit.setRemark(dto.getRemark());
        audit.setCreatedBy(LoginHelper.getUserId().toString());
        audit.setCreatedTime(LocalDateTime.now());
        audit.setIsDeleted(0);

        auditMapper.insert(audit);
    }

    @Override
    public List<InputAuditRecordVO> listAuditRecords(String enterpriseId) {
        LambdaQueryWrapper<InputRegistrationAudit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputRegistrationAudit::getEnterpriseId, enterpriseId);
        wrapper.orderByDesc(InputRegistrationAudit::getAuditTime);

        List<InputRegistrationAudit> audits = auditMapper.selectList(wrapper);

        return audits.stream().map(audit -> {
            InputAuditRecordVO vo = new InputAuditRecordVO();
            BeanUtils.copyBeanProp(vo, audit);

            InputAuditResultEnum auditResult = InputAuditResultEnum.getByCode(audit.getAuditResult());
            if (auditResult != null) {
                vo.setAuditResultName(auditResult.getDesc());
            }

            return vo;
        }).collect(Collectors.toList());
    }
}
