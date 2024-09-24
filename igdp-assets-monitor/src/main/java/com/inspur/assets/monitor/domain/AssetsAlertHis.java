package com.inspur.assets.monitor.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.assets.domain.AssetsInfo;
import com.inspur.assets.monitor.domain.payload.AssetsAlertEventPayload;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 历史告警信息
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName AssetsAlertHis
 * @date 2024/7/8 10:57
 */
@TableName("assets_alert_his")
@Setter
@Getter
public class AssetsAlertHis extends BaseEntity {

    @TableId(type = IdType.INPUT)
    private String alertId;

    /**
     * 名称
     */
    private String title;

    /**
     * 告警实例对象
     * 一般是ip:port,定位具体资产信息
     */
    private String target;

    /**
     * 事件关联key
     * 拥有相同alert_key的告警事件，合并为同一个告警
     */
    private String alertKey;

    /**
     * 告警等级
     * Critical：严重，Warning：警告，Info：提醒
     */
    private String grade;

    /**
     * 告警类型：存储告警、cpu告警。。。
     * disk 硬盘、memory 内存、network 网络、cup cpu
     */
    private String alertType;

    /**
     * 告警条件
     * 例如：cpu_usage_active > 80
     */
    private String alertRule;

    /**
     * 资产类型
     * 对应资产的typeCode
     */
    private String assetsType;
    /**
     * 告警描述
     */
    private String description;

    /**
     * 告警详情
     */
    private String content;

    /**
     * 资产id
     */
    private String assetsId;

    /**
     * 资产code
     */
    private String assetsCode;

    private String assetsName;

    private String ip;

    private String port;

    /**
     * 三方系统appId
     * 对应sys_open_app信息中的appID
     */
    private String openAppId;

    private String viewLink;

    /**
     * 来源
     * 内置、普罗米修斯、动环系统
     */
    private String source;
    /**
     * 获取方式
     * push 接收推送；api 三方系统；db 数据库拉取；
     */
    private String accessType;

    /**
     * 告警产生时间
     * 接收的时间戳，转换为LocalDataTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 资产信息
     */
    private transient AssetsInfo assetsInfo;


    /**
     * 告警解除时间
     * 接收时间戳，转为LocalDateTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recoverTime;
    /**
     * 持续时长，单位秒
     */
    private Long durationSeconds;

    public AssetsAlertHis() {

    }

    public AssetsAlertHis(AssetsAlertCur cur) {
        if (null != cur) {
            this.setAlertId(cur.getAlertId());
            this.setAlertKey(cur.getAlertKey());
            this.setAssetsId(cur.getAssetsId());
            this.setAlertRule(cur.getAlertRule());
            this.setAlertType(cur.getAlertType());
            this.setAccessType(cur.getAccessType());
            this.setAssetsCode(cur.getAssetsCode());
            this.setAssetsName(cur.getAssetsName());
            this.setAssetsType(cur.getAssetsType());
            this.setContent(cur.getContent());
            this.setDescription(cur.getDescription());
            this.setGrade(cur.getGrade());
            this.setIp(cur.getIp());
            this.setOpenAppId(cur.getOpenAppId());
            this.setStartTime(cur.getStartTime());
            this.setTitle(cur.getTitle());
            this.setCreateTime(cur.getCreateTime());
            this.setUpdateTime(LocalDateTime.now());
            this.setViewLink(cur.getViewLink());
            this.setPort(cur.getPort());
            this.setSource(cur.getSource());
        }
    }

}
