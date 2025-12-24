package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.TrialBasic;
import com.inspur.seed.domain.dto.TrialBasicAuditDTO;
import com.inspur.seed.domain.entity.TrialBasicAudit;
import com.inspur.seed.domain.entity.TrialBasicAuditHistory;
import com.inspur.seed.domain.vo.TrialBasicAuditVO;
import com.inspur.seed.mapper.TrialBasicAuditHistoryMapper;
import com.inspur.seed.mapper.TrialBasicAuditMapper;
import com.inspur.seed.mapper.TrialBasicMapper;
import com.inspur.seed.service.ITrialBasicAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 试验基础信息审核Service实现
 *
 * @author system
 * @since 2025-01-30
 */
@Slf4j
@Service
public class TrialBasicAuditServiceImpl extends ServiceImpl<TrialBasicAuditMapper, TrialBasicAudit> implements ITrialBasicAuditService {

    @Autowired
    private TrialBasicAuditMapper trialBasicAuditMapper;

    @Autowired
    private TrialBasicAuditHistoryMapper trialBasicAuditHistoryMapper;

    @Autowired
    private TrialBasicMapper trialBasicMapper;

    /**
     * 状态描述映射
     */
    private static final Map<String, String> STATUS_DESC_MAP = new HashMap<>();

    static {
        STATUS_DESC_MAP.put("S0", "Draft");
        STATUS_DESC_MAP.put("S1", "Pending Approval");
        STATUS_DESC_MAP.put("S2", "Approved");
        STATUS_DESC_MAP.put("S3", "Rejected");
        STATUS_DESC_MAP.put("S4", "Voided");
        STATUS_DESC_MAP.put("S9", "Archived");
        STATUS_DESC_MAP.put("S10", "Cancelled");
    }

    @Override
    public IPage<TrialBasicAuditVO> selectAuditList(TrialBasicAuditDTO dto) {
        Page<TrialBasicAudit> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        LambdaQueryWrapper<TrialBasicAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrialBasicAudit::getDeleted, "0")
                .like(ObjectUtil.isNotEmpty(dto.getTrialName()), TrialBasicAudit::getTrialName, dto.getTrialName());


        // 如果auditStatus为空字符串,则查询已审核的状态(S2和S3)
        if (StringUtils.hasText(dto.getAuditStatus())) {
            queryWrapper.eq(TrialBasicAudit::getAuditStatus, dto.getAuditStatus());
        } else if (dto.getAuditStatus() != null && dto.getAuditStatus().isEmpty()) {
            // auditStatus为空字符串时,查询S2和S3
            queryWrapper.in(TrialBasicAudit::getAuditStatus, "S2", "S3");
        } else if ("S4".equals(dto.getAuditStatus())) {
            // 查询已作废的状态
            queryWrapper.eq(TrialBasicAudit::getAuditStatus, "S4");
        }

        queryWrapper.eq(StringUtils.hasText(dto.getTrialId()), TrialBasicAudit::getTrialId, dto.getTrialId())
                .orderByDesc(TrialBasicAudit::getSubmitTime);

        IPage<TrialBasicAudit> auditPage = trialBasicAuditMapper.selectPage(page, queryWrapper);

        // 转换为VO
        IPage<TrialBasicAuditVO> voPage = new Page<>();
        BeanUtil.copyProperties(auditPage, voPage);
        List<TrialBasicAuditVO> voList = auditPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public TrialBasicAuditVO getAuditById(String auditId) {
        TrialBasicAudit audit = trialBasicAuditMapper.selectById(auditId);
        if (audit == null) {
            throw new ServiceException("Audit record not found");
        }
        return convertToVO(audit);
    }

    @Override
    public TrialBasicAuditVO getAuditByTrialId(String trialId) {
        LambdaQueryWrapper<TrialBasicAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrialBasicAudit::getTrialId, trialId)
                .eq(TrialBasicAudit::getDeleted, "0")
                .orderByDesc(TrialBasicAudit::getSubmitTime)
                .last("LIMIT 1");

