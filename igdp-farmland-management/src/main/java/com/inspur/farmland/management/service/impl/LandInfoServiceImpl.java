package com.inspur.farmland.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.farmland.management.bean.entity.LandInfo;
import com.inspur.farmland.management.mapper.LandInfoMapper;
import com.inspur.farmland.management.service.ILandInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 土地信息Service实现类
 * 
 * @author inspur
 */
@Service
public class LandInfoServiceImpl extends ServiceImpl<LandInfoMapper, LandInfo> implements ILandInfoService {

    @Override
    public List<LandInfo> selectLandList(LandInfo landInfo) {
        QueryWrapper<LandInfo> queryWrapper = new QueryWrapper<>();

        // 地块名称 - 模糊查询
        if (StringUtils.hasText(landInfo.getLandName())) {
            queryWrapper.like("LAND_NAME", landInfo.getLandName());
        }

        // 地块类型 - 精确查询
        if (StringUtils.hasText(landInfo.getLandType())) {
            queryWrapper.eq("LAND_TYPE", landInfo.getLandType());
        }

        // 当前状态 - 精确查询
        if (StringUtils.hasText(landInfo.getCurrentStatus())) {
            queryWrapper.eq("CURRENT_STATUS", landInfo.getCurrentStatus());
        }

        // 行政区划 - 模糊查询,支持上级区划查询下级
        if (StringUtils.hasText(landInfo.getAdCode())) {
            queryWrapper.like("AD_CODE", landInfo.getAdCode());
        }

        // 所属农民 - 精确查询
        if (StringUtils.hasText(landInfo.getFarmerUserId())) {
            queryWrapper.eq("FARMER_USER_ID", landInfo.getFarmerUserId());
        }

        // 按创建时间倒序
        queryWrapper.orderByDesc("CREATE_TIME");

        return this.list(queryWrapper);
    }

    @Override
    public List<LandInfo> getLandsByUserId(String userId) {
        return this.lambdaQuery()
                .eq(LandInfo::getFarmerUserId, userId)
                .list();
    }

    @Override
    public boolean addLandInfo(LandInfo landInfo) {
        return this.save(landInfo);
    }

    @Override
    public boolean updateLandInfo(LandInfo landInfo) {
        return this.updateById(landInfo);
    }

    @Override
    public boolean deleteLandInfo(Long landId) {
        return this.removeById(landId);
    }
}