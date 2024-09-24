package com.inspur.assets.monitor.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.domain.AssetsInfo;
import com.inspur.assets.enums.AssetsTypeEnums;
import com.inspur.assets.mapper.AssetsInfoMapper;
import com.inspur.assets.monitor.domain.AssetsAlertCur;
import com.inspur.assets.monitor.domain.AssetsAlertHis;
import com.inspur.assets.monitor.domain.payload.AssetsAlertEventPayload;
import com.inspur.assets.monitor.enums.AssetsAlertStatus;
import com.inspur.assets.monitor.mapper.AssetsAlertCurMapper;
import com.inspur.assets.monitor.mapper.AssetsAlertHisMapper;
import com.inspur.assets.monitor.service.IAssetsAlertHisService;
import com.inspur.assets.monitor.service.IAssetsAlertService;
import com.inspur.assets.service.IAssetsService;
import com.inspur.common.constant.HttpStatus;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.EnumUtil;
import com.inspur.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static javax.swing.JSplitPane.LEFT;
import static javax.swing.JSplitPane.RIGHT;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName AssetsAlertServiceImpl
 * @date 2024/7/15 19:54
 */
@Slf4j
@Service("assetsAlertService")
public class AssetsAlertServiceImpl extends ServiceImpl<AssetsAlertCurMapper, AssetsAlertCur> implements IAssetsAlertService {
    @Resource
    private AssetsInfoMapper assetsInfoMapper;
    @Resource
    private IAssetsService assetsService;
    @Resource
    private AssetsAlertCurMapper assetsAlertCurMapper;
    @Resource
    private AssetsAlertHisMapper assetsAlertHisMapper;
    @Resource
    private IAssetsAlertHisService assetsAlertHisService;

