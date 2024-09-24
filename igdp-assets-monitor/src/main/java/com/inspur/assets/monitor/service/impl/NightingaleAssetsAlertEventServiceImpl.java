package com.inspur.assets.monitor.service.impl;

import cn.hutool.json.JSONObject;
import com.inspur.assets.enums.AssetsTypeEnums;
import com.inspur.assets.monitor.domain.payload.AssetsAlertEventPayload;
import com.inspur.assets.monitor.enums.AssetsAlertStatus;
import com.inspur.assets.monitor.service.IAssetsAlertEventService;
import com.inspur.assets.monitor.service.IAssetsAlertService;
import com.inspur.common.utils.EnumUtil;
import com.inspur.common.utils.StringUtils;
import com.inspur.system.domain.SysOpenApp;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 夜莺告警推送处理
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName NightingaleAssetsAlertEventServiceImpl
 * @date 2024/8/29 14:52
 */
@Service("nightingaleAssetsAlertEventService")
public class NightingaleAssetsAlertEventServiceImpl implements IAssetsAlertEventService {
    @Resource
    private IAssetsAlertService assetsAlertService;

    @Override
    public void pushEvent(String openAppId, JSONObject eventData) {
        AssetsAlertEventPayload eventPayload = new AssetsAlertEventPayload();
        eventPayload.setOpenAppId(openAppId);
        eventPayload.setAlertKey(eventData.get("id").toString());
        JSONObject tagMap = eventData.getJSONObject("tags_map");
        eventPayload.setTarget(eventData.getStr("target_ident"));
        String typeCode = tagMap.getStr("typeCode");
        if (StringUtils.isEmpty(typeCode)) {
            String groupName = eventData.getStr("group_name");
            AssetsTypeEnums typeEnums = EnumUtil.getByDescription(groupName, AssetsTypeEnums.class);
            if (null != typeEnums) {
                typeCode = typeEnums.getCode();
            }
        }
        eventPayload.setTargetType(typeCode);
        AssetsAlertStatus status;
        boolean isRecovered = eventData.getBool("is_recovered");
        if (isRecovered) {
            status = AssetsAlertStatus.Ok;
        } else {
            int grade = eventData.getInt("severity");
            //夜莺的1级>2级>3级
            if (grade == 1) {
                status = AssetsAlertStatus.Critical;
            } else if (grade == 2) {
                status = AssetsAlertStatus.Major;
            } else if (grade == 3) {
                status = AssetsAlertStatus.Minor;
            } else {
                status = AssetsAlertStatus.Warning;
            }
        }
        eventPayload.setEventStatus(status);
        eventPayload.setAlertType(eventData.getStr("rule_name"));
        eventPayload.setTitle(eventData.getStr("rule_name") + "-机器：" + eventData.getStr("target_ident"));
        eventPayload.setDescription(eventData.getStr("rule_name"));
        eventPayload.setTimestamp(eventData.getLong("first_trigger_time") * 1000);
        eventPayload.setRecoveredTimestamp(eventData.getLong("last_eval_time") * 1000);
        eventPayload.setEventJson(eventData.toString());
        assetsAlertService.pushAssertAlert(eventPayload);
    }
}
