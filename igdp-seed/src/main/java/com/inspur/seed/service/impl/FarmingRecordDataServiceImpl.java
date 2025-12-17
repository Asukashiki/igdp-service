package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.FarmingRecordDataDTO;
import com.inspur.seed.domain.entity.FarmingRecordData;
import com.inspur.seed.domain.vo.FarmingRecordDataVO;
import com.inspur.seed.mapper.FarmingRecordDataMapper;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.service.IFarmingRecordDataService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
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
        // 简单映射：仅保留字段同名拷贝，去除无用的 Creator/Modifier 组装逻辑
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
        // 仅返回同名字段拷贝
        return BeanUtil.copyProperties(entity, FarmingRecordDataVO.class);
    }

    @Override
    public int insertFarmingRecordData(FarmingRecordDataDTO dto) {
        FarmingRecordData entity = BeanUtil.copyProperties(dto, FarmingRecordData.class);
        entity.setDelFlag("0");
        // BaseEntity 创建信息
        entity.setCreateTime(LocalDateTime.now());
        entity.setCreateBy(SecurityUtils.getUserId().toString());
        return this.save(entity) ? 1 : 0;
    }

    @Override
    public int updateFarmingRecordData(FarmingRecordDataDTO dto) {
        FarmingRecordData entity = BeanUtil.copyProperties(dto, FarmingRecordData.class);
        // BaseEntity 修改信息
        entity.setUpdateTime(LocalDateTime.now());
        entity.setUpdateBy(SecurityUtils.getUserId().toString());
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
