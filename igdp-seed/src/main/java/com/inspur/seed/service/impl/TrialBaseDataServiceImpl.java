package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.TrialBaseDataDTO;
import com.inspur.seed.domain.entity.TrialBaseData;
import com.inspur.seed.domain.vo.TrialBaseDataVO;
import com.inspur.seed.mapper.TrialBaseDataMapper;
import com.inspur.seed.service.ITrialBaseDataService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 试验基础数据采集Service业务层处理
 *
 * @author igdp
 * @date 2025-11-26
 */
@Service
public class TrialBaseDataServiceImpl extends ServiceImpl<TrialBaseDataMapper, TrialBaseData> implements ITrialBaseDataService {

    @Override
    public List<TrialBaseDataVO> selectTrialBaseDataList(TrialBaseDataDTO dto) {
        QueryWrapper<TrialBaseData> wrapper = new QueryWrapper<>();

        if (dto.getTrialId() != null) {
            wrapper.eq("trial_id", dto.getTrialId());
        }
        if (dto.getCropType() != null) {
            wrapper.eq("crop_type", dto.getCropType());
        }
        if (dto.getVarietyName() != null) {
            wrapper.like("variety_name", dto.getVarietyName());
        }

        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("create_time");

        List<TrialBaseData> list = this.list(wrapper);
        return list.stream()
                .map(entity -> BeanUtil.copyProperties(entity, TrialBaseDataVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TrialBaseDataVO selectTrialBaseDataById(String trialId) {
        TrialBaseData entity = this.getById(trialId);
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, TrialBaseDataVO.class);
    }

    @Override
    public int insertTrialBaseData(TrialBaseDataDTO dto) {
        TrialBaseData entity = BeanUtil.copyProperties(dto, TrialBaseData.class);
        entity.setDelFlag("0");
        return this.save(entity) ? 1 : 0;
    }

    @Override
    public int updateTrialBaseData(TrialBaseDataDTO dto) {
        TrialBaseData entity = BeanUtil.copyProperties(dto, TrialBaseData.class);
        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int deleteTrialBaseDataByIds(String[] trialIds) {
        List<TrialBaseData> list = Arrays.stream(trialIds)
                .map(id -> {
                    TrialBaseData entity = new TrialBaseData();
                    entity.setTrialId(id);
                    entity.setDelFlag("2");
                    return entity;
                })
                .collect(Collectors.toList());
        return this.updateBatchById(list) ? list.size() : 0;
    }
}
