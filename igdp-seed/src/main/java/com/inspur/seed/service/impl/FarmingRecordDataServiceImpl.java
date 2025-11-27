package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.FarmingRecordDataDTO;
import com.inspur.seed.domain.entity.FarmingRecordData;
import com.inspur.seed.domain.vo.FarmingRecordDataVO;
import com.inspur.seed.mapper.FarmingRecordDataMapper;
import com.inspur.seed.service.IFarmingRecordDataService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 农事记录数据采集Service业务层处理
 *
 * @author igdp
 * @date 2025-11-26
 */
@Service
public class FarmingRecordDataServiceImpl extends ServiceImpl<FarmingRecordDataMapper, FarmingRecordData> implements IFarmingRecordDataService {

    @Override
    public List<FarmingRecordDataVO> selectFarmingRecordDataList(FarmingRecordDataDTO dto) {
        QueryWrapper<FarmingRecordData> wrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(dto.getManagementPractice())) {
            wrapper.eq("management_practice", dto.getManagementPractice());
        }

        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("create_time");

        List<FarmingRecordData> list = this.list(wrapper);
        return list.stream()
                .map(entity -> BeanUtil.copyProperties(entity, FarmingRecordDataVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public FarmingRecordDataVO selectFarmingRecordDataById(String dataId) {
        FarmingRecordData entity = this.getById(dataId);
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, FarmingRecordDataVO.class);
    }

    @Override
    public int insertFarmingRecordData(FarmingRecordDataDTO dto) {
        FarmingRecordData entity = BeanUtil.copyProperties(dto, FarmingRecordData.class);
        entity.setDelFlag("0");
        return this.save(entity) ? 1 : 0;
    }

    @Override
    public int updateFarmingRecordData(FarmingRecordDataDTO dto) {
        FarmingRecordData entity = BeanUtil.copyProperties(dto, FarmingRecordData.class);
        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int deleteFarmingRecordDataByIds(String[] dataIds) {
        List<FarmingRecordData> list = Arrays.stream(dataIds)
                .map(id -> {
                    FarmingRecordData entity = new FarmingRecordData();
                    entity.setDataId(id);
                    entity.setDelFlag("2");
                    return entity;
                })
                .collect(Collectors.toList());
        return this.updateBatchById(list) ? list.size() : 0;
    }
}
