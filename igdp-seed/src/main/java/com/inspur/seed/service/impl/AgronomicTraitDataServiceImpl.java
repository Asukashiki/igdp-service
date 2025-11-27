package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.AgronomicTraitDataDTO;
import com.inspur.seed.domain.entity.AgronomicTraitData;
import com.inspur.seed.domain.vo.AgronomicTraitDataVO;
import com.inspur.seed.mapper.AgronomicTraitDataMapper;
import com.inspur.seed.service.IAgronomicTraitDataService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 农艺性状数据采集Service业务层处理
 *
 * @author igdp
 * @date 2025-11-26
 */
@Service
public class AgronomicTraitDataServiceImpl extends ServiceImpl<AgronomicTraitDataMapper, AgronomicTraitData> implements IAgronomicTraitDataService {

    @Override
    public List<AgronomicTraitDataVO> selectAgronomicTraitDataList(AgronomicTraitDataDTO dto) {
        QueryWrapper<AgronomicTraitData> wrapper = new QueryWrapper<>();

        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("create_time");

        List<AgronomicTraitData> list = this.list(wrapper);
        return list.stream()
                .map(entity -> BeanUtil.copyProperties(entity, AgronomicTraitDataVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AgronomicTraitDataVO selectAgronomicTraitDataById(String dataId) {
        AgronomicTraitData entity = this.getById(dataId);
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, AgronomicTraitDataVO.class);
    }

    @Override
    public int insertAgronomicTraitData(AgronomicTraitDataDTO dto) {
        AgronomicTraitData entity = BeanUtil.copyProperties(dto, AgronomicTraitData.class);
        entity.setDelFlag("0");
        return this.save(entity) ? 1 : 0;
    }

    @Override
    public int updateAgronomicTraitData(AgronomicTraitDataDTO dto) {
        AgronomicTraitData entity = BeanUtil.copyProperties(dto, AgronomicTraitData.class);
        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int deleteAgronomicTraitDataByIds(String[] dataIds) {
        List<AgronomicTraitData> list = Arrays.stream(dataIds)
                .map(id -> {
                    AgronomicTraitData entity = new AgronomicTraitData();
                    entity.setDataId(id);
                    entity.setDelFlag("2");
                    return entity;
                })
                .collect(Collectors.toList());
        return this.updateBatchById(list) ? list.size() : 0;
    }
}
