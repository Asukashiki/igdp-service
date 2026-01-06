package com.inspur.seed.breeding.agronomicTrait.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.inspur.seed.breeding.agronomicTrait.domain.entity.AgronomicTraitAudit;
import com.inspur.seed.breeding.agronomicTrait.domain.dto.AgronomicTraitDetailDTO;
import com.inspur.seed.breeding.agronomicTrait.domain.dto.AgronomicTraitRecordDTO;
import com.inspur.seed.breeding.agronomicTrait.domain.entity.AgronomicTraitDetail;
import com.inspur.seed.breeding.agronomicTrait.domain.entity.AgronomicTraitRecord;
import com.inspur.seed.breeding.agronomicTrait.domain.vo.AgronomicTraitDetailVO;
import com.inspur.seed.breeding.agronomicTrait.domain.vo.AgronomicTraitRecordVO;
import com.inspur.seed.breeding.agronomicTrait.mapper.AgronomicTraitDetailMapper;
import com.inspur.seed.breeding.agronomicTrait.mapper.AgronomicTraitRecordMapper;
import com.inspur.seed.breeding.agronomicTrait.mapper.AgronomicTraitAuditMapper;

import com.inspur.seed.breeding.agronomicTrait.service.IAgronomicTraitRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.dev33.satoken.SaManager.log;

/**
 * 农艺性状采集主记录Service实现
 *
 * @author inspur
 */
@Service
public class AgronomicTraitRecordServiceImpl implements IAgronomicTraitRecordService {

    @Autowired
    private AgronomicTraitRecordMapper recordMapper;

    @Autowired
    private AgronomicTraitAuditMapper traitAuditMapper;

    @Autowired
    private AgronomicTraitDetailMapper detailMapper;

    @Override
    public List<AgronomicTraitRecord> selectRecordList(AgronomicTraitRecord record) {
        return recordMapper.selectRecordList(record);
    }

