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
                                vo.setDatasetCode(dataset.getDatasetCode());
                                vo.setBatchName(dataset.getBatchName());
                                vo.setCropType(dataset.getCropType());
                                vo.setVarietyName(dataset.getVarietyName());
                                vo.setDatasetStatus(dataset.getDatasetStatus());
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

            return AjaxResult.success("查询成功", data);
        } catch (Exception e) {
            log.error("查询审核列表失败", e);
            return AjaxResult.error("查询失败:" + e.getMessage());
        }
    }

    @Override
    public AjaxResult getAuditById(String id) {
        try {
            BreedingDatasetAudit audit = this.getById(id);
            if (audit == null || "1".equals(audit.getDeleted())) {
                return AjaxResult.error("审核记录不存在");
            }

            // 查询数据集信息
            BreedingDataset dataset = datasetMapper.selectById(audit.getDatasetId());
            if (dataset == null) {
                return AjaxResult.error("数据集不存在");
            }

            // 组装VO
            BreedingDatasetAuditVO vo = BeanUtil.copyProperties(audit, BreedingDatasetAuditVO.class);
            vo.setDatasetCode(dataset.getDatasetCode());
            vo.setBatchName(dataset.getBatchName());
            vo.setCropType(dataset.getCropType());
            vo.setVarietyName(dataset.getVarietyName());
            vo.setDatasetStatus(dataset.getDatasetStatus());
            vo.setTrialCount(dataset.getTrialCount());
            vo.setFieldDataCount(dataset.getFieldDataCount());
            vo.setEnvDataCount(dataset.getEnvDataCount());
            vo.setLabTestCount(dataset.getLabTestCount());
            vo.setYieldDataCount(dataset.getYieldDataCount());

            return AjaxResult.success("查询成功", vo);
        } catch (Exception e) {
            log.error("查询审核详情失败", e);
            return AjaxResult.error("查询失败:" + e.getMessage());
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
                return AjaxResult.error("审核记录不存在");
            }

            return getAuditById(audit.getId());
        } catch (Exception e) {
            log.error("根据数据集ID查询审核详情失败", e);
            return AjaxResult.error("查询失败:" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult performAudit(BreedingDatasetAuditDTO auditDTO) {
        try {
            // 1. 校验数据集
            BreedingDataset dataset = datasetMapper.selectById(auditDTO.getDatasetId());
            if (dataset == null || "1".equals(dataset.getDeleted())) {
                return AjaxResult.error("数据集不存在");
            }

            // 2. 校验数据集状态(必须是已提交或审核中)
            if (!"submitted".equals(dataset.getDatasetStatus()) && !"reviewing".equals(dataset.getDatasetStatus())) {
                return AjaxResult.error("只能审核已提交或审核中的数据集");
            }

            // 3. 校验审核状态
            if (!"approved".equals(auditDTO.getAuditStatus()) && !"rejected".equals(auditDTO.getAuditStatus())) {
                return AjaxResult.error("审核状态只能是approved或rejected");
            }

            // 4. 驳回时必须填写审核意见
            if ("rejected".equals(auditDTO.getAuditStatus()) && StrUtil.isBlank(auditDTO.getAuditOpinion())) {
                return AjaxResult.error("驳回时必须填写审核意见");
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

            this.saveOrUpdate(audit);

            // 7. 更新数据集状态
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

                return AjaxResult.success("审核通过,数据集编号:" + dataset.getDatasetCode());
            } else {
                // 审核驳回: 状态变为已驳回
                dataset.setDatasetStatus("rejected");
                dataset.setUpdatedTime(LocalDateTime.now());
                datasetMapper.updateById(dataset);

                return AjaxResult.success("审核已驳回");
            }
        } catch (Exception e) {
            log.error("审核失败", e);
            return AjaxResult.error("审核失败:" + e.getMessage());
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

            return AjaxResult.success("查询成功", voList);
        } catch (Exception e) {
            log.error("查询审核历史失败", e);
            return AjaxResult.error("查询失败:" + e.getMessage());
        }
    }
}
