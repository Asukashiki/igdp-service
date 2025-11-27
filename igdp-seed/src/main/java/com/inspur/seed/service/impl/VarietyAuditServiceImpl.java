package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.seed.domain.VarietyAudit;
import com.inspur.seed.domain.vo.VarietyAuditTaskVO;
import com.inspur.seed.mapper.VarietyAuditMapper;
import com.inspur.seed.service.IVarietyAuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 品种审核服务实现类
 *
 * @author system
 */
@Service
public class VarietyAuditServiceImpl extends ServiceImpl<VarietyAuditMapper, VarietyAudit> implements IVarietyAuditService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handleAudit(VarietyAudit varietyAudit) {
        // 校验审核结果必须为1（通过）或2（不通过）
        if (varietyAudit.getAuditResult() != 1 && varietyAudit.getAuditResult() != 2) {
            throw new ServiceException("审核结果必须为通过或不通过");
        }

        // 校验驳回时必须填写驳回原因
        if (varietyAudit.getAuditResult() == 2 && StringUtils.isEmpty(varietyAudit.getRejectReason())) {
            throw new ServiceException("驳回时必须填写驳回原因");
        }

        // 校验通过时必须填写审核意见
        if (varietyAudit.getAuditResult() == 1 && StringUtils.isEmpty(varietyAudit.getAuditOpinion())) {
            throw new ServiceException("通过时必须填写审核意见");
        }

        // 查询当前登记ID对应的最新审核单
        VarietyAudit existingAudit = queryLatestByRegistrationId(varietyAudit.getRegistrationId());
        if (existingAudit == null) {
            throw new ServiceException("未找到待审核的记录");
        }

        // 修改当前审核单
        String auditId = existingAudit.getAuditId();
        varietyAudit.setAuditId(auditId);
        varietyAudit.setEnterpriseId(existingAudit.getEnterpriseId());
        varietyAudit.setVarietyName(existingAudit.getVarietyName());
        varietyAudit.setAuditStage(existingAudit.getAuditStage());
        
        // 设置审核时间和审核人
        varietyAudit.setAuditTime(LocalDateTime.now());
        varietyAudit.setAuditor(LoginHelper.getUsername());
        
        // 设置更新信息
        varietyAudit.setUpdateBy(LoginHelper.getUsername());
        varietyAudit.setUpdateTime(LocalDateTime.now());
        
        // 保留创建信息
        varietyAudit.setCreateBy(existingAudit.getCreateBy());
        varietyAudit.setCreateTime(existingAudit.getCreateTime());

        // 更新审核记录
        updateById(varietyAudit);

        return auditId;
    }

    @Override
    public List<VarietyAudit> queryAuditList(String varietyName, String enterpriseName, Integer auditResult) {
        LambdaQueryWrapper<VarietyAudit> wrapper = new LambdaQueryWrapper<>();

        // 品种名称筛选
        if (StringUtils.isNotEmpty(varietyName)) {
            wrapper.like(VarietyAudit::getVarietyName, varietyName);
        }

        // 企业名称筛选（需要关联查询，这里简化处理）
        // 如果需要按企业名称筛选，可以通过enterpriseId关联查询

        // 审核结果筛选
        if (auditResult != null) {
            wrapper.eq(VarietyAudit::getAuditResult, auditResult);
        }

        // 按审核时间倒序排列
        wrapper.orderByDesc(VarietyAudit::getAuditTime);

        return list(wrapper);
    }

    @Override
    public VarietyAudit queryByAuditId(String auditId) {
        return getById(auditId);
    }

    @Override
    public VarietyAudit queryLatestByRegistrationId(String registrationId) {
        LambdaQueryWrapper<VarietyAudit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VarietyAudit::getRegistrationId, registrationId);
        wrapper.orderByDesc(VarietyAudit::getAuditTime);
        wrapper.last("LIMIT 1");
        return getOne(wrapper);
    }
    

    @Override
    public List<VarietyAuditTaskVO> queryAuditTaskList(String varietyName, String enterpriseName, Integer auditResult) {
        return baseMapper.selectAuditTaskList(varietyName, enterpriseName, auditResult);
    }

    @Override
    public VarietyAuditTaskVO queryAuditTaskDetail(String registrationId) {
        return baseMapper.selectAuditTaskDetail(registrationId);
    }
}