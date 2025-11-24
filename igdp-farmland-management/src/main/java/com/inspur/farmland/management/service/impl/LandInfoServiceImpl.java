package com.inspur.farmland.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.farmland.management.bean.entity.LandInfo;
import com.inspur.farmland.management.mapper.LandInfoMapper;
import com.inspur.farmland.management.service.ILandInfoService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 土地信息Service实现类
 * 
 * @author inspur
 */
@Service
public class LandInfoServiceImpl extends ServiceImpl<LandInfoMapper, LandInfo> implements ILandInfoService {

    @Override
    public List<LandInfo> getLandsByUserId(Long userId) {
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