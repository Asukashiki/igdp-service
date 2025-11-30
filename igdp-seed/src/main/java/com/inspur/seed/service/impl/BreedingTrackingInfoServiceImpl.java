package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.BreedingTrackingAddDTO;
import com.inspur.seed.domain.dto.BreedingTrackingQueryDTO;
import com.inspur.seed.domain.dto.BreedingTrackingUpdateDTO;
import com.inspur.seed.domain.entity.BreedingTrackingInfo;
import com.inspur.seed.domain.vo.BreedingTrackingVO;
import com.inspur.seed.mapper.BreedingTrackingInfoMapper;
import com.inspur.seed.service.IBreedingTrackingInfoService;
import com.inspur.seed.utils.BreedingCodeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 繁殖跟踪信息Service实现类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Service
public class BreedingTrackingInfoServiceImpl extends ServiceImpl<BreedingTrackingInfoMapper, BreedingTrackingInfo> implements IBreedingTrackingInfoService {

    @Override
    public IPage<BreedingTrackingVO> queryPage(BreedingTrackingQueryDTO queryDTO) {
        // 构建分页条件
        Page<BreedingTrackingInfo> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 构建查询条件
        QueryWrapper<BreedingTrackingInfo> queryWrapper = buildQueryWrapper(queryDTO);

        // 执行分页查询
        IPage<BreedingTrackingInfo> resultPage = this.page(page, queryWrapper);

        // 转换为VO
        return resultPage.convert(this::convertToVO);
    }

    @Override
    public List<BreedingTrackingVO> queryList(BreedingTrackingQueryDTO queryDTO) {
        // 构建查询条件
        QueryWrapper<BreedingTrackingInfo> queryWrapper = buildQueryWrapper(queryDTO);

        // 执行查询
        List<BreedingTrackingInfo> list = this.list(queryWrapper);

        // 转换为VO
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public String add(BreedingTrackingAddDTO addDTO) {
        // 创建实体对象
        BreedingTrackingInfo trackingInfo = new BreedingTrackingInfo();
        BeanUtils.copyProperties(addDTO, trackingInfo);

        // 自动生成跟踪编号
        trackingInfo.setTrackingId(BreedingCodeUtil.generateTrackingId());

        // 设置创建时间
        trackingInfo.setCreateTime(new Date());

        // 保存数据
        this.save(trackingInfo);

        return trackingInfo.getId();
    }

    @Override
    public boolean update(BreedingTrackingUpdateDTO updateDTO) {
        // 创建实体对象
        BreedingTrackingInfo trackingInfo = new BreedingTrackingInfo();
        BeanUtils.copyProperties(updateDTO, trackingInfo);

        // 设置更新时间
        trackingInfo.setUpdateTime(new Date());

        // 执行更新
        return this.updateById(trackingInfo);
    }

    @Override
    public BreedingTrackingVO detail(String id) {
        BreedingTrackingInfo trackingInfo = this.getById(id);
        if (trackingInfo == null) {
            return null;
        }
        return convertToVO(trackingInfo);
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
    private QueryWrapper<BreedingTrackingInfo> buildQueryWrapper(BreedingTrackingQueryDTO queryDTO) {
        QueryWrapper<BreedingTrackingInfo> queryWrapper = new QueryWrapper<>();

        // 跟踪编号（模糊查询）
        if (queryDTO.getTrackingId() != null && !queryDTO.getTrackingId().isEmpty()) {
            queryWrapper.like("tracking_id", queryDTO.getTrackingId());
        }

        // 批次编号（精确查询）
        if (queryDTO.getBatchId() != null && !queryDTO.getBatchId().isEmpty()) {
            queryWrapper.eq("batch_id", queryDTO.getBatchId());
        }

        // 作物类型（精确查询）
        if (queryDTO.getCropType() != null && !queryDTO.getCropType().isEmpty()) {
            queryWrapper.eq("crop_type", queryDTO.getCropType());
        }

        // 阶段名称（精确查询）
        if (queryDTO.getStageName() != null && !queryDTO.getStageName().isEmpty()) {
            queryWrapper.eq("stage_name", queryDTO.getStageName());
        }

        // 跟踪结果（精确查询）
        if (queryDTO.getTrackingResult() != null && !queryDTO.getTrackingResult().isEmpty()) {
            queryWrapper.eq("tracking_result", queryDTO.getTrackingResult());
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
     * @param trackingInfo 实体对象
     * @return VO对象
     */
    private BreedingTrackingVO convertToVO(BreedingTrackingInfo trackingInfo) {
        BreedingTrackingVO vo = new BreedingTrackingVO();
        BeanUtils.copyProperties(trackingInfo, vo);

        // 根据枚举值翻译名称
        vo.setCropTypeName(getCropTypeName(trackingInfo.getCropType()));
        vo.setStageNameText(getStageName(trackingInfo.getStageName()));
        vo.setTrackingResultName(getTrackingResultName(trackingInfo.getTrackingResult()));

        // 统计检测记录数量
        if (trackingInfo.getTrackingId() != null) {
            Integer testCount = this.baseMapper.countTestByTrackingId(trackingInfo.getTrackingId());
            vo.setTestCount(testCount != null ? testCount : 0);
        } else {
            vo.setTestCount(0);
        }

        return vo;
    }

    /**
     * 获取作物类型名称
     */
    private String getCropTypeName(String cropType) {
        if (cropType == null) return "";
        switch (cropType) {
            case "WHEAT": return "小麦";
            case "CORN": return "玉米";
            case "RICE": return "水稻";
            case "SOYBEAN": return "大豆";
            case "COTTON": return "棉花";
            default: return "";
        }
    }

    /**
     * 获取阶段名称
     */
    private String getStageName(String stageName) {
        if (stageName == null) return "";
        switch (stageName) {
            case "01": return "亲本系准备";
            case "02": return "原原种繁殖";
            case "03": return "原种繁殖";
            case "04": return "良种生产";
            default: return "";
        }
    }

    /**
     * 获取跟踪结果名称
     */
    private String getTrackingResultName(String trackingResult) {
        if (trackingResult == null) return "";
        switch (trackingResult) {
            case "01": return "正常";
            case "02": return "异常";
            case "03": return "待观察";
            default: return "";
        }
    }
}