    /**
     * 接收统一的告警信息，处理信息内容并保存到活跃告警中或者历史告警中
     *
     * @param eventPayload 告警通知载体内容
     */
    @Override
    public AjaxResult pushAssertAlert(AssetsAlertEventPayload eventPayload) {
        AjaxResult result = checkPayload(eventPayload);
        if (!result.isSuccess()) {
            log.error("告警信息推送异常：{}", result.get(AjaxResult.MSG_TAG));
            return result;
        }
        AssetsAlertCur currentAlert = assetsAlertCurMapper.selectOne(new LambdaQueryWrapper<AssetsAlertCur>().eq(AssetsAlertCur::getAlertKey, eventPayload.getAlertKey()));
        //恢复
        if (eventPayload.getEventStatus().getCode().equals(AssetsAlertStatus.Ok.getCode())) {

            if (null != currentAlert) {
                AssetsAlertHis alertHis = new AssetsAlertHis(currentAlert);
                alertHis.setRecoverTime(LocalDateTimeUtil.of(eventPayload.getRecoveredTimestamp()));
                alertHis.setDurationSeconds(LocalDateTimeUtil.between(alertHis.getStartTime(), alertHis.getRecoverTime(), ChronoUnit.SECONDS));
                int insertResult = assetsAlertHisMapper.insert(alertHis);
                //删除活跃告警信息
                if (insertResult > 0) {
                    assetsAlertCurMapper.deleteById(currentAlert.getAlertId());
                }
            }
        } else {
            if (null != currentAlert) {
                if (!currentAlert.getGrade().equals(eventPayload.getEventStatus().getCode())) {
                    currentAlert.setGrade(eventPayload.getEventStatus().getCode());
                    currentAlert.setUpdateTime(LocalDateTime.now());
                    assetsAlertCurMapper.updateById(currentAlert);
                }
            } else {
                AssetsAlertCur alertCur = new AssetsAlertCur();
                alertCur.setCreateTime(LocalDateTime.now());
                Instant instant = Instant.ofEpochMilli(eventPayload.getTimestamp());
                alertCur.setStartTime(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                alertCur.setAlertKey(eventPayload.getAlertKey());
                alertCur.setAlertType(eventPayload.getAlertType());
                alertCur.setDescription(eventPayload.getDescription());
                alertCur.setTarget(eventPayload.getTarget());
                //获取资产信息
                LambdaQueryWrapper<AssetsInfo> queryWrapper = new LambdaQueryWrapper<>();
                Map<String, String> labels = eventPayload.getLabels();
                //处理标题title
                if (StringUtils.isNotEmpty(eventPayload.getTitle())) {
                    alertCur.setTitle(eventPayload.getTitle());
                } else {
                    if (StringUtils.isNotEmpty(eventPayload.getTitleRule())) {
                        alertCur.setTitle(handleTitle(eventPayload.getTitleRule(), labels));
                    }
                }
                if (null != labels) {
                    alertCur.setAlertRule(labels.get("rule"));
                    String ip = labels.get("ip");
                    String code = labels.get("code");
                    alertCur.setIp(ip);
                    alertCur.setPort(labels.get("port"));
                    queryWrapper.eq(StringUtils.isNotEmpty(ip), AssetsInfo::getIp, ip);
                    queryWrapper.eq(StringUtils.isNotEmpty(code), AssetsInfo::getCode, code);
                }
                queryWrapper.eq(AssetsInfo::getCode, eventPayload.getTarget()).or().eq(AssetsInfo::getIp, eventPayload.getTarget());
                AssetsTypeEnums targetType = EnumUtil.getByCode(eventPayload.getTargetType(), AssetsTypeEnums.class);
                if (null != targetType) {
                    queryWrapper.eq(AssetsInfo::getTypeCode, eventPayload.getTargetType());
                }
                AssetsInfo assetsInfo = assetsInfoMapper.selectOne(queryWrapper);
                if (null != assetsInfo) {
                    alertCur.setAssetsId(assetsInfo.getId());
                    alertCur.setAssetsName(assetsInfo.getName());
                    alertCur.setAssetsType(assetsInfo.getTypeCode());
                    alertCur.setAssetsCode(assetsInfo.getCode());
                    alertCur.setGrade(eventPayload.getEventStatus().getCode());
                    alertCur.setOpenAppId(eventPayload.getOpenAppId());
                }
                assetsAlertCurMapper.insert(alertCur);
            }
        }
        return AjaxResult.success("告警信息推送成功");
    }

    /**
     * 处理批量推送的预警信息
     * @param alarmList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult batchPushAssertAlert(List<AssetsAlertEventPayload> alarmList) {
        //数据校验
        AjaxResult result = checkPayloads(alarmList);
        if (!result.isSuccess()) {
            log.error("告警信息推送异常：{}", result.get(AjaxResult.MSG_TAG));
            return result;
        }
        List<AssetsAlertHis> alertHisList = new ArrayList<>();
        List<String> delAlertIdList = new ArrayList<>();
        List<AssetsAlertCur> alertCurList = new ArrayList<>();
        //基础数据获取
        Map<String,AssetsAlertCur> curAlarmMap = new HashMap<>();
        List<String> alertKeys = alarmList.stream().map(AssetsAlertEventPayload::getAlertKey).collect(Collectors.toList());
        List<AssetsAlertCur> curAlarmList = this.list(new LambdaQueryWrapper<AssetsAlertCur>().in(AssetsAlertCur::getAlertKey, alertKeys));
        if(CollectionUtil.isNotEmpty(curAlarmList)){
            curAlarmMap = curAlarmList.stream().collect(Collectors.toMap(AssetsAlertCur::getAlertKey, Function.identity()));
        }

        List<String> alertIps = alarmList.stream().map(AssetsAlertEventPayload::getTarget).collect(Collectors.toList());
        List<AssetsInfo> assetList = assetsService.list(new LambdaQueryWrapper<AssetsInfo>().in(AssetsInfo::getIp, alertIps).or().in(AssetsInfo::getCode, alertIps));

        //处理预警恢复数据
        List<AssetsAlertEventPayload> alarmOkList = alarmList.stream().filter(item -> item.getEventStatus().getCode().equals(AssetsAlertStatus.Ok.getCode())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(alarmOkList)) {
            for (AssetsAlertEventPayload alarm : alarmOkList) {
                if(curAlarmMap.containsKey(alarm.getAlertKey())){
                    AssetsAlertCur currentAlert = curAlarmMap.get(alarm.getAlertKey());
                    //加历史 删当前
                    AssetsAlertHis alertHis = new AssetsAlertHis(currentAlert);
                    alertHis.setRecoverTime(LocalDateTimeUtil.of(alarm.getRecoveredTimestamp()));
                    alertHis.setDurationSeconds(LocalDateTimeUtil.between(alertHis.getStartTime(), alertHis.getRecoverTime(), ChronoUnit.SECONDS));
                    alertHisList.add(alertHis);
                    delAlertIdList.add(currentAlert.getAlertId());
                }
            }
        }
        //处理普通预警数据 1.预警级别变更 2.新预警
        List<AssetsAlertEventPayload> newAlarmList = alarmList.stream().filter(item->!item.getEventStatus().getCode().equals(AssetsAlertStatus.Ok.getCode())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(newAlarmList)) {
            for (AssetsAlertEventPayload alarm : newAlarmList) {
                if(curAlarmMap.containsKey(alarm.getAlertKey())){
                    AssetsAlertCur currentAlert = curAlarmMap.get(alarm.getAlertKey());
                    if (!currentAlert.getGrade().equals(alarm.getEventStatus().getCode())) {
                        currentAlert.setGrade(alarm.getEventStatus().getCode());
                        currentAlert.setUpdateTime(LocalDateTime.now());
                        alertCurList.add(currentAlert);
                    }
                }else{
                    AssetsAlertCur alertCur = new AssetsAlertCur();
                    alertCur.setCreateTime(LocalDateTime.now());
                    Instant instant = Instant.ofEpochMilli(alarm.getTimestamp());
                    alertCur.setStartTime(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                    alertCur.setAlertKey(alarm.getAlertKey());
                    alertCur.setAlertType(alarm.getAlertType());
                    alertCur.setDescription(alarm.getDescription());
                    alertCur.setTarget(alarm.getTarget());

                    Map<String, String> labels = alarm.getLabels();
                    //处理标题title
                    if (StringUtils.isNotEmpty(alarm.getTitle())) {
                        alertCur.setTitle(alarm.getTitle());
                    } else {
                        if (StringUtils.isNotEmpty(alarm.getTitleRule())) {
                            alertCur.setTitle(handleTitle(alarm.getTitleRule(), labels));
                        }
                    }

                    //获取资产信息
                    List<AssetsInfo> currentAssetList = assetList.stream().filter(item->item.getIp().equals(alarm.getTarget())||item.getCode().equals(alarm.getTarget())).collect(Collectors.toList());
                    if (null != labels) {
                        alertCur.setAlertRule(labels.get("rule"));
                        String ip = labels.get("ip");
                        String code = labels.get("code");
                        alertCur.setIp(ip);
                        alertCur.setPort(labels.get("port"));
                        if(StrUtil.isNotEmpty(ip)){
                            currentAssetList = currentAssetList.stream().filter(item->item.getIp().equals(ip)).collect(Collectors.toList());
                        }
                        if(StrUtil.isNotEmpty(code)){
                            currentAssetList = currentAssetList.stream().filter(item->item.getCode().equals(code)).collect(Collectors.toList());
                        }
                    }
                    AssetsTypeEnums targetType = EnumUtil.getByCode(alarm.getTargetType(), AssetsTypeEnums.class);
                    if (null != targetType) {
                        currentAssetList = currentAssetList.stream().filter(item->item.getTypeCode().equals(alarm.getTargetType())).collect(Collectors.toList());
                    }

                    if (CollectionUtil.isNotEmpty(currentAssetList)) {
                        AssetsInfo assetsInfo = currentAssetList.get(0);
                        alertCur.setAssetsId(assetsInfo.getId());
                        alertCur.setAssetsName(assetsInfo.getName());
                        alertCur.setAssetsType(assetsInfo.getTypeCode());
                        alertCur.setAssetsCode(assetsInfo.getCode());
                        alertCur.setGrade(alarm.getEventStatus().getCode());
                        alertCur.setOpenAppId(alarm.getOpenAppId());
                    }
                    alertCurList.add(alertCur);
                }
            }
        }

        //数据保存
        if(CollectionUtil.isNotEmpty(alertHisList)){
            assetsAlertHisService.saveBatch(alertHisList);
        }
        if(CollectionUtil.isNotEmpty(delAlertIdList)){
            assetsAlertHisService.removeByIds(delAlertIdList);
        }
        if(CollectionUtil.isNotEmpty(alertCurList)){
            this.saveOrUpdateBatch(alertCurList);
        }
        return AjaxResult.success();
    }

    private AjaxResult checkPayload(AssetsAlertEventPayload payload) {
        if (payload.getEventStatus() == null) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "告警状态不能为空");
        }
        if (StringUtils.isEmpty(payload.getAlertKey())) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "告警key不能为空");
        }
        if (!payload.getEventStatus().getCode().equals(AssetsAlertStatus.Ok.getCode())) {
            //判断必填项
            if (StringUtils.isEmpty(payload.getTitle()) && StringUtils.isEmpty(payload.getTitleRule())) {
                return AjaxResult.error(HttpStatus.BAD_REQUEST, "告警标题不能为空");
            }
            if (StringUtils.isEmpty(payload.getTarget())) {
                return AjaxResult.error(HttpStatus.BAD_REQUEST, "告警实例不能为空");
            }
            if (null == payload.getTargetType()) {
                return AjaxResult.error(HttpStatus.BAD_REQUEST, "实例类型不能为空");
            }
        }
        return AjaxResult.success();
    }

    /**
     * 校验预警集合(同单条校验逻辑)
     * @param payloads
     * @return
     */
    private AjaxResult checkPayloads(List<AssetsAlertEventPayload> payloads) {
        if (CollectionUtil.isEmpty(payloads)) {
            return AjaxResult.error("预警集合不能为空");
        }
        if (payloads.stream().anyMatch(item -> (item.getEventStatus() == null || StringUtils.isEmpty(item.getAlertKey())))) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "告警状态及告警key不能为空");
        }
        List<AssetsAlertEventPayload> newAlarmList = payloads.stream().filter(item -> !item.getEventStatus().getCode().equals(AssetsAlertStatus.Ok.getCode())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(newAlarmList)) {
            if (newAlarmList.stream().anyMatch(item -> (StringUtils.isEmpty(item.getTitle()) && StringUtils.isEmpty(item.getTitleRule())))) {
                return AjaxResult.error(HttpStatus.BAD_REQUEST, "告警标题不能为空");
            }
            if (newAlarmList.stream().anyMatch(item -> StringUtils.isEmpty(item.getTarget()))) {
                return AjaxResult.error(HttpStatus.BAD_REQUEST, "告警实例不能为空");
            }
//            if (newAlarmList.stream().anyMatch(item -> null == item.getTargetType())) {
//                return AjaxResult.error(HttpStatus.BAD_REQUEST, "实例类型不能为空");
//            }
        }
        return AjaxResult.success();
    }

    private String handleTitle(String titleRule, Map<String, String> labels) {
        String pattern = "(\\$\\{(.+?)})";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(titleRule);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String key = m.group();
            if (key != null && key.length() > 3) {
                String keyName = key.replace("${", LEFT).replace("}", RIGHT);
                if (StringUtils.isNotEmpty(keyName)) {
                    sb.append(labels.get(keyName));
                }
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    @Override
    public List<AssetsAlertCur> getAlertsCurList() {
        return Collections.emptyList();
    }
}
