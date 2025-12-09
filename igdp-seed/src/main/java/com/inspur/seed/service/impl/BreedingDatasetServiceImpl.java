package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.dto.BreedingDatasetDTO;
import com.inspur.seed.domain.dto.BreedingDatasetQueryDTO;
import com.inspur.seed.domain.entity.BreedingDataset;
import com.inspur.seed.domain.vo.BreedingDatasetVO;
import com.inspur.seed.mapper.BreedingDatasetMapper;
import com.inspur.seed.domain.entity.BreedingDatasetAudit;
import com.inspur.seed.mapper.BreedingDatasetAuditMapper;
import com.inspur.seed.service.IBreedingDatasetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 育种数据集Service实现类
 *
 * @author system
 * @date 2025-01-30
 */
@Slf4j
@Service
public class BreedingDatasetServiceImpl extends ServiceImpl<BreedingDatasetMapper, BreedingDataset>
        implements IBreedingDatasetService {

    @Autowired
    private BreedingDatasetAuditMapper auditMapper;

    @Override
    public AjaxResult getDatasetList(BreedingDatasetQueryDTO queryDTO) {
        try {
            // 构建查询条件
            QueryWrapper<BreedingDataset> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted", "0");

            // 批次ID
            if (StrUtil.isNotBlank(queryDTO.getBatchId())) {
                wrapper.eq("batch_id", queryDTO.getBatchId());
            }

            // 批次名称模糊查询
            if (StrUtil.isNotBlank(queryDTO.getBatchName())) {
                wrapper.like("batch_name", queryDTO.getBatchName());
            }

            // 作物类型
            if (StrUtil.isNotBlank(queryDTO.getCropType())) {
                wrapper.eq("crop_type", queryDTO.getCropType());
            }

            // 品种名称模糊查询
            if (StrUtil.isNotBlank(queryDTO.getVarietyName())) {
                wrapper.like("variety_name", queryDTO.getVarietyName());
            }

            // 数据集状态
            if (StrUtil.isNotBlank(queryDTO.getDatasetStatus())) {
                wrapper.eq("dataset_status", queryDTO.getDatasetStatus());
            }

            // 时间范围
            if (StrUtil.isNotBlank(queryDTO.getStartTime())) {
                wrapper.ge("created_time", queryDTO.getStartTime());
            }
            if (StrUtil.isNotBlank(queryDTO.getEndTime())) {
                wrapper.le("created_time", queryDTO.getEndTime());
            }

            // 按创建时间倒序
            wrapper.orderByDesc("created_time");

            // 分页查询
            Integer pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
            Integer pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
            Page<BreedingDataset> page = new Page<>(pageNum, pageSize);

            Page<BreedingDataset> result = this.page(page, wrapper);

            // 转换为VO
            List<BreedingDatasetVO> voList = result.getRecords().stream()
                    .map(entity -> BeanUtil.copyProperties(entity, BreedingDatasetVO.class))
                    .collect(Collectors.toList());

            Map<String, Object> data = new HashMap<>();
            data.put("list", voList);
            data.put("total", result.getTotal());

            return AjaxResult.success("Query successful", data);
        } catch (Exception e) {
            log.error("Failed to query breeding dataset list", e);
            return AjaxResult.error("Query failed: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getDatasetById(String id) {
        try {
            BreedingDataset dataset = this.getById(id);
            if (dataset == null || "1".equals(dataset.getDeleted())) {
                return AjaxResult.error("Dataset does not exist");
            }

            BreedingDatasetVO vo = BeanUtil.copyProperties(dataset, BreedingDatasetVO.class);
            return AjaxResult.success("Query successful", vo);
        } catch (Exception e) {
            log.error("Failed to query breeding dataset details", e);
            return AjaxResult.error("Query failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addDataset(BreedingDatasetDTO dto) {
        try {
            // 验证必填字段
            if (StrUtil.isBlank(dto.getBatchId())) {
                return AjaxResult.error("Batch ID cannot be empty");
            }

            BreedingDataset dataset = new BreedingDataset();
            BeanUtil.copyProperties(dto, dataset);

            // 版本号在提交时生成，新增时不设置
            dataset.setVersionNo(null);

            // 设置默认值
            dataset.setId(IdUtil.simpleUUID());
            dataset.setDatasetStatus("draft");
            dataset.setTrialCount(0);
            dataset.setFieldDataCount(0);
            dataset.setEnvDataCount(0);
            dataset.setLabTestCount(0);
            dataset.setYieldDataCount(0);
            dataset.setStatus("1");
            dataset.setDeleted("0");
            dataset.setCreatedTime(LocalDateTime.now());

            // TODO: 设置创建人信息(从当前登录用户获取)
            dataset.setCreatedBy(SecurityUtils.getUsername());
            // dataset.setCreatedBy(currentUserId);
            // dataset.setCreatedByName(currentUserName);
            // dataset.setCreatedOrgCode(currentUserOrgCode);
            // dataset.setCreatedOrgName(currentUserOrgName);

            this.save(dataset);

            return AjaxResult.success("Added successfully", dataset.getId());
        } catch (Exception e) {
            log.error("Failed to add breeding dataset", e);
            return AjaxResult.error("Add failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateDataset(BreedingDatasetDTO dto) {
        try {
            if (StrUtil.isBlank(dto.getId())) {
                return AjaxResult.error("Dataset ID cannot be empty");
            }

            BreedingDataset dataset = this.getById(dto.getId());
            if (dataset == null || "1".equals(dataset.getDeleted())) {
                return AjaxResult.error("Dataset does not exist");
            }

            // 检查数据集是否已被锁定（只有审核通过且锁定的数据集才不可修改）
            if ("approved".equals(dataset.getDatasetStatus())) {
                QueryWrapper<BreedingDatasetAudit> auditWrapper = new QueryWrapper<>();
                auditWrapper.eq("dataset_id", dto.getId());
                auditWrapper.eq("deleted", "0");
                auditWrapper.eq("audit_status", "approved");
                auditWrapper.orderByDesc("created_time");
                auditWrapper.last("LIMIT 1");
                BreedingDatasetAudit audit = auditMapper.selectOne(auditWrapper);

                if (audit != null && audit.getLockedFlag() != null && audit.getLockedFlag() == 1) {
                    log.warn("Attempt to modify locked approved dataset: {}", dto.getId());
                    return AjaxResult.error("Dataset is approved and locked, cannot be modified. Please contact administrator if you need to unlock it.");
                }
                // 如果审核通过但未锁定（locked_flag=0或NULL），允许修改
            }

            // 提交中或审核中的数据集不允许修改
            if ("submitted".equals(dataset.getDatasetStatus()) || "reviewing".equals(dataset.getDatasetStatus())) {
                return AjaxResult.error("Submitted or reviewing datasets cannot be modified");
            }

            // 更新字段(允许修改批次ID和冗余字段)
            if (StrUtil.isNotBlank(dto.getTrialId())) {
                dataset.setTrialId(dto.getTrialId());
            }
            if (StrUtil.isNotBlank(dto.getBatchId())) {
                dataset.setBatchId(dto.getBatchId());
            }
            if (StrUtil.isNotBlank(dto.getBatchName())) {
                dataset.setBatchName(dto.getBatchName());
            }
            if (StrUtil.isNotBlank(dto.getCropType())) {
                dataset.setCropType(dto.getCropType());
            }
            if (StrUtil.isNotBlank(dto.getVarietyName())) {
                dataset.setVarietyName(dto.getVarietyName());
            }
            // 版本号由系统自动管理，不允许手动修改
            // if (StrUtil.isNotBlank(dto.getVersionNo())) {
            //     dataset.setVersionNo(dto.getVersionNo());
            // }
            if (dto.getRecordCount() != null) {
                dataset.setRecordCount(dto.getRecordCount());
            }
            if (dto.getCompiledAt() != null) {
                dataset.setCompiledAt(dto.getCompiledAt());
            }
            if (StrUtil.isNotBlank(dto.getRemark())) {
                dataset.setRemark(dto.getRemark());
            }

            dataset.setUpdatedTime(LocalDateTime.now());
            // TODO: 设置更新人信息
            // dataset.setUpdatedBy(currentUserId);

            this.updateById(dataset);

            return AjaxResult.success("Modified successfully");
        } catch (Exception e) {
            log.error("Failed to modify breeding dataset", e);
            return AjaxResult.error("Modification failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteDataset(String[] ids) {
        try {
            if (ids == null || ids.length == 0) {
                return AjaxResult.error("Please select datasets to delete");
            }

            List<BreedingDataset> datasets = this.listByIds(Arrays.asList(ids));

            if (datasets == null || datasets.isEmpty()) {
                return AjaxResult.error("Dataset does not exist");
            }

            // 检查状态和锁定标记
            for (BreedingDataset dataset : datasets) {
                // 检查数据集是否已被锁定（只有审核通过且锁定的数据集才不可删除）
                if ("approved".equals(dataset.getDatasetStatus())) {
                    QueryWrapper<BreedingDatasetAudit> auditWrapper = new QueryWrapper<>();
                    auditWrapper.eq("dataset_id", dataset.getId());
                    auditWrapper.eq("deleted", "0");
                    auditWrapper.eq("audit_status", "approved");
                    auditWrapper.orderByDesc("created_time");
                    auditWrapper.last("LIMIT 1");
                    BreedingDatasetAudit audit = auditMapper.selectOne(auditWrapper);

                    if (audit != null && audit.getLockedFlag() != null && audit.getLockedFlag() == 1) {
                        log.warn("Attempt to delete locked approved dataset: {}", dataset.getId());
                        return AjaxResult.error("Dataset is approved and locked, cannot be deleted. Dataset code: " + dataset.getDatasetCode());
                    }
                    // 如果审核通过但未锁定（locked_flag=0或NULL），允许删除
                }

                // 检查数据集状态（草稿、驳回、需要修订的数据集可以删除）
                if (!"draft".equals(dataset.getDatasetStatus())
                        && !"rejected".equals(dataset.getDatasetStatus())
                        && !"needs_revision".equals(dataset.getDatasetStatus())
                        && !"approved".equals(dataset.getDatasetStatus())) {
                    return AjaxResult.error("Only draft, rejected, needs_revision or unlocked approved datasets can be deleted, current status: " + dataset.getDatasetStatus());
                }
            }

            // 使用MyBatis-Plus的removeByIds方法进行逻辑删除数据集
            boolean success = this.removeByIds(Arrays.asList(ids));

            if (!success) {
                log.error("Failed to delete datasets");
                return AjaxResult.error("Delete failed");
            }

            // 同步删除关联的审核记录
            for (String datasetId : ids) {
                try {
                    QueryWrapper<BreedingDatasetAudit> auditWrapper = new QueryWrapper<>();
                    auditWrapper.eq("dataset_id", datasetId);
                    auditWrapper.eq("deleted", "0");

                    List<BreedingDatasetAudit> audits = auditMapper.selectList(auditWrapper);

                    if (audits != null && !audits.isEmpty()) {
                        // 逻辑删除审核记录
                        for (BreedingDatasetAudit audit : audits) {
                            audit.setDeleted("1");
                            audit.setUpdatedTime(LocalDateTime.now());
                            auditMapper.updateById(audit);
                        }
                        log.info("Deleted {} audit records for dataset {}", audits.size(), datasetId);
                    }
                } catch (Exception e) {
                    log.warn("Failed to delete audit records for dataset {}: {}", datasetId, e.getMessage());
                    // 继续处理其他记录，不中断整个删除流程
                }
            }

            log.info("Successfully deleted {} dataset records and their audit records", ids.length);
            return AjaxResult.success("Deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete breeding datasets", e);
            return AjaxResult.error("Delete failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public  AjaxResult submitDataset(String id) {
        try {
            BreedingDataset dataset = this.getById(id);
            if (dataset == null || "1".equals(dataset.getDeleted())) {
                return AjaxResult.error("Dataset does not exist");
            }

            if (!"draft".equals(dataset.getDatasetStatus()) && !"rejected".equals(dataset.getDatasetStatus())) {
                return AjaxResult.error("Only draft or rejected datasets can be submitted");
            }

            // 检查数据完整性 - 放宽限制，允许暂无数据时提交
            // if (dataset.getLabTestCount() == null || dataset.getLabTestCount() == 0) {
            //     return AjaxResult.error("At least one laboratory test record is required for submission");
            // }
            // if (dataset.getYieldDataCount() == null || dataset.getYieldDataCount() == 0) {
            //     return AjaxResult.error("At least one yield data record is required for submission");
            // }

            // 自动生成版本号：每次提交都递增（包括首次提交和退回后重新提交）
            if (StrUtil.isNotBlank(dataset.getTrialId())) {
                Integer maxVersionNo = this.baseMapper.selectMaxVersionNoByTrialId(dataset.getTrialId());
                // 如果没有记录，从1开始；否则最大版本号+1
                dataset.setVersionNo(maxVersionNo == null ? 1 : maxVersionNo + 1);
                log.info("为数据集生成版本号: trialId={}, versionNo={}", dataset.getTrialId(), dataset.getVersionNo());
            } else {
                // 如果没有试验ID，默认版本号为1
                dataset.setVersionNo(1);
                log.warn("数据集没有试验ID，版本号设为1, datasetId={}", id);
            }

            // 更新状态
            dataset.setDatasetStatus("submitted");
            LocalDateTime submitTime = LocalDateTime.now();
            dataset.setSubmitTime(submitTime);
            dataset.setCreatedBy(SecurityUtils.getUsername());


            // 设置提交人信息(从当前登录用户获取)
            String submitBy = null;
            String submitByName = null;
            try {
                submitBy = SecurityUtils.getUsername();
                submitByName = submitBy; // 如果没有用户名,使用用户ID
                dataset.setSubmitBy(submitBy);
                dataset.setSubmitByName(submitByName);
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败,使用创建人信息", ex);
                // 使用创建人信息作为提交人
                submitBy = dataset.getCreatedBy();
                submitByName = dataset.getCreatedByName() != null ? dataset.getCreatedByName() : dataset.getCreatedBy();
                dataset.setSubmitBy(submitBy);
                dataset.setSubmitByName(submitByName);
            }

            dataset.setUpdatedTime(LocalDateTime.now());

            // 更新数据集
            boolean updateSuccess = this.updateById(dataset);
            if (!updateSuccess) {
                log.error("Failed to update dataset status, dataset ID: {}", id);
                return AjaxResult.error("Submission failed: Failed to update dataset status");
            }

            // 查询是否已存在审核记录
            QueryWrapper<BreedingDatasetAudit> wrapper = new QueryWrapper<>();
            wrapper.eq("dataset_id", dataset.getId());
            wrapper.eq("deleted", "0");
            wrapper.orderByDesc("created_time");
            wrapper.last("LIMIT 1");
            BreedingDatasetAudit existingAudit = auditMapper.selectOne(wrapper);

            if (existingAudit != null) {
                // 如果是被驳回后重新提交,更新现有审核记录
                existingAudit.setAuditStatus("pending"); // 重置为待审核
                existingAudit.setAuditOpinion(null); // 清空之前的审核意见
                existingAudit.setAuditTime(null); // 清空审核时间
                existingAudit.setAuditorId(null); // 清空审核人
                existingAudit.setAuditorName(null);
                existingAudit.setSubmitTime(submitTime); // 更新提交时间
                existingAudit.setSubmitterId(submitBy);
                existingAudit.setSubmitterName(submitByName);
                existingAudit.setUpdatedTime(LocalDateTime.now());
                existingAudit.setUpdatedBy(submitBy);

                int updateResult = auditMapper.updateById(existingAudit);
                if (updateResult <= 0) {
                    log.error("Failed to update audit record, dataset ID: {}", id);
                    throw new RuntimeException("Failed to update audit record");
                }
                log.info("Dataset resubmitted successfully, dataset ID: {}, audit record ID: {}", id, existingAudit.getId());
            } else {
                // 首次提交,创建新的审核记录
                BreedingDatasetAudit audit = new BreedingDatasetAudit();
                audit.setId(IdUtil.simpleUUID());
                audit.setDatasetId(dataset.getId());
                audit.setBatchId(dataset.getBatchId());
                audit.setAuditNode("数据集审核");
                audit.setAuditOrder(1);
                audit.setAuditStatus("pending");
                audit.setSubmitTime(submitTime);
                audit.setSubmitterId(submitBy);
                audit.setSubmitterName(submitByName);
                audit.setStatus("1");
                audit.setDeleted("0");
                audit.setCreatedTime(LocalDateTime.now());
                audit.setCreatedBy(submitBy); // 设置创建人
                audit.setUpdatedBy(submitBy); // 设置更新人
                audit.setUpdatedTime(LocalDateTime.now()); // 设置更新时间

                int auditInsertResult = auditMapper.insert(audit);
                if (auditInsertResult <= 0) {
                    log.error("Failed to create audit record, dataset ID: {}", id);
                    throw new RuntimeException("Failed to create audit record");
                }
                log.info("Dataset submitted successfully, dataset ID: {}, audit record ID: {}", id, audit.getId());
            }

            return AjaxResult.success("Submitted successfully");
        } catch (Exception e) {
            log.error("Failed to submit breeding dataset", e);
            return AjaxResult.error("Submission failed: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult statisticsData(String batchId) {
        try {
            if (StrUtil.isBlank(batchId)) {
                return AjaxResult.error("Breeding batch ID cannot be empty");
            }

            Map<String, Object> statistics = new HashMap<>();

            // TODO: 统计各项数据记录数
            // 1. 查询试验记录数 (从breeding_trial表)
            // 2. 查询田间数据记录数 (从相关表)
            // 3. 查询环境数据记录数 (从相关表)
            // 4. 查询实验室检测记录数 (从breeding_lab_test表)
            // 5. 查询产量数据记录数 (从breeding_yield_data表)

            // 暂时返回模拟数据
            statistics.put("trialCount", 0);
            statistics.put("fieldDataCount", 0);
            statistics.put("envDataCount", 0);
            statistics.put("labTestCount", 0);
            statistics.put("yieldDataCount", 0);

            return AjaxResult.success("Statistics generated successfully", statistics);
        } catch (Exception e) {
            log.error("Failed to generate statistics", e);
            return AjaxResult.error("Statistics failed: " + e.getMessage());
        }
    }
}
