package com.inspur.assets.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.monitor.domain.AssetsCurConfiguration;
import com.inspur.assets.monitor.domain.util.ConfigurationUtils;
import com.inspur.assets.monitor.mapper.AssetsCurConfigurationMapper;
import com.inspur.assets.monitor.service.IAssetsCurConfigurationService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.exception.job.TaskException;
import com.inspur.common.utils.StringUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

import static com.inspur.common.utils.LoginHelper.getUsername;

/**
 * @author 王海龙
 * @version 1.0
 * @ClassName IAssetsAlertCurService
 * @date 2024/9/10 11:21
 */
@Service
public class AssetsCurConfigurationServiceImpl extends ServiceImpl<AssetsCurConfigurationMapper, AssetsCurConfiguration> implements IAssetsCurConfigurationService {
    @Autowired
    private Scheduler scheduler;

    @Override
    public List<AssetsCurConfiguration> getList(AssetsCurConfiguration param) {
        LambdaQueryWrapper<AssetsCurConfiguration> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(param.getSource()),AssetsCurConfiguration::getSource,param.getSource());
        queryWrapper.eq(StringUtils.isNotEmpty(param.getGrade()),AssetsCurConfiguration::getGrade,param.getGrade());
        queryWrapper.eq(StringUtils.isNotEmpty(param.getAssetsType()),AssetsCurConfiguration::getAssetsType,param.getAssetsType());
        return list(queryWrapper);
    }

    @Override
    public AjaxResult add(AssetsCurConfiguration assetsCurConfiguration) {
        assetsCurConfiguration.setCreateBy(getUsername());
        assetsCurConfiguration.setCreateTime(LocalDateTime.now());
        save(assetsCurConfiguration);
        return AjaxResult.success();
    }

    @Override
    public boolean updateCur(AssetsCurConfiguration assetsCurConfiguration) {
        assetsCurConfiguration.setUpdateBy(getUsername());
        assetsCurConfiguration.setUpdateTime(LocalDateTime.now());
        return updateById(assetsCurConfiguration);
    }

    @PostConstruct
    public void init() throws SchedulerException, TaskException
    {
        AssetsCurConfiguration assetsCurConfiguration = new AssetsCurConfiguration();
        scheduler.clear();
        List<AssetsCurConfiguration> jobList = getList(assetsCurConfiguration);
        for (AssetsCurConfiguration job : jobList)
        {
            ConfigurationUtils.createScheduleJob(scheduler,job);
        }
    }

}
