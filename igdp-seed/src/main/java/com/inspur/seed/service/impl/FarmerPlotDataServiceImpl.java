package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.FarmerPlotDataDTO;
import com.inspur.seed.domain.entity.FarmerPlotData;
import com.inspur.seed.domain.vo.FarmerPlotDataVO;
import com.inspur.seed.mapper.FarmerPlotDataMapper;
import com.inspur.seed.service.IFarmerPlotDataService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 农民与地块属性数据采集Service业务层处理
 *
 * @author igdp
 * @date 2025-11-26
 */
@Service
public class FarmerPlotDataServiceImpl extends ServiceImpl<FarmerPlotDataMapper, FarmerPlotData> implements IFarmerPlotDataService {

    @Override
    public List<FarmerPlotDataVO> selectFarmerPlotDataList(FarmerPlotDataDTO dto) {
        QueryWrapper<FarmerPlotData> wrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(dto.getFarmerName())) {
            wrapper.like("farmer_name", dto.getFarmerName());
        }

        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("create_time");

        List<FarmerPlotData> list = this.list(wrapper);
        return list.stream()
                .map(entity -> BeanUtil.copyProperties(entity, FarmerPlotDataVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public FarmerPlotDataVO selectFarmerPlotDataById(String dataId) {
        FarmerPlotData entity = this.getById(dataId);
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, FarmerPlotDataVO.class);
    }

    @Override
    public int insertFarmerPlotData(FarmerPlotDataDTO dto) {
        FarmerPlotData entity = BeanUtil.copyProperties(dto, FarmerPlotData.class);
        entity.setDelFlag("0");
        return this.save(entity) ? 1 : 0;
    }

    @Override
    public int updateFarmerPlotData(FarmerPlotDataDTO dto) {
        FarmerPlotData entity = BeanUtil.copyProperties(dto, FarmerPlotData.class);
        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int deleteFarmerPlotDataByIds(String[] dataIds) {
        List<FarmerPlotData> list = Arrays.stream(dataIds)
                .map(id -> {
                    FarmerPlotData entity = new FarmerPlotData();
                    entity.setDataId(id);
                    entity.setDelFlag("2");
                    return entity;
                })
                .collect(Collectors.toList());
        return this.updateBatchById(list) ? list.size() : 0;
    }
}
