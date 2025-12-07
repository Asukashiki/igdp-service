package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.dto.BreedingDatasetAuditDTO;
import com.inspur.seed.domain.dto.BreedingDatasetAuditQueryDTO;
import com.inspur.seed.domain.entity.BreedingDataset;
import com.inspur.seed.domain.entity.BreedingDatasetAudit;
import com.inspur.seed.domain.vo.BreedingDatasetAuditVO;
import com.inspur.seed.mapper.BreedingDatasetAuditMapper;
import com.inspur.seed.mapper.BreedingDatasetMapper;
import com.inspur.seed.service.IBreedingDatasetAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 育种数据集审核Service实现类
 *
 * @author system
 * @since 2025-01-30
 */
@Slf4j
@Service
public class BreedingDatasetAuditServiceImpl extends ServiceImpl<BreedingDatasetAuditMapper, BreedingDatasetAudit>
        implements IBreedingDatasetAuditService {

    @Autowired
    private BreedingDatasetMapper datasetMapper;

    @Override
    public AjaxResult getAuditList(BreedingDatasetAuditQueryDTO queryDTO) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<BreedingDatasetAudit> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BreedingDatasetAudit::getDeleted, "0");

            // 审核状态
            if (StrUtil.isNotBlank(queryDTO.getAuditStatus())) {
                wrapper.eq(BreedingDatasetAudit::getAuditStatus, queryDTO.getAuditStatus());
            }

            // 批次ID
            if (StrUtil.isNotBlank(queryDTO.getBatchId())) {
                wrapper.eq(BreedingDatasetAudit::getBatchId, queryDTO.getBatchId());
            }

            // 提交时间范围
            if (StrUtil.isNotBlank(queryDTO.getSubmitTimeStart())) {
                wrapper.ge(BreedingDatasetAudit::getSubmitTime, queryDTO.getSubmitTimeStart());
            }
            if (StrUtil.isNotBlank(queryDTO.getSubmitTimeEnd())) {
                wrapper.le(BreedingDatasetAudit::getSubmitTime, queryDTO.getSubmitTimeEnd());
            }

            // 按提交时间倒序
            wrapper.orderByDesc(BreedingDatasetAudit::getSubmitTime);

            // 分页查询
            Integer pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
            Integer pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
            Page<BreedingDatasetAudit> page = new Page<>(pageNum, pageSize);

            // 执行查询
            Page<BreedingDatasetAudit> result = this.page(page, wrapper);

            // 转换为VO并关联数据集信息
            List<BreedingDatasetAuditVO> voList = result.getRecords().stream()
                    .map(audit -> {
                        BreedingDatasetAuditVO vo = BeanUtil.copyProperties(audit, BreedingDatasetAuditVO.class);

                        // 查询关联的数据集信息
                        if (StrUtil.isNotBlank(audit.getDatasetId())) {
                            BreedingDataset dataset = datasetMapper.selectById(audit.getDatasetId());
                            if (dataset != null) {
                                // 数据集基础信息
                                vo.setDatasetCode(dataset.getDatasetCode());
                                vo.setTrialId(dataset.getTrialId());
                                vo.setBatchName(dataset.getBatchName());
                                vo.setCropType(dataset.getCropType());
                                vo.setVarietyName(dataset.getVarietyName());
                                vo.setVersionNo(dataset.getVersionNo());
                                vo.setRecordCount(dataset.getRecordCount());
                                vo.setDatasetStatus(dataset.getDatasetStatus());

                                // 编制人信息
                                vo.setCompiledBy(dataset.getCompiledBy());
                                vo.setCompiledByName(dataset.getCreatedByName());
                                vo.setCompiledAt(dataset.getCompiledAt());

                                // 统计数据
                                vo.setTrialCount(dataset.getTrialCount());
                                vo.setFieldDataCount(dataset.getFieldDataCount());
                                vo.setEnvDataCount(dataset.getEnvDataCount());
                                vo.setLabTestCount(dataset.getLabTestCount());
                                vo.setYieldDataCount(dataset.getYieldDataCount());
                            }
                        }

                        return vo;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> data = new HashMap<>();
            data.put("list", voList);
            data.put("total", result.getTotal());

            return AjaxResult.success("Query successful", data);
        } catch (Exception e) {
            log.error("Failed to query audit list", e);
            return AjaxResult.error("Query failed: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getAuditById(String id) {
        try {
            BreedingDatasetAudit audit = this.getById(id);
            if (audit == null || "1".equals(audit.getDeleted())) {
                return AjaxResult.error("Audit record does not exist");
            }

            // 查询数据集信息
            BreedingDataset dataset = datasetMapper.selectById(audit.getDatasetId());
            if (dataset == null) {
                return AjaxResult.error("Dataset does not exist");
            }

            // 组装VO
            BreedingDatasetAuditVO vo = BeanUtil.copyProperties(audit, BreedingDatasetAuditVO.class);

            // 数据集基础信息
            vo.setDatasetCode(dataset.getDatasetCode());
            vo.setTrialId(dataset.getTrialId());
            vo.setBatchName(dataset.getBatchName());
            vo.setCropType(dataset.getCropType());
            vo.setVarietyName(dataset.getVarietyName());
            vo.setVersionNo(dataset.getVersionNo());
            vo.setRecordCount(dataset.getRecordCount());
            vo.setDatasetStatus(dataset.getDatasetStatus());

            // 编制人信息
            vo.setCompiledBy(dataset.getCompiledBy());
            vo.setCompiledByName(dataset.getCreatedByName());
            vo.setCompiledAt(dataset.getCompiledAt());

            // 统计数据
            vo.setTrialCount(dataset.getTrialCount());
            vo.setFieldDataCount(dataset.getFieldDataCount());
            vo.setEnvDataCount(dataset.getEnvDataCount());
            vo.setLabTestCount(dataset.getLabTestCount());
            vo.setYieldDataCount(dataset.getYieldDataCount());

            return AjaxResult.success("Query successful", vo);
        } catch (Exception e) {
            log.error("Failed to query audit details", e);
            return AjaxResult.error("Query failed: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getAuditByDatasetId(String datasetId) {
        try {
            QueryWrapper<BreedingDatasetAudit> wrapper = new QueryWrapper<>();
            wrapper.eq("dataset_id", datasetId);
            wrapper.eq("deleted", "0");
            wrapper.orderByDesc("created_time");
            wrapper.last("LIMIT 1");

            BreedingDatasetAudit audit = this.getOne(wrapper);
            if (audit == null) {
                return AjaxResult.error("Audit record does not exist");
            }

            return getAuditById(audit.getId());
        } catch (Exception e) {
            log.error("Failed to query audit details by dataset ID", e);
            return AjaxResult.error("Query failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult performAudit(BreedingDatasetAuditDTO auditDTO) {
        try {
            // 1. 校验数据集
            BreedingDataset dataset = datasetMapper.selectById(auditDTO.getDatasetId());
            if (dataset == null || "1".equals(dataset.getDeleted())) {
                return AjaxResult.error("Dataset does not exist");
            }

            // 2. 校验数据集状态(必须是已提交或审核中)
            if (!"submitted".equals(dataset.getDatasetStatus()) && !"reviewing".equals(dataset.getDatasetStatus())) {
                return AjaxResult.error("Only submitted or reviewing datasets can be audited");
            }

            // 3. 校验审核状态
            if (!"approved".equals(auditDTO.getAuditStatus())
                    && !"rejected".equals(auditDTO.getAuditStatus())
                    && !"needs_revision".equals(auditDTO.getAuditStatus())) {
                return AjaxResult.error("Audit status must be approved, rejected or needs_revision");
            }

            // 4. 驳回或需要修订时必须填写审核意见
            if (("rejected".equals(auditDTO.getAuditStatus()) || "needs_revision".equals(auditDTO.getAuditStatus()))
                    && StrUtil.isBlank(auditDTO.getAuditOpinion())) {
                return AjaxResult.error("Audit opinion is required when rejecting or marking as needs revision");
            }

            // 5. 查询或创建审核记录
            QueryWrapper<BreedingDatasetAudit> wrapper = new QueryWrapper<>();
            wrapper.eq("dataset_id", auditDTO.getDatasetId());
            wrapper.eq("deleted", "0");
            wrapper.orderByDesc("created_time");
            wrapper.last("LIMIT 1");

            BreedingDatasetAudit audit = this.getOne(wrapper);
            if (audit == null) {
                // 创建新的审核记录
                audit = new BreedingDatasetAudit();
                audit.setId(IdUtil.simpleUUID());
                audit.setDatasetId(auditDTO.getDatasetId());
                audit.setBatchId(dataset.getBatchId());
                audit.setAuditNode(auditDTO.getAuditNode());
                audit.setAuditOrder(auditDTO.getAuditOrder() != null ? auditDTO.getAuditOrder() : 1);
                audit.setSubmitTime(dataset.getSubmitTime());
                audit.setSubmitterId(dataset.getSubmitBy());
                audit.setSubmitterName(dataset.getSubmitByName());
                audit.setStatus("1");
                audit.setDeleted("0");
                audit.setCreatedTime(LocalDateTime.now());
                // 设置创建人信息(使用提交人作为创建人)
                audit.setCreatedBy(dataset.getSubmitBy());
            }

            // 6. 更新审核信息
            audit.setAuditStatus(auditDTO.getAuditStatus());
            audit.setAuditOpinion(auditDTO.getAuditOpinion());
            audit.setAuditTime(LocalDateTime.now());

            // 设置审核人信息
            String auditorId = null;
            try {
                auditorId = SecurityUtils.getUsername();
                audit.setAuditorId(auditorId);
                audit.setAuditorName(auditorId); // 如果没有用户名,使用用户ID
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败", ex);
            }
            // TODO: 设置审核人机构信息
            // audit.setAuditorOrgCode(currentUserOrgCode);
            // audit.setAuditorOrgName(currentUserOrgName);

            audit.setUpdatedTime(LocalDateTime.now());
            audit.setUpdatedBy(auditorId != null ? auditorId : "system");

            // 7. 设置锁定标记（由审核人通过DTO传入，如果未传入则根据审核状态设置默认值）
            if (auditDTO.getLockedFlag() != null) {
                // 审核人明确指定了锁定状态
                audit.setLockedFlag(auditDTO.getLockedFlag());
            } else {
                // 未指定时，驳回和需要修订默认不锁定，审核通过默认不锁定（给审核人灵活性）
                if ("rejected".equals(auditDTO.getAuditStatus()) || "needs_revision".equals(auditDTO.getAuditStatus())) {
                    audit.setLockedFlag(0);
                } else if ("approved".equals(auditDTO.getAuditStatus())) {
                    // 审核通过时默认不锁定（0），审核人需要明确选择锁定
                    audit.setLockedFlag(0);
                }
            }

            this.saveOrUpdate(audit);

            // 8. 更新数据集状态
            if ("approved".equals(auditDTO.getAuditStatus())) {
                // 审核通过: 生成数据集编号, 状态变为已通过
                dataset.setDatasetStatus("approved");
                if (StrUtil.isBlank(dataset.getDatasetCode())) {
                    // 生成数据集编号: DS + 年月日 + 6位随机数
                    String datasetCode = "DS" + DateUtil.format(LocalDateTime.now(), "yyyyMMdd") + IdUtil.randomUUID().substring(0, 6).toUpperCase();
                    dataset.setDatasetCode(datasetCode);
                }
                dataset.setUpdatedTime(LocalDateTime.now());
                datasetMapper.updateById(dataset);

                String lockStatus = (audit.getLockedFlag() != null && audit.getLockedFlag() == 1) ? "locked" : "unlocked";
                log.info("Dataset approved and {}: {}", lockStatus, dataset.getDatasetCode());

                if (audit.getLockedFlag() != null && audit.getLockedFlag() == 1) {
                    return AjaxResult.success("Audit approved and locked, dataset code: " + dataset.getDatasetCode());
                } else {
                    return AjaxResult.success("Audit approved (unlocked), dataset code: " + dataset.getDatasetCode());
                }
            } else if ("rejected".equals(auditDTO.getAuditStatus())) {
                // 审核驳回: 状态变为已驳回
                dataset.setDatasetStatus("rejected");
                dataset.setUpdatedTime(LocalDateTime.now());
                datasetMapper.updateById(dataset);

                log.info("Dataset rejected: {}", dataset.getId());
                return AjaxResult.success("Audit rejected");
            } else if ("needs_revision".equals(auditDTO.getAuditStatus())) {
                // 需要修订: 状态变为需要修订
                dataset.setDatasetStatus("needs_revision");
                dataset.setUpdatedTime(LocalDateTime.now());
                datasetMapper.updateById(dataset);

                log.info("Dataset marked as needs revision: {}", dataset.getId());
                return AjaxResult.success("Marked as needs revision");
            } else {
                return AjaxResult.error("Unknown audit status");
            }
        } catch (Exception e) {
            log.error("Audit failed", e);
            return AjaxResult.error("Audit failed: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getAuditHistory(String datasetId) {
        try {
            QueryWrapper<BreedingDatasetAudit> wrapper = new QueryWrapper<>();
            wrapper.eq("dataset_id", datasetId);
            wrapper.eq("deleted", "0");
            wrapper.orderByDesc("audit_time");

            List<BreedingDatasetAudit> auditList = this.list(wrapper);

            List<BreedingDatasetAuditVO> voList = auditList.stream()
                    .map(audit -> BeanUtil.copyProperties(audit, BreedingDatasetAuditVO.class))
                    .collect(Collectors.toList());

            return AjaxResult.success("Query successful", voList);
        } catch (Exception e) {
            log.error("Failed to query audit history", e);
            return AjaxResult.error("Query failed: " + e.getMessage());
        }
    }
}
