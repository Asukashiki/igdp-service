package com.inspur.seed.service.impl;

import cn.hutool.core.util.IdUtil;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.MessageUtils;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.TrialBasic;
import com.inspur.seed.domain.TrialPlotRelation;
import com.inspur.seed.mapper.TrialBasicMapper;
import com.inspur.seed.mapper.TrialPlotRelationMapper;
import com.inspur.seed.service.ITrialBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 试验基础信息Service实现类
 *
 * @author inspur
 */
@Service
public class TrialBasicServiceImpl implements ITrialBasicService {

    @Autowired
    private TrialBasicMapper trialBasicMapper;

    @Autowired
    private TrialPlotRelationMapper trialPlotRelationMapper;

    @Autowired
    private com.inspur.seed.mapper.TrialBasicAuditMapper trialBasicAuditMapper;

    @Autowired
    private com.inspur.seed.mapper.TrialBasicAuditHistoryMapper trialBasicAuditHistoryMapper;

    @Override
    public List<TrialBasic> selectTrialBasicList(TrialBasic trialBasic) {
        List<TrialBasic> list = trialBasicMapper.selectTrialBasicList(trialBasic);
        return list;
    }

    @Override
    public TrialBasic selectTrialBasicById(String trialId) {
        TrialBasic trialBasic = trialBasicMapper.selectTrialBasicById(trialId);
        return trialBasic;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertTrialBasic(TrialBasic trialBasic) {
        // 检查试验名称唯一性
        if (trialBasicMapper.checkTrialNameUnique(trialBasic.getTrialName(), null) > 0) {
            throw new ServiceException("试验名称已存在");
        }

        // 生成试验ID: TR-{variety_code}-{location_id}-{year}-序号
        String trialId = generateTrialId(trialBasic.getBatchId(), trialBasic.getLocationId(), trialBasic.getYear());
        trialBasic.setTrialId(trialId);

        // 设置创建信息
        trialBasic.setCreateTime(LocalDateTime.now());
        trialBasic.setCreateBy(SecurityUtils.getUserId().toString());
        trialBasic.setCreatedName(SecurityUtils.getUsername());
        trialBasic.setTrialStatus("S0"); // 默认草稿状态

        // 保存试验信息
        trialBasicMapper.insert(trialBasic);

        return trialId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTrialBasic(TrialBasic trialBasic) {
        // 检查试验名称唯一性
        if (trialBasicMapper.checkTrialNameUnique(trialBasic.getTrialName(), trialBasic.getTrialId()) > 0) {
            throw new ServiceException("试验名称已存在");
        }

        // 设置更新信息
        trialBasic.setUpdateTime(LocalDateTime.now());
        trialBasic.setUpdateBy(SecurityUtils.getUserId().toString());
        trialBasic.setModifiedName(SecurityUtils.getUsername());

        // 更新试验信息
        int rows = trialBasicMapper.updateById(trialBasic);

        // 删除原有关联关系
        trialPlotRelationMapper.deleteByTrialId(trialBasic.getTrialId());


        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTrialBasicByIds(String[] trialIds) {
        int count = 0;
        for (String trialId : trialIds) {
            // 删除关联关系
            trialPlotRelationMapper.deleteByTrialId(trialId);

            // 删除试验信息 - 使用deleteById方法，让@TableLogic自动处理逻辑删除
            boolean success = trialBasicMapper.deleteById(trialId) > 0;
            if (success) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<TrialBasic> selectTrialOptions(String batchId) {
        return trialBasicMapper.selectTrialOptions(batchId);
    }

    /**
     * 生成试验ID
     * 格式: TR-{variety_code}-{location_id}-{year}-序号
     */
    private String generateTrialId(String batchId, String locationId, Integer year) {
        if (batchId == null || batchId.isEmpty()) {
            throw new ServiceException("育种批次ID不能为空");
        }
        if (locationId == null || locationId.isEmpty()) {
            throw new ServiceException("研究中心ID不能为空");
        }
        if (year == null) {
            throw new ServiceException("年份不能为空");
        }

        String trialId = trialBasicMapper.generateTrialIdByBatchAndLocationAndYear(batchId, locationId, year);
        if (trialId == null) {
            // 如果没有找到记录，需要从batch中获取variety_code来生成默认ID
            // 这里假设返回null时使用一个默认格式，实际应该从batch表查询variety_code
            throw new ServiceException("无法生成试验ID，请检查育种批次信息");
        }
        return trialId;
    }

    /**
     * 保存地块关联关系
     */
    private void savePlotRelations(String trialId, List<String> plotIds) {
        if (CollectionUtils.isEmpty(plotIds)) {
            return;
        }

        Date now = new Date();
        List<TrialPlotRelation> relationList = new ArrayList<>();
        for (String plotId : plotIds) {
            TrialPlotRelation relation = new TrialPlotRelation();
            relation.setRelationId(IdUtil.simpleUUID());
            relation.setTrialId(trialId);
            relation.setGroundId(plotId);
            relation.setCreateTime(now);
            relationList.add(relation);
        }

        trialPlotRelationMapper.batchInsert(relationList);
    }

    /**
     * 获取季节名称（支持国际化）
     */
    private String getSeasonName(String season) {
        if (season == null || season.isEmpty()) {
            return "";
        }
        String messageKey = "season." + season.toLowerCase();
        try {
            return MessageUtils.message(messageKey);
        } catch (Exception e) {
            // 如果找不到对应的国际化key，返回原值
            return season;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitTrialForAudit(String trialId) {
        // 1. 获取试验信息
        TrialBasic trial = trialBasicMapper.selectById(trialId);
        if (trial == null) {
            throw new ServiceException("Trial information not found");
        }

        // 2. 检查试验状态（只有草稿和已退回状态可以提交）
        if (!"S0".equals(trial.getTrialStatus()) && !"S3".equals(trial.getTrialStatus())) {
            throw new ServiceException("Only trials with 'Draft' or 'Rejected' status can be submitted");
        }

        // 3. 获取当前用户信息
        String currentUserId = SecurityUtils.getUserId().toString();
        String currentUserName = SecurityUtils.getUsername();

        // 4. 更新试验状态为待审批
        String beforeStatus = trial.getTrialStatus();
        trial.setTrialStatus("S1");
        trial.setSubmittedBy(currentUserId);
        trial.setSubmittedName(currentUserName);
        trial.setSubmittedTime(LocalDateTime.now());
        trial.setUpdateTime(LocalDateTime.now());
        trial.setUpdateBy(currentUserId);
        trialBasicMapper.updateById(trial);

        // 5. 创建或更新审核记录
        com.inspur.seed.domain.entity.TrialBasicAudit audit = null;
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.inspur.seed.domain.entity.TrialBasicAudit> queryWrapper =
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.eq(com.inspur.seed.domain.entity.TrialBasicAudit::getTrialId, trialId)
                .eq(com.inspur.seed.domain.entity.TrialBasicAudit::getDeleted, "0")
                .orderByDesc(com.inspur.seed.domain.entity.TrialBasicAudit::getSubmitTime)
                .last("LIMIT 1");
        audit = trialBasicAuditMapper.selectOne(queryWrapper);

        if (audit != null && "S3".equals(audit.getAuditStatus())) {
            // 如果是退回后重新提交，更新现有审核记录
            audit.setAuditStatus("S1");
            audit.setAuditOpinion(null);
            audit.setRejectReason(null);
            audit.setAuditorId(null);
            audit.setAuditorName(null);
            audit.setAuditTime(null);
            audit.setSubmitTime(LocalDateTime.now());
            audit.setUpdateTime(LocalDateTime.now());
            audit.setUpdateBy(currentUserId);
            // 更新试验数据快照
            audit.setTrialDataSnapshot(cn.hutool.json.JSONUtil.toJsonStr(trial));
            trialBasicAuditMapper.updateById(audit);
        } else {
            // 首次提交，创建新审核记录
            audit = new com.inspur.seed.domain.entity.TrialBasicAudit();
            audit.setAuditId(IdUtil.simpleUUID());
            audit.setTrialId(trial.getTrialId());
            audit.setBatchId(trial.getBatchId());
            audit.setTrialName(trial.getTrialName());
            audit.setAuditNode("Trial Information Audit");
            audit.setAuditOrder(1);
            audit.setAuditStatus("S1");
            audit.setSubmitterId(currentUserId);
            audit.setSubmitterName(currentUserName);
            audit.setSubmitTime(LocalDateTime.now());
            audit.setTrialDataSnapshot(cn.hutool.json.JSONUtil.toJsonStr(trial));
            audit.setCreateTime(LocalDateTime.now());
            audit.setCreateBy(currentUserId);
            audit.setDeleted("0");
            trialBasicAuditMapper.insert(audit);
        }

        // 6. 记录审核历史
        com.inspur.seed.domain.entity.TrialBasicAuditHistory history = new com.inspur.seed.domain.entity.TrialBasicAuditHistory();
        history.setHistoryId(IdUtil.simpleUUID());
        history.setAuditId(audit.getAuditId());
        history.setTrialId(trial.getTrialId());
        history.setOperationType("SUBMIT");
        history.setOperationDesc("Submit trial for audit");
        history.setOperatorId(currentUserId);
        history.setOperatorName(currentUserName);
        history.setOperationTime(LocalDateTime.now());
        history.setBeforeStatus(beforeStatus);
        history.setAfterStatus("S1");
        history.setCreateTime(LocalDateTime.now());
        history.setDeleted("0");
        trialBasicAuditHistoryMapper.insert(history);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelTrial(String trialId, String cancelReason) {
        // 1. 获取试验信息
        TrialBasic trial = trialBasicMapper.selectById(trialId);
        if (trial == null) {
            throw new ServiceException("Trial information not found");
        }

        // 2. 检查试验状态（只有草稿和已退回状态可以作废）
        if (!"S0".equals(trial.getTrialStatus()) && !"S3".equals(trial.getTrialStatus())) {
            throw new ServiceException("Only trials with 'Draft' or 'Rejected' status can be cancelled");
        }

        // 3. 检查作废原因
        if (cancelReason == null || cancelReason.trim().isEmpty()) {
            throw new ServiceException("Cancellation reason is required");
        }

        // 4. 获取当前用户信息
        String currentUserId = SecurityUtils.getUserId().toString();
        String currentUserName = SecurityUtils.getUsername();

        // 5. 更新试验状态为作废
        String beforeStatus = trial.getTrialStatus();
        trial.setTrialStatus("S10");
        trial.setCancelledBy(currentUserId);
        trial.setCancelledName(currentUserName);
        trial.setCancelledTime(LocalDateTime.now());
        trial.setCancelReason(cancelReason);
        trial.setUpdateTime(LocalDateTime.now());
        trial.setUpdateBy(currentUserId);
        trialBasicMapper.updateById(trial);

        // 6. 如果存在审核记录，也需要标记
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.inspur.seed.domain.entity.TrialBasicAudit> queryWrapper =
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.eq(com.inspur.seed.domain.entity.TrialBasicAudit::getTrialId, trialId)
                .eq(com.inspur.seed.domain.entity.TrialBasicAudit::getDeleted, "0");
        com.inspur.seed.domain.entity.TrialBasicAudit audit = trialBasicAuditMapper.selectOne(queryWrapper);

        if (audit != null) {
            // 记录审核历史
            com.inspur.seed.domain.entity.TrialBasicAuditHistory history = new com.inspur.seed.domain.entity.TrialBasicAuditHistory();
            history.setHistoryId(IdUtil.simpleUUID());
            history.setAuditId(audit.getAuditId());
            history.setTrialId(trial.getTrialId());
            history.setOperationType("CANCEL");
            history.setOperationDesc("Cancel trial: " + cancelReason);
            history.setOperatorId(currentUserId);
            history.setOperatorName(currentUserName);
            history.setOperationTime(LocalDateTime.now());
            history.setBeforeStatus(beforeStatus);
            history.setAfterStatus("S10");
            history.setCreateTime(LocalDateTime.now());
            history.setDeleted("0");
            trialBasicAuditHistoryMapper.insert(history);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean archiveTrial(String trialId) {
        // 1. 获取试验信息
        TrialBasic trial = trialBasicMapper.selectById(trialId);
        if (trial == null) {
            throw new ServiceException("Trial information not found");
        }

        // 2. 检查试验状态（只有已审批状态可以归档）
        if (!"S2".equals(trial.getTrialStatus())) {
            throw new ServiceException("Only approved trials can be archived");
        }

        // 3. 获取当前用户信息
        String currentUserId = SecurityUtils.getUserId().toString();
        String currentUserName = SecurityUtils.getUsername();

        // 4. 更新试验状态为已归档
        String beforeStatus = trial.getTrialStatus();
        trial.setTrialStatus("S9");
        trial.setArchivedBy(currentUserId);
        trial.setArchivedName(currentUserName);
        trial.setArchivedTime(LocalDateTime.now());
        trial.setUpdateTime(LocalDateTime.now());
        trial.setUpdateBy(currentUserId);
        trialBasicMapper.updateById(trial);

        // 5. 记录审核历史
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.inspur.seed.domain.entity.TrialBasicAudit> queryWrapper =
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.eq(com.inspur.seed.domain.entity.TrialBasicAudit::getTrialId, trialId)
                .eq(com.inspur.seed.domain.entity.TrialBasicAudit::getDeleted, "0")
                .orderByDesc(com.inspur.seed.domain.entity.TrialBasicAudit::getSubmitTime)
                .last("LIMIT 1");
        com.inspur.seed.domain.entity.TrialBasicAudit audit = trialBasicAuditMapper.selectOne(queryWrapper);

        if (audit != null) {
            com.inspur.seed.domain.entity.TrialBasicAuditHistory history = new com.inspur.seed.domain.entity.TrialBasicAuditHistory();
            history.setHistoryId(IdUtil.simpleUUID());
            history.setAuditId(audit.getAuditId());
            history.setTrialId(trial.getTrialId());
            history.setOperationType("ARCHIVE");
            history.setOperationDesc("Archive trial");
            history.setOperatorId(currentUserId);
            history.setOperatorName(currentUserName);
            history.setOperationTime(LocalDateTime.now());
            history.setBeforeStatus(beforeStatus);
            history.setAfterStatus("S9");
            history.setCreateTime(LocalDateTime.now());
            history.setDeleted("0");
            trialBasicAuditHistoryMapper.insert(history);
        }

        return true;
    }
}
