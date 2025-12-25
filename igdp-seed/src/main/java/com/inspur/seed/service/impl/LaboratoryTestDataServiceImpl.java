package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.dto.LaboratoryTestDataDTO;
import com.inspur.seed.domain.entity.LaboratoryTestData;
import com.inspur.seed.domain.vo.LaboratoryTestDataVO;
import com.inspur.seed.mapper.LaboratoryTestDataMapper;
import com.inspur.seed.service.ILaboratoryTestDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实验室测试数据采集Service业务层处理
 *
 * @author igdp
 * @date 2025-11-26
 */
@Slf4j
@Service
public class LaboratoryTestDataServiceImpl extends ServiceImpl<LaboratoryTestDataMapper, LaboratoryTestData> implements ILaboratoryTestDataService {

    @Override
    public List<LaboratoryTestDataVO> selectLaboratoryTestDataList(LaboratoryTestDataDTO dto) {
        QueryWrapper<LaboratoryTestData> wrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(dto.getSampleId())) {
            wrapper.like("sample_id", dto.getSampleId());
        }

        if (StrUtil.isNotBlank(dto.getBatchId())) {
            wrapper.eq("batch_id", dto.getBatchId());
        }

        if (StrUtil.isNotBlank(dto.getTrialId())) {
            wrapper.eq("trial_id", dto.getTrialId());
        }

        if (StrUtil.isNotBlank(dto.getWorkflowStatus())) {
            wrapper.eq("workflow_status", dto.getWorkflowStatus());
        }

        if (StrUtil.isNotBlank(dto.getSampleType())) {
            wrapper.like("sample_type", dto.getSampleType());
        }

        if (StrUtil.isNotBlank(dto.getPassFailFlag())) {
            wrapper.eq("pass_fail_flag", dto.getPassFailFlag());
        }

        // 审核列表查询时，需要过滤 audit_canceled
        if (dto.getAuditCanceled() != null) {
            wrapper.eq("audit_canceled", dto.getAuditCanceled());
        }

        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("create_time");

