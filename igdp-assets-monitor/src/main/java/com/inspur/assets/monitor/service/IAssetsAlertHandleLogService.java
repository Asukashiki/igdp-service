package com.inspur.assets.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.assets.monitor.domain.AssetsAlertHandleLog;
import com.inspur.common.core.domain.AjaxResult;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName IAssetsAlertHandleLogService
 * @date 2024/7/8 11:26
 */
public interface IAssetsAlertHandleLogService extends IService<AssetsAlertHandleLog> {
    /**
     * 处理详情新增
     * @param assetsAlertHandleLog
     */
    AjaxResult add(AssetsAlertHandleLog assetsAlertHandleLog);

    /**
     * 查询告警处理记录
     * @param alertId
     * @return 集合
     */
    List<AssetsAlertHandleLog> getAlertHandleLog(String alertId);
}