        TrialBasicAudit audit = trialBasicAuditMapper.selectOne(queryWrapper);
        if (audit == null) {
            throw new ServiceException("Audit record not found");
        }
        return convertToVO(audit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean performAudit(TrialBasicAuditDTO dto) {
        // 1. 获取审核记录
        TrialBasicAudit audit = trialBasicAuditMapper.selectById(dto.getAuditId());
        if (audit == null) {
            throw new ServiceException("Audit record not found");
        }

        // 2. 检查审核状态
        if (!"S1".equals(audit.getAuditStatus())) {
            throw new ServiceException("Only pending audits can be reviewed");
        }

        // 3. 获取试验信息
        TrialBasic trial = trialBasicMapper.selectById(audit.getTrialId());
        if (trial == null) {
            throw new ServiceException("Trial information not found");
        }

        // 4. 检查试验状态
        if (!"S1".equals(trial.getTrialStatus())) {
            throw new ServiceException("Only trials with 'Pending Approval' status can be reviewed");
        }

        // 5. 获取当前用户信息
        String currentUserId = SecurityUtils.getUserId().toString();
        String currentUserName = SecurityUtils.getUsername();

        // 6. 执行审核操作
        String beforeStatus = trial.getTrialStatus();
        String afterStatus;
        String operationType;

        if ("S2".equals(dto.getAuditStatus())) {
            // 审核通过
            afterStatus = "S2";
            operationType = "APPROVE";

            // 更新审核记录
            audit.setAuditStatus("S2");
            audit.setAuditOpinion(dto.getAuditOpinion());
            audit.setAuditorId(currentUserId);
            audit.setAuditorName(currentUserName);
            audit.setAuditTime(LocalDateTime.now());

            // 更新试验信息
            trial.setTrialStatus("S2");
            trial.setApprovedBy(currentUserId);
            trial.setApprovedName(currentUserName);
            trial.setApprovedTime(LocalDateTime.now());

        } else if ("S3".equals(dto.getAuditStatus())) {
            // 审核退回
            if (!StringUtils.hasText(dto.getRejectReason())) {
                throw new ServiceException("Rejection reason is required");
            }

            afterStatus = "S3";
            operationType = "REJECT";

            // 更新审核记录
            audit.setAuditStatus("S3");
            audit.setRejectReason(dto.getRejectReason());
            audit.setAuditorId(currentUserId);
            audit.setAuditorName(currentUserName);
            audit.setAuditTime(LocalDateTime.now());

            // 更新试验信息
            trial.setTrialStatus("S3");
            trial.setRejectedBy(currentUserId);
            trial.setRejectedName(currentUserName);
            trial.setRejectedTime(LocalDateTime.now());
            trial.setRejectReason(dto.getRejectReason()); // 添加退回原因

        } else {
            throw new ServiceException("Invalid audit status");
        }

        // 7. 保存更新
        audit.setUpdateTime(LocalDateTime.now());
        audit.setUpdateBy(currentUserId);
        trialBasicAuditMapper.updateById(audit);

        trial.setUpdateTime(LocalDateTime.now());
        trial.setUpdateBy(currentUserId);
        trialBasicMapper.updateById(trial);

        // 8. 记录审核历史
        TrialBasicAuditHistory history = new TrialBasicAuditHistory();
        history.setHistoryId(IdUtil.simpleUUID());
        history.setAuditId(audit.getAuditId());
        history.setTrialId(trial.getTrialId());
        history.setOperationType(operationType);
        history.setOperationDesc(operationType.equals("APPROVE") ? dto.getAuditOpinion() : dto.getRejectReason());
        history.setOperatorId(currentUserId);
        history.setOperatorName(currentUserName);
        history.setOperationTime(LocalDateTime.now());
        history.setBeforeStatus(beforeStatus);
        history.setAfterStatus(afterStatus);
        history.setCreateTime(LocalDateTime.now());
        history.setDeleted("0");
        trialBasicAuditHistoryMapper.insert(history);

        log.info("Trial audit completed: trialId={}, auditId={}, result={}", trial.getTrialId(), audit.getAuditId(), afterStatus);

        return true;
    }

    @Override
    public IPage<TrialBasicAuditVO> getAuditHistory(String trialId, Integer pageNum, Integer pageSize) {
        Page<TrialBasicAudit> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<TrialBasicAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrialBasicAudit::getTrialId, trialId)
                .eq(TrialBasicAudit::getDeleted, "0")
                .orderByDesc(TrialBasicAudit::getSubmitTime);

        IPage<TrialBasicAudit> auditPage = trialBasicAuditMapper.selectPage(page, queryWrapper);

        // 转换为VO
        IPage<TrialBasicAuditVO> voPage = new Page<>();
        BeanUtil.copyProperties(auditPage, voPage);
        List<TrialBasicAuditVO> voList = auditPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    /**
     * 转换为VO对象
     */
    private TrialBasicAuditVO convertToVO(TrialBasicAudit audit) {
        TrialBasicAuditVO vo = new TrialBasicAuditVO();
        BeanUtil.copyProperties(audit, vo);
        vo.setAuditStatusDesc(STATUS_DESC_MAP.getOrDefault(audit.getAuditStatus(), audit.getAuditStatus()));

        // 从trial_basic表查询试验信息,填充缺失字段
        TrialBasic trial = trialBasicMapper.selectById(audit.getTrialId());
        if (trial != null) {
            vo.setCropType(trial.getCropType());
            vo.setVarietyName(trial.getVarietyName());
            vo.setCreatedName(trial.getCreatedName());
            vo.setCreateTime(trial.getCreateTime());
            vo.setModifiedName(trial.getModifiedName());
            vo.setModifiedTime(trial.getUpdateTime());
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean voidAudit(String auditId, String voidReason) {
        // 1. 获取审核记录
        TrialBasicAudit audit = trialBasicAuditMapper.selectById(auditId);
        if (audit == null) {
            throw new ServiceException("Audit record not found");
        }

        // 2. 检查审核状态 - 只有已审批(S2)的记录才能作废
        if (!"S2".equals(audit.getAuditStatus())) {
            throw new ServiceException("Only approved audits can be voided");
        }

        // 3. 检查作废原因
        if (!StringUtils.hasText(voidReason)) {
            throw new ServiceException("Void reason is required");
        }

        // 4. 获取当前用户信息
        String currentUserId = SecurityUtils.getUserId().toString();
        String currentUserName = SecurityUtils.getUsername();

        // 5. 更新审核记录状态为已作废(S4)
        String beforeStatus = audit.getAuditStatus();
        audit.setAuditStatus("S4");
        audit.setRejectReason(voidReason); // 使用rejectReason字段存储作废原因
        audit.setUpdateTime(LocalDateTime.now());
        audit.setUpdateBy(currentUserId);
        trialBasicAuditMapper.updateById(audit);

        // 6. 记录审核历史
        TrialBasicAuditHistory history = new TrialBasicAuditHistory();
        history.setHistoryId(IdUtil.simpleUUID());
        history.setAuditId(audit.getAuditId());
        history.setTrialId(audit.getTrialId());
        history.setOperationType("VOID");
        history.setOperationDesc(voidReason);
        history.setOperatorId(currentUserId);
        history.setOperatorName(currentUserName);
        history.setOperationTime(LocalDateTime.now());
        history.setBeforeStatus(beforeStatus);
        history.setAfterStatus("S4");
        history.setCreateTime(LocalDateTime.now());
        history.setDeleted("0");
        trialBasicAuditHistoryMapper.insert(history);

        log.info("Audit record voided: auditId={}, trialId={}, voidBy={}", auditId, audit.getTrialId(), currentUserName);

        return true;
    }
}
