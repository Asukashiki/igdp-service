package com.inspur.assets.monitor.service;

import com.inspur.assets.monitor.domain.AssetsAlertCur;
import com.inspur.assets.monitor.domain.payload.AssetsAlertEventPayload;
import com.inspur.common.core.domain.AjaxResult;

import java.util.List;

/**
 * 资产告警通用service
 * @author liyunlong
 * @version 1.0
 * @ClassName IAssetsAlertService
 * @date 2024/7/15 19:54
 */
public interface IAssetsAlertService {
    /**
     * 接收统一的告警信息，处理信息内容并保存到活跃告警中或者历史告警中
     * @param eventPayload 告警通知载体内容
     * @return 结果
     * */
    AjaxResult pushAssertAlert(AssetsAlertEventPayload eventPayload);

    /**
     * 批量处理接收的告警信息
     * @param alarmList
     * @return
     */
    AjaxResult batchPushAssertAlert(List<AssetsAlertEventPayload> alarmList);
    /**
     * 获取当前告警列表
     * @return 当前告警列表
     * */
    List<AssetsAlertCur> getAlertsCurList();
}
