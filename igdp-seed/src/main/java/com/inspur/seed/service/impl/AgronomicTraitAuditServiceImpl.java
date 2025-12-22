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
import com.inspur.seed.domain.dto.AgronomicTraitAuditDTO;
import com.inspur.seed.domain.dto.AgronomicTraitAuditQueryDTO;
import com.inspur.seed.domain.entity.AgronomicTraitAudit;
import com.inspur.seed.domain.entity.AgronomicTraitRecord;
import com.inspur.seed.domain.vo.AgronomicTraitAuditVO;
import com.inspur.seed.mapper.AgronomicTraitAuditMapper;
import com.inspur.seed.mapper.AgronomicTraitRecordMapper;
import com.inspur.seed.service.IBreedingBatchService;
import com.inspur.seed.service.IAgronomicTraitAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

            // 筛选条件对接查询DTO
            if (StrUtil.isNotBlank(queryDTO.getAuditStatus())) {
                wrapper.eq(AgronomicTraitAudit::getAuditStatus, queryDTO.getAuditStatus());
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
                        // 审核表的trait_id 对应 主表的record_id
                        if (StrUtil.isNotBlank(audit.getTraitId())) {
                            AgronomicTraitRecord traitRecord = traitRecordMapper.selectById(audit.getTraitId());
                            if (traitRecord != null) {
                                BeanUtil.copyProperties(traitRecord, vo);
                                // 处理日期字段重名：主表auditTime -> VO的auditTimeMain
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
            // 1. 查询主表（AgronomicTraitRecord）并校验
            AgronomicTraitRecord traitRecord = traitRecordMapper.selectById(auditDTO.getTraitId());
            if (traitRecord == null || 1 == traitRecord.getIsDeleted()) {
                return AjaxResult.error("农艺性状主记录不存在");
            }

            // 2. 校验主表流程状态
            if (!"submitted".equals(traitRecord.getWorkflowStatus()) && !"reviewing".equals(traitRecord.getWorkflowStatus())) {
                return AjaxResult.error("仅已提交或审核中的性状可进行审核操作");
            }

            // 3. 校验审核状态合法性
            if (!"approved".equals(auditDTO.getAuditStatus())
                    && !"rejected".equals(auditDTO.getAuditStatus())
                    && !"needs_revision".equals(auditDTO.getAuditStatus())) {
                return AjaxResult.error("审核状态仅支持approved、rejected、needs_revision");
            }

            // 4. 驳回/需修订时必填审核意见
            if (("rejected".equals(auditDTO.getAuditStatus()) || "needs_revision".equals(auditDTO.getAuditStatus()))
                    && StrUtil.isBlank(auditDTO.getAuditOpinion())) {
                return AjaxResult.error("驳回或需要修订时必须填写审核意见");
            }

            // 5. 查询或创建审核记录
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

            // 6. 更新审核记录核心信息
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
                log.warn("获取当前用户信息失败", ex);
            }

            audit.setUpdatedTime(LocalDateTime.now());
            audit.setUpdatedBy(auditorId != null ? auditorId : "system");

            // 设置锁定标记（默认0未锁定）
            if (auditDTO.getLockedFlag() != null) {
                audit.setLockedFlag(auditDTO.getLockedFlag());
            } else {
                audit.setLockedFlag(("rejected".equals(auditDTO.getAuditStatus()) || "needs_revision".equals(auditDTO.getAuditStatus())) ? 0 : 0);
            }

            this.saveOrUpdate(audit);

            // 7. 更新主表（AgronomicTraitRecord）状态及审核信息
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

            traitRecordMapper.updateById(traitRecord);

            // 8. 返回对应提示信息
            if ("approved".equals(auditDTO.getAuditStatus())) {
                String lockStatus = audit.getLockedFlag() == 1 ? "锁定" : "未锁定";
                return AjaxResult.success("审核通过并" + lockStatus);
            } else if ("rejected".equals(auditDTO.getAuditStatus())) {
                return AjaxResult.success("审核驳回");
            } else if ("needs_revision".equals(auditDTO.getAuditStatus())) {
                return AjaxResult.success("标记为需要修订");
            } else {
                return AjaxResult.error("未知的审核状态");
            }
        } catch (Exception e) {
            log.error("农艺性状审核操作失败", e);
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