    @Override
    public AgronomicTraitRecordVO selectRecordById(String recordId) {
        // 查询主记录（含明细）
        AgronomicTraitRecord record = recordMapper.selectRecordByIdWithDetails(recordId);
        if (record == null) {
            return null;
        }

        // 转换为VO
        AgronomicTraitRecordVO vo = new AgronomicTraitRecordVO();
        BeanUtils.copyProperties(record, vo);

        // 转换明细列表
        if (!CollectionUtils.isEmpty(record.getDetailList())) {
            List<AgronomicTraitDetailVO> detailVOList = record.getDetailList().stream()
                    .map(detail -> {
                        AgronomicTraitDetailVO detailVO = new AgronomicTraitDetailVO();
                        BeanUtils.copyProperties(detail, detailVO);
                        return detailVO;
                    })
                    .collect(Collectors.toList());
            vo.setDetailList(detailVOList);
            vo.setTraitCount(detailVOList.size());
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertRecord(AgronomicTraitRecordDTO dto) {
        String username = SecurityUtils.getUsername();
        LocalDateTime now = LocalDateTime.now();

        // 生成记录ID
        String recordId = generateRecordId(dto.getPlotId());

        // 构建主记录实体
        AgronomicTraitRecord record = new AgronomicTraitRecord();
        BeanUtils.copyProperties(dto, record);
        record.setRecordId(recordId);
        record.setCreateBy(username);
        record.setCreateTime(now);
        record.setUpdateBy(username);
        record.setUpdateTime(now);

        // 默认状态
        if (record.getStatus() == null) {
            record.setStatus("draft");
        }
        // 同步设置 workflowStatus（用于前端显示和搜索）
        if (record.getWorkflowStatus() == null) {
            record.setWorkflowStatus("draft");
        }

        // 插入主记录
        recordMapper.insert(record);

        // 插入明细列表
        if (!CollectionUtils.isEmpty(dto.getDetailList())) {
            insertDetailList(recordId, dto.getDetailList(), username, now);
        }

        return recordId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRecord(AgronomicTraitRecordDTO dto) {
        String username = SecurityUtils.getUsername();
        LocalDateTime now = LocalDateTime.now();

        // 更新主记录
        AgronomicTraitRecord record = new AgronomicTraitRecord();
        BeanUtils.copyProperties(dto, record);
        record.setUpdateBy(username);
        record.setUpdateTime(now);

        int result = recordMapper.updateById(record);

        // 删除旧明细
        detailMapper.deleteDetailsByRecordId(dto.getRecordId());

        // 插入新明细
        if (!CollectionUtils.isEmpty(dto.getDetailList())) {
            insertDetailList(dto.getRecordId(), dto.getDetailList(), username, now);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRecordByIds(String[] recordIds) {
        LocalDateTime now = LocalDateTime.now();
        int count = 0;

        for (String recordId : recordIds) {
            // 逻辑删除主记录
            LambdaUpdateWrapper<AgronomicTraitRecord> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(AgronomicTraitRecord::getRecordId, recordId)
                    .set(AgronomicTraitRecord::getIsDeleted, 1)
                    .set(AgronomicTraitRecord::getUpdateTime, now);
            count += recordMapper.update(null, wrapper);

            // 逻辑删除明细
            detailMapper.deleteDetailsByRecordId(recordId);
        }

        return count;
    }

    @Override
    public String generateRecordId(String plotId) {
        Integer sequence = recordMapper.generateRecordSequence(plotId);
        if (sequence == null) {
            sequence = 1;
        }
        return String.format("%s-TR%03d", plotId, sequence);
    }

    /**
     * 插入明细列表
     */
    private void insertDetailList(String recordId, List<AgronomicTraitDetailDTO> detailDTOList,
                                   String username, LocalDateTime now) {
        List<AgronomicTraitDetail> detailList = new ArrayList<>();

        // 获取起始序号（基于所有记录，包括已删除的）
        Integer startSequence = detailMapper.generateDetailSequence(recordId);
        if (startSequence == null) {
            startSequence = 1;
        }

        for (int i = 0; i < detailDTOList.size(); i++) {
            AgronomicTraitDetailDTO detailDTO = detailDTOList.get(i);

            // 生成明细ID，使用数据库序号
            String detailId = String.format("%s-D%03d", recordId, startSequence + i);

            AgronomicTraitDetail detail = new AgronomicTraitDetail();
            BeanUtils.copyProperties(detailDTO, detail);
            detail.setDetailId(detailId);
            detail.setRecordId(recordId);
            detail.setSortOrder(i + 1);
            detail.setCreateBy(username);
            detail.setCreateTime(now);
            detail.setUpdateBy(username);
            detail.setUpdateTime(now);

            detailList.add(detail);
        }

        if (!detailList.isEmpty()) {
            detailMapper.batchInsertDetails(detailList);
        }
    }

    /**
     * 农艺性状提交审核
     * 参考 BreedingBatchServiceImpl 的 submitAudit 方法实现
     * @param recordId 农艺性状主键ID
     * @return AjaxResult 统一返回结果
     */
    /**
     * 农艺性状提交审核（适配自定义状态：draft/submitted/reviewing/approved/rejected，英文提示）
     * @param recordId 农艺性状记录ID
     * @return AjaxResult
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult submitAgronomicTraitAudit(String recordId) {
        try {
            if (StrUtil.isBlank(recordId)) {
                return AjaxResult.error("recordId cannot be blank");
            }

            // 第一步：前置校验（查询主记录）
            AgronomicTraitRecord agronomicTraitRecord = recordMapper.selectById(recordId);
            // 判断记录是否存在或已逻辑删除
            if (agronomicTraitRecord == null || (agronomicTraitRecord.getIsDeleted() != null && agronomicTraitRecord.getIsDeleted() == 1)) {
                log.warn("Agronomic trait submit audit failed: record does not exist or has been logically deleted, record_id={}", recordId);
                return AjaxResult.error("Agronomic trait record does not exist or has been deleted");
            }

            // 第二步：状态合法性校验（仅允许 草稿(draft) / 已驳回(rejected) 提交）
            String currentStatus = agronomicTraitRecord.getStatus();
            // 移除无用的W0状态，只保留合法可提交状态
            if (!"draft".equals(currentStatus) && !"rejected".equals(currentStatus)) {
                log.warn("Agronomic trait submit audit failed: not in allowable submit status, record_id={}, currentStatus={}", recordId, currentStatus);
                return AjaxResult.error("Only records in draft or rejected status can be submitted for audit");
            }

            // 第三步：主表核心更新（状态改为 submitted(已提交)，适配你的状态定义）
            LocalDateTime submitTime = LocalDateTime.now();
            String loginUser = SecurityUtils.getUsername();
            // 关键修改：将S1改为 submitted（你的已提交状态）
            agronomicTraitRecord.setStatus("submitted");
            // 同步设置 workflowStatus
            agronomicTraitRecord.setWorkflowStatus("submitted");
            agronomicTraitRecord.setUpdateBy(loginUser);
            agronomicTraitRecord.setUpdateTime(submitTime);

            // 执行主表更新
            int affectedRows = recordMapper.updateById(agronomicTraitRecord);
            boolean updateSuccess = affectedRows > 0;
            if (!updateSuccess) {
                log.error("Agronomic trait submit audit failed: failed to update main table status, record_id={}, affectedRows={}", recordId, affectedRows);
                return AjaxResult.error("Failed to submit for audit: failed to update record status");
            }

            // 第四步：获取提交人信息
            String submitterId = null;
            String submitterName = null;
            try {
                submitterId = SecurityUtils.getUsername();
                submitterName = submitterId; // 若有用户名查询逻辑，可在此处扩展
            } catch (Exception ex) {
                log.warn("Failed to get current user info, using record creator as submitter, record_id={}", recordId, ex);
                submitterId = agronomicTraitRecord.getCreateBy();
                submitterName = submitterId;
            }

            // 第五步：审核表操作（新增/更新审核记录，保持原核心逻辑）
            QueryWrapper<AgronomicTraitAudit> wrapper = new QueryWrapper<>();
            wrapper.eq("trait_id", recordId);
            wrapper.eq("deleted", "0");
            wrapper.orderByDesc("created_time");
            wrapper.last("LIMIT 1");

            AgronomicTraitAudit existingAudit = traitAuditMapper.selectOne(wrapper);
            if (existingAudit != null) {
                // 场景1：存在历史审核记录（驳回后重新提交，重置审核状态为待审核）
                existingAudit.setAuditStatus("pending"); // pending对应你的reviewing（审核中），后续审核时可更新为reviewing
                existingAudit.setAuditOpinion(null);
                existingAudit.setAuditTime(null);
                existingAudit.setAuditorId(null);
                existingAudit.setAuditorName(null);
                existingAudit.setAuditorOrgCode(null);
                existingAudit.setAuditorOrgName(null);
                existingAudit.setSubmitTime(submitTime);
                existingAudit.setSubmitterId(submitterId);
                existingAudit.setSubmitterName(submitterName);
                existingAudit.setUpdatedBy(submitterId);
                existingAudit.setUpdatedTime(LocalDateTime.now());

                int updateResult = traitAuditMapper.updateById(existingAudit);
                if (updateResult <= 0) {
                    log.error("Agronomic trait submit audit failed: failed to update audit record, record_id={}, audit_id={}", recordId, existingAudit.getId());
                    throw new RuntimeException("Failed to submit for audit: failed to update audit record");
                }
                log.info("Agronomic trait re-submitted for audit successfully, record_id={}, audit record ID={}", recordId, existingAudit.getId());
            } else {
                // 场景2：无历史审核记录（首次提交，新增审核记录）
                AgronomicTraitAudit traitAudit = new AgronomicTraitAudit();
                String auditId = IdUtil.simpleUUID();
                traitAudit.setId(auditId);
                traitAudit.setTraitId(recordId);
                traitAudit.setBatchId(agronomicTraitRecord.getBatchId());
                traitAudit.setAuditNode("Agronomic Trait Audit");
                traitAudit.setAuditOrder(1);
                traitAudit.setAuditStatus("pending"); // 待审核，后续审核流程可改为reviewing（审核中）
                traitAudit.setAuditOpinion(null);
                traitAudit.setAuditTime(null);
                traitAudit.setAuditorId(null);
                traitAudit.setAuditorName(null);
                traitAudit.setAuditorOrgCode(null);
                traitAudit.setAuditorOrgName(null);
                traitAudit.setLockedFlag(0);
                traitAudit.setSubmitTime(submitTime);
                traitAudit.setSubmitterId(submitterId);
                traitAudit.setSubmitterName(submitterName);
                traitAudit.setStatus("1");
                traitAudit.setCreatedBy(submitterId);
                traitAudit.setCreatedTime(LocalDateTime.now());
                traitAudit.setUpdatedBy(submitterId);
                traitAudit.setUpdatedTime(LocalDateTime.now());
                traitAudit.setRemark(null);
                traitAudit.setDeleted("0");

                int auditInsertResult = traitAuditMapper.insert(traitAudit);
                if (auditInsertResult <= 0) {
                    log.error("Agronomic trait submit audit failed: failed to create audit record, record_id={}", recordId);
                    throw new RuntimeException("Failed to submit for audit: failed to create audit record");
                }
                log.info("Agronomic trait submitted for audit successfully for the first time, record_id={}, audit record ID={}", recordId, traitAudit.getId());
            }

            return AjaxResult.success("Agronomic trait submitted for audit successfully");
        } catch (Exception e) {
            log.error("Agronomic trait submit audit failed, record_id={}", recordId, e);
            return AjaxResult.error("Failed to submit for audit: " + e.getMessage());
        }
    }
}
