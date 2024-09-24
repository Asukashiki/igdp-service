package com.inspur.assets.monitor.service;

import cn.hutool.json.JSONObject;
import com.inspur.system.domain.SysOpenApp;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName IAssetsAlertEventService
 * @date 2024/8/29 11:42
 */
public interface IAssetsAlertEventService {
    /**
     * 告警信息推送
     * @param openAppId 开放appId
     * @param eventData 推送数据
     * */
    void pushEvent(String openAppId, JSONObject eventData);
}