        List<LaboratoryTestData> list = this.list(wrapper);
        return list.stream()
                .map(entity -> {
                    LaboratoryTestDataVO vo = BeanUtil.copyProperties(entity, LaboratoryTestDataVO.class);
                    // 映射创建人和创建时间字段
                    vo.setCreatedByName(entity.getCreateBy());
                    if (entity.getCreateTime() != null) {
                        vo.setCreatedTime(entity.getCreateTime().toString());
                    }
                    // 映射更新时间字段
                    if (entity.getUpdateTime() != null) {
                        vo.setUpdatedTime(entity.getUpdateTime().toString());
                    }
                    // 映射审批人字段
                    vo.setApproveByName(entity.getApproveBy());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public LaboratoryTestDataVO selectLaboratoryTestDataById(String dataId) {
        LaboratoryTestData entity = this.getById(dataId);
        if (entity == null) {
            return null;
        }
        LaboratoryTestDataVO vo = BeanUtil.copyProperties(entity, LaboratoryTestDataVO.class);
        // 映射创建人和创建时间字段
        vo.setCreatedByName(entity.getCreateBy());
        if (entity.getCreateTime() != null) {
            vo.setCreatedTime(entity.getCreateTime().toString());
        }
        // 映射更新时间字段
        if (entity.getUpdateTime() != null) {
            vo.setUpdatedTime(entity.getUpdateTime().toString());
        }
        // 映射审批人字段
        vo.setApproveByName(entity.getApproveBy());
        return vo;
    }

    @Override
    public int insertLaboratoryTestData(LaboratoryTestDataDTO dto) {
        log.info("开始插入实验室测试数据, sampleId: {}", dto.getSampleId());

        // 验证必填字段
//        if (StrUtil.isBlank(dto.getSampleId())) {
//            log.error("样本ID不能为空");
//            throw new IllegalArgumentException("样本ID不能为空");
//        }
//        if (StrUtil.isBlank(dto.getSampleCondition())) {
//            log.error("样本状态不能为空");
//            throw new IllegalArgumentException("样本状态不能为空");
//        }
//        if (dto.getGerminationRate() == null) {
//            log.error("发芽率不能为空");
//            throw new IllegalArgumentException("发芽率不能为空");
//        }
//        if (dto.getPurityPercent() == null) {
//            log.error("纯度不能为空");
//            throw new IllegalArgumentException("纯度不能为空");
//        }
//        if (dto.getMoistureContentPercent() == null) {
//            log.error("含水量不能为空");
//            throw new IllegalArgumentException("含水量不能为空");
//        }
//        if (dto.getProteinPercent() == null) {
//            log.error("蛋白质不能为空");
//            throw new IllegalArgumentException("蛋白质不能为空");
//        }
//        if (StrUtil.isBlank(dto.getSeedHealthFindings())) {
//            log.error("种子健康发现不能为空");
//            throw new IllegalArgumentException("种子健康发现不能为空");
//        }
//        if (StrUtil.isBlank(dto.getTraceabilityLink())) {
//            log.error("链路责任不能为空");
//            throw new IllegalArgumentException("链路责任不能为空");
//        }

        LaboratoryTestData entity = BeanUtil.copyProperties(dto, LaboratoryTestData.class);
        entity.setDelFlag("0");
        // 新增数据默认为草稿状态
        entity.setWorkflowStatus("S0");
        entity.setAuditCanceled(0);

        // 设置创建信息
        String username = SecurityUtils.getUsername();
        entity.setCreateBy(username);
        entity.setCreateTime(LocalDateTime.now());

        log.info("准备保存实验室测试数据, dataId: {}, createBy: {}", entity.getDataId(), username);

        boolean success = this.save(entity);
        if (!success) {
            log.error("保存实验室测试数据失败, sampleId: {}", dto.getSampleId());
            throw new RuntimeException("保存实验室测试数据失败");
        }

        log.info("成功插入实验室测试数据, dataId: {}, sampleId: {}", entity.getDataId(), dto.getSampleId());
        return 1;
    }

    @Override
    public int updateLaboratoryTestData(LaboratoryTestDataDTO dto) {
        LaboratoryTestData entity = BeanUtil.copyProperties(dto, LaboratoryTestData.class);

        // 设置更新信息
        entity.setUpdateBy(SecurityUtils.getUsername());
        entity.setUpdateTime(LocalDateTime.now());

        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int deleteLaboratoryTestDataByIds(String[] dataIds) {
        List<LaboratoryTestData> list = Arrays.stream(dataIds)
                .map(id -> {
                    LaboratoryTestData entity = new LaboratoryTestData();
                    entity.setDataId(id);
                    entity.setDelFlag("2");
                    return entity;
                })
                .collect(Collectors.toList());
        return this.updateBatchById(list) ? list.size() : 0;
    }

    @Override
    public int submitForApproval(String dataId) {
        LaboratoryTestData entity = this.getById(dataId);
        if (entity == null) {
            throw new RuntimeException("Data not found");
        }

        // 只有草稿(S0)和已退回(S3)状态可以提交审核
        if (!"S0".equals(entity.getWorkflowStatus()) && !"S3".equals(entity.getWorkflowStatus())) {
            throw new RuntimeException("Only draft or rejected data can be submitted for approval");
        }

        entity.setWorkflowStatus("S1");
        entity.setUpdateBy(SecurityUtils.getUsername());
        entity.setUpdateTime(LocalDateTime.now());

        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int approveLaboratoryTestData(String dataId, String auditOpinion) {
        LaboratoryTestData entity = this.getById(dataId);
        if (entity == null) {
            throw new RuntimeException("Data not found");
        }

        // 只有待审批(S1)状态可以审核通过
        if (!"S1".equals(entity.getWorkflowStatus())) {
            throw new RuntimeException("Only pending approval data can be approved");
        }

        String username = SecurityUtils.getUsername();
        entity.setWorkflowStatus("S2");
        entity.setApproveBy(username);
        entity.setApproveTime(LocalDateTime.now().toString());
        entity.setAuditOpinion(auditOpinion);
        entity.setUpdateBy(username);
        entity.setUpdateTime(LocalDateTime.now());

        boolean updateSuccess = this.updateById(entity);
        
        // 审核通过后，检查该试验是否所有实验室测试数据都已审核通过
        // 如果是，则将试验状态更新为已完成(02)
        if (updateSuccess && cn.hutool.core.util.StrUtil.isNotBlank(entity.getTrialId())) {
            try {
                com.inspur.seed.service.ITrialBasicService trialBasicService = 
                    com.inspur.common.utils.spring.SpringUtils.getBean(com.inspur.seed.service.ITrialBasicService.class);
                trialBasicService.checkAndUpdateTrialCompletionStatus(entity.getTrialId());
                log.info("实验室测试数据审核通过后，已检查试验完成状态, trialId: {}", entity.getTrialId());
            } catch (Exception e) {
                log.error("检查试验完成状态失败, trialId: {}, error: {}", entity.getTrialId(), e.getMessage());
                // 不影响主流程，继续执行
            }
        }

        return updateSuccess ? 1 : 0;
    }

    @Override
    public int rejectLaboratoryTestData(String dataId, String auditOpinion) {
        LaboratoryTestData entity = this.getById(dataId);
        if (entity == null) {
            throw new RuntimeException("Data not found");
        }

        // 只有待审批(S1)状态可以退回
        if (!"S1".equals(entity.getWorkflowStatus())) {
            throw new RuntimeException("Only pending approval data can be rejected");
        }

        String username = SecurityUtils.getUsername();
        entity.setWorkflowStatus("S3");
        entity.setAuditOpinion(auditOpinion);
        entity.setUpdateBy(username);
        entity.setUpdateTime(LocalDateTime.now());

        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int archiveLaboratoryTestData(String dataId) {
        LaboratoryTestData entity = this.getById(dataId);
        if (entity == null) {
            throw new RuntimeException("Data not found");
        }

        // 只有已审批(S2)状态可以归档
        if (!"S2".equals(entity.getWorkflowStatus())) {
            throw new RuntimeException("Only approved data can be archived");
        }

        entity.setWorkflowStatus("S9");
        entity.setUpdateBy(SecurityUtils.getUsername());
        entity.setUpdateTime(LocalDateTime.now());

        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int cancelLaboratoryTestData(String dataId) {
        LaboratoryTestData entity = this.getById(dataId);
        if (entity == null) {
            throw new RuntimeException("Data not found");
        }

        entity.setWorkflowStatus("S10");
        entity.setUpdateBy(SecurityUtils.getUsername());
        entity.setUpdateTime(LocalDateTime.now());

        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int cancelAuditRecord(String dataId) {
        LaboratoryTestData entity = this.getById(dataId);
        if (entity == null) {
            throw new RuntimeException("Data not found");
        }

        // 只有已审批(S2)状态可以作废审核记录
        if (!"S2".equals(entity.getWorkflowStatus())) {
            throw new RuntimeException("Only approved data audit record can be canceled");
        }

        // 保持S2状态，但标记审核记录已作废
        entity.setAuditCanceled(1);
        entity.setUpdateBy(SecurityUtils.getUsername());
        entity.setUpdateTime(LocalDateTime.now());

        return this.updateById(entity) ? 1 : 0;
    }
}
