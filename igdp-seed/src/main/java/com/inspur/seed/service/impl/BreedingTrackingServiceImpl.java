package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.BreedingTracking;
import com.inspur.seed.mapper.BreedingTrackingMapper;
import com.inspur.seed.service.IBreedingTrackingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 育种跟踪记录服务实现类
 *
 * @author system
 */
@Service
public class BreedingTrackingServiceImpl extends ServiceImpl<BreedingTrackingMapper, BreedingTracking> implements IBreedingTrackingService {

    @Override
    public String addBreedingTracking(BreedingTracking breedingTracking) {
        // 生成跟踪ID
        String trackingId = "TRK" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
        breedingTracking.setTrackingId(trackingId);

        // 设置记录时间
        breedingTracking.setRecordTime(LocalDateTime.now());

        // 设置创建信息
        breedingTracking.setCreateBy(LoginHelper.getUsername());
        breedingTracking.setCreateTime(LocalDateTime.now());

        // 保存育种跟踪记录
        save(breedingTracking);

        return trackingId;
    }

    @Override
    public List<BreedingTracking> queryBreedingTrackingList(String batchId, String stageName, LocalDate stageCompletionDateStart, LocalDate stageCompletionDateEnd) {
        LambdaQueryWrapper<BreedingTracking> wrapper = new LambdaQueryWrapper<>();

        // 育种批次ID筛选
        if (StringUtils.isNotEmpty(batchId)) {
            wrapper.eq(BreedingTracking::getBatchId, batchId);
        }

        // 阶段名称筛选
        if (StringUtils.isNotEmpty(stageName)) {
            wrapper.eq(BreedingTracking::getStageName, stageName);
        }

        // 阶段完成日期范围筛选
        if (stageCompletionDateStart != null) {
            wrapper.ge(BreedingTracking::getStageCompletionDate, stageCompletionDateStart);
        }
        if (stageCompletionDateEnd != null) {
            wrapper.le(BreedingTracking::getStageCompletionDate, stageCompletionDateEnd);
        }

        // 按记录时间倒序排列
        wrapper.orderByDesc(BreedingTracking::getRecordTime);

        return list(wrapper);
    }

    @Override
    public BreedingTracking queryByTrackingId(String trackingId) {
        return getById(trackingId);
    }

    @Override
    public boolean editBreedingTracking(BreedingTracking breedingTracking) {
        // 查询现有跟踪记录
        BreedingTracking existingTracking = queryByTrackingId(breedingTracking.getTrackingId());
        if (existingTracking == null) {
            throw new ServiceException("育种跟踪记录不存在");
        }

        // 设置更新信息
        breedingTracking.setUpdateBy(LoginHelper.getUsername());
        breedingTracking.setUpdateTime(LocalDateTime.now());

        return updateById(breedingTracking);
    }

    @Override
    public boolean removeBreedingTracking(String trackingId) {
        return removeById(trackingId);
    }
}
