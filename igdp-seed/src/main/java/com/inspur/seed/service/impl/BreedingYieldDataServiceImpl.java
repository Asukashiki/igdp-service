package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.dto.BreedingYieldDataDTO;
import com.inspur.seed.domain.entity.BreedingYieldData;
import com.inspur.seed.domain.vo.BreedingYieldDataVO;
import com.inspur.seed.mapper.BreedingYieldDataMapper;
import com.inspur.seed.service.IBreedingYieldDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产量数据Service实现类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Service
public class BreedingYieldDataServiceImpl implements IBreedingYieldDataService {

    @Autowired
    private BreedingYieldDataMapper breedingYieldDataMapper;

    @Override
    public List<BreedingYieldDataVO> selectBreedingYieldDataList(BreedingYieldDataDTO dto) {
        QueryWrapper<BreedingYieldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", "0");

        // 育种批次ID
        if (StrUtil.isNotBlank(dto.getBatchId())) {
            queryWrapper.eq("batch_id", dto.getBatchId());
        }

        // 试验ID
        if (StrUtil.isNotBlank(dto.getTrialId())) {
            queryWrapper.eq("trial_id", dto.getTrialId());
        }

        // 地块编号模糊查询
        if (StrUtil.isNotBlank(dto.getPlotId())) {
            queryWrapper.like("plot_id", dto.getPlotId());
        }

        // 收获日期范围
        if (dto.getHarvestDateStart() != null) {
            queryWrapper.ge("harvest_date", dto.getHarvestDateStart());
        }
        if (dto.getHarvestDateEnd() != null) {
            queryWrapper.le("harvest_date", dto.getHarvestDateEnd());
        }

        // 业务状态筛选（submit/approve 等）
        if (StrUtil.isNotBlank(dto.getStatus())) {
            queryWrapper.eq("status", dto.getStatus());
        }

        // 流程审核状态筛选（字典 flow_status）
        if (StrUtil.isNotBlank(dto.getWorkflowStatus())) {
            queryWrapper.eq("workflow_status", dto.getWorkflowStatus());
        }

        queryWrapper.orderByDesc("created_time");

        List<BreedingYieldData> list = breedingYieldDataMapper.selectList(queryWrapper);
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public BreedingYieldDataVO selectBreedingYieldDataById(String id) {
        BreedingYieldData entity = breedingYieldDataMapper.selectById(id);
        return entity != null ? convertToVO(entity) : null;
    }

    @Override
    public int insertBreedingYieldData(BreedingYieldDataDTO dto) {
        BreedingYieldData entity = new BreedingYieldData();
        BeanUtil.copyProperties(dto, entity);

        // 默认业务状态
        if (StrUtil.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
        // 默认流程审核状态为待审批
        if (StrUtil.isBlank(entity.getWorkflowStatus())) {
            entity.setWorkflowStatus("S0");
        }
        entity.setDeleted("0");
        entity.setCreatedTime(LocalDateTime.now());
        entity.setCreatedBy(SecurityUtils.getUsername());

        return breedingYieldDataMapper.insert(entity);
    }

    @Override
    public int updateBreedingYieldData(BreedingYieldDataDTO dto) {
        BreedingYieldData entity = new BreedingYieldData();
        BeanUtil.copyProperties(dto, entity);

        entity.setUpdatedTime(LocalDateTime.now());
        entity.setUpdatedBy(SecurityUtils.getUsername());
        
        // 如果DTO中有审核相关信息，则更新审核字段
        if (dto.getAuditBy() != null) {
            entity.setAuditBy(dto.getAuditBy());
        }
        if (dto.getAuditTime() != null) {
            entity.setAuditTime(dto.getAuditTime());
        }
        if (dto.getWorkflowStatus() != null) {
            entity.setWorkflowStatus(dto.getWorkflowStatus());
        }
        if (dto.getRemark() != null) {
            entity.setAuditRemark(dto.getRemark());
        }

        return breedingYieldDataMapper.updateById(entity);
    }

    @Override
    public int deleteBreedingYieldDataByIds(String[] ids) {
        // 逻辑删除
        return Arrays.stream(ids).mapToInt(id -> {
            BreedingYieldData entity = new BreedingYieldData();
            entity.setId(id);
            entity.setDeleted("1");
            entity.setUpdatedTime(LocalDateTime.now());
            return breedingYieldDataMapper.updateById(entity);
        }).sum();
    }

    @Override
    public int submitForReview(String id, String workflowStatus) {
        BreedingYieldData entity = new BreedingYieldData();
        entity.setId(id);
        entity.setStatus("1"); // 将status字段更新为'1'
        entity.setUpdatedTime(LocalDateTime.now());
        entity.setUpdatedBy(SecurityUtils.getUsername());
        if(StrUtil.isNotBlank(workflowStatus)){
            // 更新流程审核状态
            entity.setWorkflowStatus(workflowStatus);
        }else{
            entity.setWorkflowStatus("S1");
        }

        return breedingYieldDataMapper.updateById(entity);
    }

    @Override
    public int voidYieldData(String id, String remark) {
        BreedingYieldData entity = new BreedingYieldData();
        entity.setId(id);
        entity.setWorkflowStatus("S10"); // 设置为作废状态
        entity.setRemark(remark); // 记录作废原因
        entity.setUpdatedTime(LocalDateTime.now());
        entity.setStatus("0"); // 将status字段更新为'1'
        entity.setUpdatedBy(SecurityUtils.getUsername());
        
        return breedingYieldDataMapper.updateById(entity);
    }

    /**
     * 实体转VO
     */
    private BreedingYieldDataVO convertToVO(BreedingYieldData entity) {
        BreedingYieldDataVO vo = new BreedingYieldDataVO();
        BeanUtil.copyProperties(entity, vo);
        // 字段名差异
        vo.setUpdateBy(entity.getUpdatedBy());
        vo.setUpdateTime(entity.getUpdatedTime());
        return vo;
    }

    // 审核相关逻辑暂不纳入当前版本
}