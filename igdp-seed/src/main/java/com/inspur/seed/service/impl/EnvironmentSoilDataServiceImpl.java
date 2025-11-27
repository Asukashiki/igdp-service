package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.EnvironmentSoilDataDTO;
import com.inspur.seed.domain.entity.EnvironmentSoilData;
import com.inspur.seed.domain.vo.EnvironmentSoilDataVO;
import com.inspur.seed.mapper.EnvironmentSoilDataMapper;
import com.inspur.seed.service.IEnvironmentSoilDataService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 环境与土壤属性数据采集Service业务层处理
 *
 * @author igdp
 * @date 2025-11-26
 */
@Service
public class EnvironmentSoilDataServiceImpl extends ServiceImpl<EnvironmentSoilDataMapper, EnvironmentSoilData> implements IEnvironmentSoilDataService {

    @Override
    public List<EnvironmentSoilDataVO> selectEnvironmentSoilDataList(EnvironmentSoilDataDTO dto) {
        QueryWrapper<EnvironmentSoilData> wrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(dto.getTopography())) {
            wrapper.eq("topography", dto.getTopography());
        }
        if (StrUtil.isNotBlank(dto.getWaterSource())) {
            wrapper.eq("water_source", dto.getWaterSource());
        }

        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("create_time");

        List<EnvironmentSoilData> list = this.list(wrapper);
        return list.stream()
                .map(entity -> BeanUtil.copyProperties(entity, EnvironmentSoilDataVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EnvironmentSoilDataVO selectEnvironmentSoilDataById(String dataId) {
        EnvironmentSoilData entity = this.getById(dataId);
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, EnvironmentSoilDataVO.class);
    }

    @Override
    public int insertEnvironmentSoilData(EnvironmentSoilDataDTO dto) {
        EnvironmentSoilData entity = BeanUtil.copyProperties(dto, EnvironmentSoilData.class);
        entity.setDelFlag("0");
        return this.save(entity) ? 1 : 0;
    }

    @Override
    public int updateEnvironmentSoilData(EnvironmentSoilDataDTO dto) {
        EnvironmentSoilData entity = BeanUtil.copyProperties(dto, EnvironmentSoilData.class);
        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int deleteEnvironmentSoilDataByIds(String[] dataIds) {
        List<EnvironmentSoilData> list = Arrays.stream(dataIds)
                .map(id -> {
                    EnvironmentSoilData entity = new EnvironmentSoilData();
                    entity.setDataId(id);
                    entity.setDelFlag("2");
                    return entity;
                })
                .collect(Collectors.toList());
        return this.updateBatchById(list) ? list.size() : 0;
    }
}
