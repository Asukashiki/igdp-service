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
            wrapper.eq("sample_id", dto.getSampleId());
        }

        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("create_time");

        List<LaboratoryTestData> list = this.list(wrapper);
        return list.stream()
                .map(entity -> BeanUtil.copyProperties(entity, LaboratoryTestDataVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public LaboratoryTestDataVO selectLaboratoryTestDataById(String dataId) {
        LaboratoryTestData entity = this.getById(dataId);
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, LaboratoryTestDataVO.class);
    }

    @Override
    public int insertLaboratoryTestData(LaboratoryTestDataDTO dto) {
        log.info("开始插入实验室测试数据, sampleId: {}", dto.getSampleId());

        // 验证必填字段
        if (StrUtil.isBlank(dto.getSampleId())) {
            log.error("样本ID不能为空");
            throw new IllegalArgumentException("样本ID不能为空");
        }
        if (StrUtil.isBlank(dto.getSampleCondition())) {
            log.error("样本状态不能为空");
            throw new IllegalArgumentException("样本状态不能为空");
        }
        if (dto.getGerminationRate() == null) {
            log.error("发芽率不能为空");
            throw new IllegalArgumentException("发芽率不能为空");
        }
        if (dto.getPurityPercent() == null) {
            log.error("纯度不能为空");
            throw new IllegalArgumentException("纯度不能为空");
        }
        if (dto.getMoistureContentPercent() == null) {
            log.error("含水量不能为空");
            throw new IllegalArgumentException("含水量不能为空");
        }
        if (dto.getProteinPercent() == null) {
            log.error("蛋白质不能为空");
            throw new IllegalArgumentException("蛋白质不能为空");
        }
        if (StrUtil.isBlank(dto.getSeedHealthFindings())) {
            log.error("种子健康发现不能为空");
            throw new IllegalArgumentException("种子健康发现不能为空");
        }
        if (StrUtil.isBlank(dto.getTraceabilityLink())) {
            log.error("链路责任不能为空");
            throw new IllegalArgumentException("链路责任不能为空");
        }

        LaboratoryTestData entity = BeanUtil.copyProperties(dto, LaboratoryTestData.class);
        entity.setDelFlag("0");

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
}
