package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.VarietyEvaluationDataDTO;
import com.inspur.seed.domain.entity.VarietyEvaluationData;
import com.inspur.seed.domain.vo.VarietyEvaluationDataVO;
import com.inspur.seed.mapper.VarietyEvaluationDataMapper;
import com.inspur.seed.service.IVarietyEvaluationDataService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 品种评估数据采集Service业务层处理
 *
 * @author igdp
 * @date 2025-11-26
 */
@Service
public class VarietyEvaluationDataServiceImpl extends ServiceImpl<VarietyEvaluationDataMapper, VarietyEvaluationData> implements IVarietyEvaluationDataService {

    @Override
    public List<VarietyEvaluationDataVO> selectVarietyEvaluationDataList(VarietyEvaluationDataDTO dto) {
        QueryWrapper<VarietyEvaluationData> wrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(dto.getPlotId())) {
            wrapper.eq("plot_id", dto.getPlotId());
        }

        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("create_time");

        List<VarietyEvaluationData> list = this.list(wrapper);
        return list.stream()
                .map(entity -> BeanUtil.copyProperties(entity, VarietyEvaluationDataVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public VarietyEvaluationDataVO selectVarietyEvaluationDataById(String dataId) {
        VarietyEvaluationData entity = this.getById(dataId);
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, VarietyEvaluationDataVO.class);
    }

    @Override
    public int insertVarietyEvaluationData(VarietyEvaluationDataDTO dto) {
        VarietyEvaluationData entity = BeanUtil.copyProperties(dto, VarietyEvaluationData.class);
        entity.setDelFlag("0");
        return this.save(entity) ? 1 : 0;
    }

    @Override
    public int updateVarietyEvaluationData(VarietyEvaluationDataDTO dto) {
        VarietyEvaluationData entity = BeanUtil.copyProperties(dto, VarietyEvaluationData.class);
        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int deleteVarietyEvaluationDataByIds(String[] dataIds) {
        List<VarietyEvaluationData> list = Arrays.stream(dataIds)
                .map(id -> {
                    VarietyEvaluationData entity = new VarietyEvaluationData();
                    entity.setDataId(id);
                    entity.setDelFlag("2");
                    return entity;
                })
                .collect(Collectors.toList());
        return this.updateBatchById(list) ? list.size() : 0;
    }
}
