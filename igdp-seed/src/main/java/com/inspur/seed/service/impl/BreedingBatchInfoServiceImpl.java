package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.utils.MessageUtils;
import com.inspur.seed.domain.dto.BreedingBatchAddDTO;
import com.inspur.seed.domain.dto.BreedingBatchQueryDTO;
import com.inspur.seed.domain.dto.BreedingBatchUpdateDTO;
import com.inspur.seed.domain.entity.BreedingBatchInfo;
import com.inspur.seed.domain.vo.BreedingBatchVO;
import com.inspur.seed.mapper.BreedingBatchInfoMapper;
import com.inspur.seed.service.IBreedingBatchInfoService;
import com.inspur.seed.utils.BreedingCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 繁殖批次信息Service实现类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Slf4j
@Service
public class BreedingBatchInfoServiceImpl extends ServiceImpl<BreedingBatchInfoMapper, BreedingBatchInfo> implements IBreedingBatchInfoService {

    @Override
    public IPage<BreedingBatchVO> queryPage(BreedingBatchQueryDTO queryDTO) {
        // 构建分页条件
        Page<BreedingBatchInfo> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 构建查询条件
        QueryWrapper<BreedingBatchInfo> queryWrapper = buildQueryWrapper(queryDTO);

        // 执行分页查询
        IPage<BreedingBatchInfo> resultPage = this.page(page, queryWrapper);

        // 转换为VO
        return resultPage.convert(this::convertToVO);
    }

