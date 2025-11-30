package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.dto.BreedingLabTestDTO;
import com.inspur.seed.domain.entity.BreedingLabTest;
import com.inspur.seed.domain.vo.BreedingLabTestVO;
import com.inspur.seed.mapper.BreedingLabTestMapper;
import com.inspur.seed.service.IBreedingLabTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实验室测试数据Service实现类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Service
public class BreedingLabTestServiceImpl implements IBreedingLabTestService {

    @Autowired
    private BreedingLabTestMapper breedingLabTestMapper;

    @Override
    public Map<String, Object> selectBreedingLabTestList(BreedingLabTestDTO dto) {
        QueryWrapper<BreedingLabTest> queryWrapper = buildQueryWrapper(dto);
        queryWrapper.orderByDesc("created_time");

        Map<String, Object> result = new HashMap<>();

        // 如果有分页参数,使用分页查询
        if (dto.getPageNum() != null && dto.getPageSize() != null) {
            Page<BreedingLabTest> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            IPage<BreedingLabTest> pageResult = breedingLabTestMapper.selectPage(page, queryWrapper);
            List<BreedingLabTestVO> voList = pageResult.getRecords().stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

            result.put("list", voList);
            result.put("total", pageResult.getTotal());
            return result;
        }

        // 无分页参数,返回全部数据
        List<BreedingLabTest> list = breedingLabTestMapper.selectList(queryWrapper);
        List<BreedingLabTestVO> voList = list.stream().map(this::convertToVO).collect(Collectors.toList());

        result.put("list", voList);
        result.put("total", voList.size());
        return result;
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<BreedingLabTest> buildQueryWrapper(BreedingLabTestDTO dto) {
        QueryWrapper<BreedingLabTest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", "0");
        queryWrapper.eq("status", "1");

        // 育种批次ID
        if (StrUtil.isNotBlank(dto.getBatchId())) {
            queryWrapper.eq("batch_id", dto.getBatchId());
        }

        // 试验ID
        if (StrUtil.isNotBlank(dto.getTrialId())) {
            queryWrapper.eq("trial_id", dto.getTrialId());
        }

        // 样本编号模糊查询
        if (StrUtil.isNotBlank(dto.getSampleId())) {
            queryWrapper.like("sample_id", dto.getSampleId());
        }

        // 检测日期范围
        if (dto.getTestDateStart() != null) {
            queryWrapper.ge("test_date", dto.getTestDateStart());
        }
        if (dto.getTestDateEnd() != null) {
            queryWrapper.le("test_date", dto.getTestDateEnd());
        }

        return queryWrapper;
    }

    @Override
    public BreedingLabTestVO selectBreedingLabTestById(String id) {
        BreedingLabTest entity = breedingLabTestMapper.selectById(id);
        return entity != null ? convertToVO(entity) : null;
    }

    @Override
    public int insertBreedingLabTest(BreedingLabTestDTO dto) {
        BreedingLabTest entity = new BreedingLabTest();
        BeanUtil.copyProperties(dto, entity);

        // 设置系统字段
        entity.setStatus("1");
        entity.setDeleted("0");
        entity.setCreatedTime(LocalDateTime.now());
        try {
            entity.setCreatedBy(SecurityUtils.getUsername());
        } catch (Exception e) {
            // 如果获取用户失败，使用默认值
            entity.setCreatedBy("system");
        }

        return breedingLabTestMapper.insert(entity);
    }

    @Override
    public int updateBreedingLabTest(BreedingLabTestDTO dto) {
        BreedingLabTest entity = new BreedingLabTest();
        BeanUtil.copyProperties(dto, entity);

        // 设置更新时间
        entity.setUpdatedTime(LocalDateTime.now());
        try {
            entity.setUpdatedBy(SecurityUtils.getUsername());
        } catch (Exception e) {
            // 如果获取用户失败，使用默认值
            entity.setUpdatedBy("system");
        }

        return breedingLabTestMapper.updateById(entity);
    }

    @Override
    public int deleteBreedingLabTestByIds(String[] ids) {
        // 逻辑删除
        return Arrays.stream(ids).mapToInt(id -> {
            BreedingLabTest entity = new BreedingLabTest();
            entity.setId(id);
            entity.setDeleted("1");
            entity.setUpdatedTime(LocalDateTime.now());
            return breedingLabTestMapper.updateById(entity);
        }).sum();
    }

    /**
     * 实体转VO
     */
    private BreedingLabTestVO convertToVO(BreedingLabTest entity) {
        BreedingLabTestVO vo = new BreedingLabTestVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
