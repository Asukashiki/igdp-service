package com.inspur.assets.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.assets.monitor.domain.AssetsAlertCur;
import com.inspur.assets.monitor.domain.AssetsCurConfiguration;
import com.inspur.common.core.domain.AjaxResult;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author 王海龙
 * @version 1.0
 * @ClassName IAssetsAlertCurService
 * @date 2024/9/10 11:21
 */
public interface IAssetsCurConfigurationService extends IService<AssetsCurConfiguration> {
    /**
     * 查询当前告警通知配置信息列表
     * @param param 查询条件
     * @return 列表集合
     * */
    List<AssetsCurConfiguration> getList(AssetsCurConfiguration param);

    /**
     * 新增告警通知配置信息
     * @param assetsCurConfiguration
     * @return 结果
     */
    AjaxResult add(AssetsCurConfiguration assetsCurConfiguration);

    /**
     * 修改告警通知信息
     * @param assetsCurConfiguration
     */
    boolean updateCur(AssetsCurConfiguration assetsCurConfiguration);

//    /**
//     * 定时任务执行
//     * @param assetsCurConfiguration
//     * @return
//     * @throws SchedulerException
//     */
//    public boolean run(AssetsCurConfiguration assetsCurConfiguration) throws SchedulerException;
}