    @Override
    public List<BreedingBatchVO> queryList(BreedingBatchQueryDTO queryDTO) {
        // 构建查询条件
        QueryWrapper<BreedingBatchInfo> queryWrapper = buildQueryWrapper(queryDTO);

        // 执行查询
        List<BreedingBatchInfo> list = this.list(queryWrapper);

        // 转换为VO
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public String add(BreedingBatchAddDTO addDTO) {
        // 创建实体对象
        BreedingBatchInfo batchInfo = new BreedingBatchInfo();
        BeanUtils.copyProperties(addDTO, batchInfo);

        // 自动生成批次编号
        batchInfo.setBatchId(BreedingCodeUtil.generateBatchId());

        // 自动生成品种编码：作物类型_品种名称
        if (batchInfo.getCropType() != null && batchInfo.getVarietyName() != null) {
            String varietyCode = batchInfo.getCropType() + "_" + batchInfo.getVarietyName();
            batchInfo.setVarietyCode(varietyCode);
        }

        // 设置默认状态为"进行中"
        batchInfo.setBatchStatus("01");

        // 设置创建时间
        batchInfo.setCreateTime(new Date());

        // 保存数据
        this.save(batchInfo);

        return batchInfo.getId();
    }

    @Override
    public boolean update(BreedingBatchUpdateDTO updateDTO) {
        // 创建实体对象
        BreedingBatchInfo batchInfo = new BreedingBatchInfo();
        BeanUtils.copyProperties(updateDTO, batchInfo);

        // 自动生成品种编码：作物类型_品种名称
        if (batchInfo.getCropType() != null && batchInfo.getVarietyName() != null) {
            String varietyCode = batchInfo.getCropType() + "_" + batchInfo.getVarietyName();
            batchInfo.setVarietyCode(varietyCode);
        }

        // 设置更新时间
        batchInfo.setUpdateTime(new Date());

        // 执行更新
        return this.updateById(batchInfo);
    }

    @Override
    public BreedingBatchVO detail(String id) {
        BreedingBatchInfo batchInfo = this.getById(id);
        if (batchInfo == null) {
            return null;
        }
        return convertToVO(batchInfo);
    }

    @Override
    public boolean delete(List<String> ids) {
        return this.removeBatchByIds(ids);
    }

    /**
     * 构建查询条件
     *
     * @param queryDTO 查询条件DTO
     * @return QueryWrapper
     */
    private QueryWrapper<BreedingBatchInfo> buildQueryWrapper(BreedingBatchQueryDTO queryDTO) {
        QueryWrapper<BreedingBatchInfo> queryWrapper = new QueryWrapper<>();

        // 批次编号（模糊查询）
        if (queryDTO.getBatchId() != null && !queryDTO.getBatchId().isEmpty()) {
            queryWrapper.like("batch_id", queryDTO.getBatchId());
        }

        // 作物类型（精确查询）
        if (queryDTO.getCropType() != null && !queryDTO.getCropType().isEmpty()) {
            queryWrapper.eq("crop_type", queryDTO.getCropType());
        }

        // 品种名称（模糊查询）
        if (queryDTO.getVarietyName() != null && !queryDTO.getVarietyName().isEmpty()) {
            queryWrapper.like("variety_name", queryDTO.getVarietyName());
        }

        // 繁殖级别（精确查询）
        if (queryDTO.getBreedingLevel() != null && !queryDTO.getBreedingLevel().isEmpty()) {
            queryWrapper.eq("breeding_level", queryDTO.getBreedingLevel());
        }

        // 批次状态（精确查询）
        if (queryDTO.getBatchStatus() != null && !queryDTO.getBatchStatus().isEmpty()) {
            queryWrapper.eq("batch_status", queryDTO.getBatchStatus());
        }

        // 开始日期范围查询
        if (queryDTO.getStartDateBegin() != null) {
            queryWrapper.ge("start_date", queryDTO.getStartDateBegin());
        }
        if (queryDTO.getStartDateEnd() != null) {
            queryWrapper.le("start_date", queryDTO.getStartDateEnd());
        }

        // 按创建时间倒序
        queryWrapper.orderByDesc("create_time");

        return queryWrapper;
    }

    /**
     * 转换为VO对象
     *
     * @param batchInfo 实体对象
     * @return VO对象
     */
    private BreedingBatchVO convertToVO(BreedingBatchInfo batchInfo) {
        BreedingBatchVO vo = new BreedingBatchVO();
        BeanUtils.copyProperties(batchInfo, vo);

        // 根据枚举值翻译名称
        vo.setCropTypeName(getCropTypeName(batchInfo.getCropType()));
        vo.setBreedingLevelName(getBreedingLevelName(batchInfo.getBreedingLevel()));
        vo.setBatchStatusName(getBatchStatusName(batchInfo.getBatchStatus()));

        // 统计跟踪和检测记录数量
        if (batchInfo.getBatchId() != null) {
            Integer trackingCount = this.baseMapper.countTrackingByBatchId(batchInfo.getBatchId());
            Integer testCount = this.baseMapper.countTestByBatchId(batchInfo.getBatchId());
            vo.setTrackingCount(trackingCount != null ? trackingCount : 0);
            vo.setTestCount(testCount != null ? testCount : 0);
        } else {
            vo.setTrackingCount(0);
            vo.setTestCount(0);
        }

        return vo;
    }

    /**
     * 获取作物类型名称（支持国际化）
     *
     * @param cropType 作物类型编码
     * @return 作物类型名称
     */
    private String getCropTypeName(String cropType) {
        if (cropType == null || cropType.isEmpty()) return "";
        // 转换为小写进行匹配
        String lowerCropType = cropType.toLowerCase();
        String messageKey = "crop.type." + lowerCropType;
        try {
            return MessageUtils.message(messageKey);
        } catch (Exception e) {
            // 如果找不到对应的国际化key，返回原值
            return cropType;
        }
    }

    /**
     * 获取繁殖级别名称（支持国际化）
     *
     * @param breedingLevel 繁殖级别编码
     * @return 繁殖级别名称
     */
    private String getBreedingLevelName(String breedingLevel) {
        if (breedingLevel == null || breedingLevel.isEmpty()) return "";
        String messageKey = "breeding.level." + breedingLevel;
        try {
            return MessageUtils.message(messageKey);
        } catch (Exception e) {
            // 如果找不到对应的国际化key，返回原值
            return breedingLevel;
        }
    }

    /**
     * 获取批次状态名称（支持国际化）
     *
     * @param batchStatus 批次状态编码
     * @return 批次状态名称
     */
    private String getBatchStatusName(String batchStatus) {
        if (batchStatus == null || batchStatus.isEmpty()) return "";
        String messageKey = "batch.status." + batchStatus;
        try {
            return MessageUtils.message(messageKey);
        } catch (Exception e) {
            // 如果找不到对应的国际化key，返回原值
            return batchStatus;
        }
    }
}
