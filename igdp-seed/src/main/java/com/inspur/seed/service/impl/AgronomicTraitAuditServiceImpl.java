package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.dto.AgronomicTraitAuditDTO;
import com.inspur.seed.domain.dto.AgronomicTraitAuditQueryDTO;
import com.inspur.seed.breeding.agronomicTrait.domain.entity.AgronomicTraitAudit;
import com.inspur.seed.breeding.agronomicTrait.domain.entity.AgronomicTraitRecord;

import com.inspur.seed.domain.vo.AgronomicTraitAuditVO;
import com.inspur.seed.breeding.agronomicTrait.mapper.AgronomicTraitAuditMapper;
import com.inspur.seed.breeding.agronomicTrait.mapper.AgronomicTraitRecordMapper;
import com.inspur.seed.breeding.breedingBatch.service.IBreedingBatchService;
import com.inspur.seed.service.IAgronomicTraitAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 农艺性状审核Service实现（完全适配你的实体类）
 */
@Slf4j
@Service
public class AgronomicTraitAuditServiceImpl extends ServiceImpl<AgronomicTraitAuditMapper, AgronomicTraitAudit>
        implements IAgronomicTraitAuditService {

    // 替换为你的主表Mapper：AgronomicTraitRecordMapper
    @Autowired
    private AgronomicTraitRecordMapper traitRecordMapper;

    @Autowired
    private IBreedingBatchService breedingBatchService;

    @Override
    public AjaxResult getAuditList(AgronomicTraitAuditQueryDTO queryDTO) {
        try {
            LambdaQueryWrapper<AgronomicTraitAudit> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AgronomicTraitAudit::getDeleted, "0");

            LambdaQueryWrapper<AgronomicTraitRecord> recordWrapper = new LambdaQueryWrapper<>();
            recordWrapper.eq(AgronomicTraitRecord::getIsDeleted, 0);
            boolean needRecordFilter = false;

            if (StrUtil.isNotBlank(queryDTO.getTrialId())) {
                recordWrapper.eq(AgronomicTraitRecord::getTrialId, queryDTO.getTrialId());
                needRecordFilter = true;
            }

            if (StrUtil.isNotBlank(queryDTO.getPlotId())) {
                recordWrapper.eq(AgronomicTraitRecord::getPlotId, queryDTO.getPlotId());
                needRecordFilter = true;
            }

            if (StrUtil.isNotBlank(queryDTO.getBatchId())) {
                recordWrapper.eq(AgronomicTraitRecord::getBatchId, queryDTO.getBatchId());
                needRecordFilter = true;
            }

            // ========== 先处理主表字段筛选 ==========
            if (StrUtil.isNotBlank(queryDTO.getGrowthStage())) {
                recordWrapper.eq(AgronomicTraitRecord::getGrowthStage, queryDTO.getGrowthStage());
                needRecordFilter = true;
            }

            if (needRecordFilter) {
                List<String> recordIdList = traitRecordMapper.selectList(recordWrapper).stream()
                        .map(AgronomicTraitRecord::getRecordId)
                        .collect(Collectors.toList());

                if (!recordIdList.isEmpty()) {
                    wrapper.in(AgronomicTraitAudit::getTraitId, recordIdList);
                } else {
                    Map<String, Object> emptyData = new HashMap<>();
                    emptyData.put("list", new ArrayList<>());
                    emptyData.put("total", 0);
                    return AjaxResult.success("查询成功", emptyData);
                }
            }
            // ========== 主表筛选处理结束 ==========

            // 筛选条件对接查询DTO
            Set<String> allowedAuditStatus = new HashSet<>(Arrays.asList("pending", "approved"));
            if (StrUtil.isNotBlank(queryDTO.getAuditStatus())) {
                String inputStatus = queryDTO.getAuditStatus();
                if (allowedAuditStatus.contains(inputStatus)) {
                    wrapper.eq(AgronomicTraitAudit::getAuditStatus, inputStatus);
                }
            } else {
                wrapper.in(AgronomicTraitAudit::getAuditStatus, allowedAuditStatus);
            }
            if (StrUtil.isNotBlank(queryDTO.getBatchId())) {
                wrapper.eq(AgronomicTraitAudit::getBatchId, queryDTO.getBatchId());
            }
            if (StrUtil.isNotBlank(queryDTO.getObservationDateStart())) {
                wrapper.ge(AgronomicTraitAudit::getSubmitTime, queryDTO.getObservationDateStart());
            }
            if (StrUtil.isNotBlank(queryDTO.getObservationDateEnd())) {
                wrapper.le(AgronomicTraitAudit::getSubmitTime, queryDTO.getObservationDateEnd());
            }

            wrapper.orderByDesc(AgronomicTraitAudit::getSubmitTime);

            // 分页参数
            Integer pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
            Integer pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
            Page<AgronomicTraitAudit> page = new Page<>(pageNum, pageSize);
            Page<AgronomicTraitAudit> result = this.page(page, wrapper);

            // 转换VO并关联主表（AgronomicTraitRecord）
            List<AgronomicTraitAuditVO> voList = result.getRecords().stream()
                    .map(audit -> {
                        AgronomicTraitAuditVO vo = BeanUtil.copyProperties(audit, AgronomicTraitAuditVO.class);
                        if (StrUtil.isNotBlank(audit.getTraitId())) {
                            AgronomicTraitRecord traitRecord = traitRecordMapper.selectById(audit.getTraitId());
                            if (traitRecord != null) {
                                BeanUtil.copyProperties(traitRecord, vo);
                                vo.setAuditTimeMain(traitRecord.getAuditTime());
                            }
                        }
                        return vo;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> data = new HashMap<>();
            data.put("list", voList);
            data.put("total", result.getTotal());

            return AjaxResult.success("查询成功", data);
        } catch (Exception e) {
            log.error("查询农艺性状审核列表失败", e);
            return AjaxResult.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    public AjaxResult getAuditById(String id) {
        try {
            AgronomicTraitAudit audit = this.getById(id);
            if (audit == null || "1".equals(audit.getDeleted())) {
                return AjaxResult.error("审核记录不存在");
            }

            // 关联主表查询
            AgronomicTraitRecord traitRecord = traitRecordMapper.selectById(audit.getTraitId());
            if (traitRecord == null) {
                return AjaxResult.error("农艺性状主记录不存在");
            }

            // 转换VO并赋值主表字段
            AgronomicTraitAuditVO vo = BeanUtil.copyProperties(audit, AgronomicTraitAuditVO.class);
            BeanUtil.copyProperties(traitRecord, vo);
            vo.setAuditTimeMain(traitRecord.getAuditTime()); // 区分审核表和主表的审核时间

            return AjaxResult.success("查询成功", vo);
        } catch (Exception e) {
            log.error("查询农艺性状审核详情失败", e);
            return AjaxResult.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    public AjaxResult getAuditByTraitId(String traitId) {
        try {
            QueryWrapper<AgronomicTraitAudit> wrapper = new QueryWrapper<>();
            wrapper.eq("trait_id", traitId);
            wrapper.eq("deleted", "0");
            wrapper.orderByDesc("created_time");
            wrapper.last("LIMIT 1");

            AgronomicTraitAudit audit = this.getOne(wrapper);
            if (audit == null) {
                return AjaxResult.error("审核记录不存在");
            }

            return getAuditById(audit.getId());
        } catch (Exception e) {
            log.error("按性状ID查询审核详情失败", e);
            return AjaxResult.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult performAudit(AgronomicTraitAuditDTO auditDTO) {


        try {
            // 2. 查询主表（AgronomicTraitRecord）并校验
            AgronomicTraitRecord traitRecord = traitRecordMapper.selectById(auditDTO.getTraitId());
            if (traitRecord == null || 1 == traitRecord.getIsDeleted()) {
                String errorMsg = "农艺性状主记录不存在";
                log.error("审核失败：{}，传入的traitId：{}", errorMsg, auditDTO.getTraitId()); // 错误日志，标注失败原因
                return AjaxResult.error(errorMsg);
            }

            // 3. 校验主表流程状态
            String Status = traitRecord.getStatus();
            if (!"submitted".equals(Status) && !"reviewing".equals(Status)) {
                String errorMsg = "仅已提交（submitted）或审核中（reviewing）的性状可进行审核操作，当前流程状态：" + Status;
                log.error("审核失败：{}", errorMsg); // 错误日志，明确流程状态不合法
                return AjaxResult.error(errorMsg);
            }

            // 4. 校验审核状态合法性
            String auditStatus = auditDTO.getAuditStatus();
            if (!"approved".equals(auditStatus)
                    && !"rejected".equals(auditStatus)
                    && !"needs_revision".equals(auditStatus)) {
                String errorMsg = "审核状态仅支持approved、rejected、needs_revision，当前传入：" + auditStatus;
                log.error("审核失败：{}", errorMsg); // 错误日志，明确审核状态不合法
                return AjaxResult.error(errorMsg);
            }

            // 5. 驳回/需修订时必填审核意见
            if (("rejected".equals(auditStatus) || "needs_revision".equals(auditStatus))
                    && StrUtil.isBlank(auditDTO.getAuditOpinion())) {
                String errorMsg = "驳回或需要修订时必须填写审核意见";
                log.error("审核失败：{}，当前审核状态：{}，审核意见为空", errorMsg, auditStatus); // 错误日志，明确意见为空
                return AjaxResult.error(errorMsg);
            }

            // 6. 查询或创建审核记录
            QueryWrapper<AgronomicTraitAudit> wrapper = new QueryWrapper<>();
            wrapper.eq("trait_id", auditDTO.getTraitId());
            wrapper.eq("deleted", "0");
            wrapper.orderByDesc("created_time");
            wrapper.last("LIMIT 1");

            AgronomicTraitAudit audit = this.getOne(wrapper);

            if (audit == null) {
                audit = new AgronomicTraitAudit();
                audit.setId(IdUtil.simpleUUID());
                audit.setTraitId(auditDTO.getTraitId());
                audit.setBatchId(traitRecord.getBatchId()); // 从主表获取批次ID
                audit.setAuditNode(auditDTO.getAuditNode());
                audit.setAuditOrder(auditDTO.getAuditOrder() != null ? auditDTO.getAuditOrder() : 1);

                // 主表observationDate是Date类型，转换为LocalDateTime赋值给审核表submitTime
                if (traitRecord.getObservationDate() != null) {
                    audit.setSubmitTime(LocalDateTime.ofInstant(traitRecord.getObservationDate().toInstant(), java.time.ZoneId.systemDefault()));
                } else {
                    audit.setSubmitTime(LocalDateTime.now());
                }

                audit.setSubmitterId(traitRecord.getCreateBy()); // 主表创建人作为提交人
                audit.setSubmitterName(traitRecord.getCreateBy());
                audit.setStatus("1");
                audit.setDeleted("0");
                audit.setCreatedTime(LocalDateTime.now());
                audit.setCreatedBy(traitRecord.getCreateBy());
            }

            // 7. 更新审核记录核心信息
            audit.setAuditStatus(auditDTO.getAuditStatus());
            audit.setAuditOpinion(auditDTO.getAuditOpinion());
            audit.setAuditTime(LocalDateTime.now());

            // 获取当前审核人信息
            String auditorId = null;
            try {
                auditorId = SecurityUtils.getUsername();
                audit.setAuditorId(auditorId);
                audit.setAuditorName(auditorId);
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败", ex); // 打印异常堆栈，方便排查用户信息获取失败原因
                auditorId = "system"; // 兜底设置审核人
            }

            audit.setUpdatedTime(LocalDateTime.now());
            audit.setUpdatedBy(auditorId != null ? auditorId : "system");

            // 设置锁定标记（默认0未锁定）
            if (auditDTO.getLockedFlag() != null) {
                audit.setLockedFlag(auditDTO.getLockedFlag());
            } else {
                audit.setLockedFlag(("rejected".equals(auditDTO.getAuditStatus()) || "needs_revision".equals(auditDTO.getAuditStatus())) ? 0 : 0);
            }

            // 保存/更新审核记录
            boolean saveOrUpdateResult = this.saveOrUpdate(audit);

            // 8. 更新主表（AgronomicTraitRecord）状态及审核信息
            traitRecord.setStatus(auditDTO.getAuditStatus());
            // 同步设置 workflowStatus
            traitRecord.setWorkflowStatus(auditDTO.getAuditStatus());
            traitRecord.setAuditBy(auditorId);
            // 审核表auditTime是LocalDateTime，主表是Date，转换赋值
            traitRecord.setAuditTime(new Date());
            traitRecord.setUpdateBy(auditorId != null ? auditorId : "system");
            traitRecord.setUpdateTime(LocalDateTime.now());
            // 审核通过时自动生成性状编码（如果未生成）
            if ("approved".equals(auditDTO.getAuditStatus())) {
                // 此处可根据你的业务规则生成编码，示例沿用之前的规则
                // 注意：主表若没有traitCode字段，可删除该段逻辑
            /*
            if (StrUtil.isBlank(traitRecord.getTraitCode())) {
                String traitCode = "AT" + DateUtil.format(LocalDateTime.now(), "yyyyMMdd") + IdUtil.randomUUID().substring(0, 6).toUpperCase();
                traitRecord.setTraitCode(traitCode);
            }
            */
                breedingBatchService.finished(traitRecord.getBatchId());
            }

            // 更新主表
            int updateMainTableResult = traitRecordMapper.updateById(traitRecord);

            // 9. 返回对应提示信息
            String successMsg = "";
            if ("approved".equals(auditDTO.getAuditStatus())) {
                String lockStatus = audit.getLockedFlag() == 1 ? "锁定" : "未锁定";
                successMsg = "审核通过并" + lockStatus;
            } else if ("rejected".equals(auditDTO.getAuditStatus())) {
                successMsg = "审核驳回";
            } else if ("needs_revision".equals(auditDTO.getAuditStatus())) {
                successMsg = "标记为需要修订";
            }
            log.info("农艺性状审核操作成功：{}，性状ID={}，审核状态={}", successMsg, auditDTO.getTraitId(), auditStatus);
            return AjaxResult.success(successMsg);

        } catch (Exception e) {
            // 打印异常完整堆栈（核心：不仅打印消息，还要打印堆栈，定位具体报错行）
            log.error("农艺性状审核操作失败！传入参数auditDTO={}，异常详情：", auditDTO, e);
            return AjaxResult.error("审核失败：" + e.getMessage());
        }
    }

    @Override
    public AjaxResult getAuditHistory(String traitId) {
        try {
            QueryWrapper<AgronomicTraitAudit> wrapper = new QueryWrapper<>();
            wrapper.eq("trait_id", traitId);
            wrapper.eq("deleted", "0");
            wrapper.orderByDesc("audit_time");

            List<AgronomicTraitAudit> auditList = this.list(wrapper);
            List<AgronomicTraitAuditVO> voList = auditList.stream()
                    .map(audit -> {
                        AgronomicTraitAuditVO vo = BeanUtil.copyProperties(audit, AgronomicTraitAuditVO.class);
                        // 关联主表补充信息
                        AgronomicTraitRecord traitRecord = traitRecordMapper.selectById(audit.getTraitId());
                        if (traitRecord != null) {
                            BeanUtil.copyProperties(traitRecord, vo);
                            vo.setAuditTimeMain(traitRecord.getAuditTime());
                        }
                        return vo;
                    })
                    .collect(Collectors.toList());

            return AjaxResult.success("查询成功", voList);
        } catch (Exception e) {
            log.error("查询农艺性状审核历史失败", e);
            return AjaxResult.error("查询失败：" + e.getMessage());
        }
    }
}
