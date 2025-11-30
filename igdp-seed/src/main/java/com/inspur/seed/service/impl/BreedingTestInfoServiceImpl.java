package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.BreedingTestAddDTO;
import com.inspur.seed.domain.dto.BreedingTestQueryDTO;
import com.inspur.seed.domain.dto.BreedingTestUpdateDTO;
import com.inspur.seed.domain.entity.BreedingTestInfo;
import com.inspur.seed.domain.vo.BreedingTestVO;
import com.inspur.seed.mapper.BreedingTestInfoMapper;
import com.inspur.seed.service.IBreedingTestInfoService;
import com.inspur.seed.utils.BreedingCodeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 繁殖检测信息Service实现类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Service
public class BreedingTestInfoServiceImpl extends ServiceImpl<BreedingTestInfoMapper, BreedingTestInfo> implements IBreedingTestInfoService {

    @Override
    public IPage<BreedingTestVO> queryPage(BreedingTestQueryDTO queryDTO) {
        // 构建分页条件
        Page<BreedingTestInfo> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 构建查询条件
        QueryWrapper<BreedingTestInfo> queryWrapper = buildQueryWrapper(queryDTO);

        // 执行分页查询
        IPage<BreedingTestInfo> resultPage = this.page(page, queryWrapper);

        // 转换为VO
        return resultPage.convert(this::convertToVO);
    }

    @Override
    public List<BreedingTestVO> queryList(BreedingTestQueryDTO queryDTO) {
        // 构建查询条件
        QueryWrapper<BreedingTestInfo> queryWrapper = buildQueryWrapper(queryDTO);

        // 执行查询
        List<BreedingTestInfo> list = this.list(queryWrapper);

        // 转换为VO
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public String add(BreedingTestAddDTO addDTO) {
        // 创建实体对象
        BreedingTestInfo testInfo = new BreedingTestInfo();
        BeanUtils.copyProperties(addDTO, testInfo);

        // 自动生成检测编号
        testInfo.setTestId(BreedingCodeUtil.generateTestId());

        // 设置创建时间
        testInfo.setCreateTime(new Date());

        // 保存数据
        this.save(testInfo);

        return testInfo.getId();
    }

    @Override
    public boolean update(BreedingTestUpdateDTO updateDTO) {
        // 创建实体对象
        BreedingTestInfo testInfo = new BreedingTestInfo();
        BeanUtils.copyProperties(updateDTO, testInfo);

        // 设置更新时间
        testInfo.setUpdateTime(new Date());

        // 执行更新
        return this.updateById(testInfo);
    }

    @Override
    public BreedingTestVO detail(String id) {
        BreedingTestInfo testInfo = this.getById(id);
        if (testInfo == null) {
            return null;
        }
        return convertToVO(testInfo);
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
    private QueryWrapper<BreedingTestInfo> buildQueryWrapper(BreedingTestQueryDTO queryDTO) {
        QueryWrapper<BreedingTestInfo> queryWrapper = new QueryWrapper<>();

        // 检测编号（模糊查询）
        if (queryDTO.getTestId() != null && !queryDTO.getTestId().isEmpty()) {
            queryWrapper.like("test_id", queryDTO.getTestId());
        }

        // 跟踪编号（精确查询）
        if (queryDTO.getTrackingId() != null && !queryDTO.getTrackingId().isEmpty()) {
            queryWrapper.eq("tracking_id", queryDTO.getTrackingId());
        }

        // 批次编号（精确查询）
        if (queryDTO.getBatchId() != null && !queryDTO.getBatchId().isEmpty()) {
            queryWrapper.eq("batch_id", queryDTO.getBatchId());
        }

        // 作物种类（精确查询）
        if (queryDTO.getCropType() != null && !queryDTO.getCropType().isEmpty()) {
            queryWrapper.eq("crop_type", queryDTO.getCropType());
        }

        // 检测结论（精确查询）
        if (queryDTO.getTestResult() != null && !queryDTO.getTestResult().isEmpty()) {
            queryWrapper.eq("test_result", queryDTO.getTestResult());
        }

        // 检测日期范围查询
        if (queryDTO.getTestDateBegin() != null) {
            queryWrapper.ge("test_date", queryDTO.getTestDateBegin());
        }
        if (queryDTO.getTestDateEnd() != null) {
            queryWrapper.le("test_date", queryDTO.getTestDateEnd());
        }

        // 按创建时间倒序
        queryWrapper.orderByDesc("create_time");

        return queryWrapper;
    }

    /**
     * 转换为VO对象
     *
     * @param testInfo 实体对象
     * @return VO对象
     */
    private BreedingTestVO convertToVO(BreedingTestInfo testInfo) {
        BreedingTestVO vo = new BreedingTestVO();
        BeanUtils.copyProperties(testInfo, vo);

        // 根据枚举值翻译名称
        vo.setCropTypeName(getCropTypeName(testInfo.getCropType()));
        vo.setTestResultName(getTestResultName(testInfo.getTestResult()));

        return vo;
    }

    /**
     * 获取作物种类名称
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
     * 获取检测结论名称
     */
    private String getTestResultName(String testResult) {
        if (testResult == null) return "";
        switch (testResult) {
            case "01": return "合格";
            case "02": return "不合格";
            case "03": return "待复检";
            default: return "";
        }
    }
}
