package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.LaboratoryTestDataDTO;
import com.inspur.seed.domain.entity.LaboratoryTestData;
import com.inspur.seed.domain.vo.LaboratoryTestDataVO;
import com.inspur.seed.mapper.LaboratoryTestDataMapper;
import com.inspur.seed.service.ILaboratoryTestDataService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实验室测试数据采集Service业务层处理
 *
 * @author igdp
 * @date 2025-11-26
 */
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
        LaboratoryTestData entity = BeanUtil.copyProperties(dto, LaboratoryTestData.class);
        entity.setDelFlag("0");
        return this.save(entity) ? 1 : 0;
    }

    @Override
    public int updateLaboratoryTestData(LaboratoryTestDataDTO dto) {
        LaboratoryTestData entity = BeanUtil.copyProperties(dto, LaboratoryTestData.class);
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
