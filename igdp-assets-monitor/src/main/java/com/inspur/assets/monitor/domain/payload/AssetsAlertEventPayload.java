package com.inspur.assets.monitor.domain.payload;

import com.inspur.assets.monitor.enums.AssetsAlertStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 告警事件接收载体
 * @author liyunlong
 * @version 1.0
 * @ClassName AssetsAlertEventPayload
 * @date 2024/7/11 15:18
 */
@Setter
@Getter
public class AssetsAlertEventPayload {
    private String title;

    private String titleRule;

    /**
     * 告警状态
     * 参照枚举内容 严重、重要、警告、提示、未定义、恢复
     * */
    private AssetsAlertStatus eventStatus;

    /**
     * 告警key，告警发生、恢复的凭证等
     * */
    private String alertKey;

    /**
     * 告警事件类型
     * 内存使用率过高、cup使用率过高、磁盘存储占用率过高。。。。
     * */
    private String alertType;

    /**
     * 实例对象
     * 虚拟机ip；数据库ip:port；物理机ip；中间件：ip
     * */
    private String target;

    /**
     * 实例类型
     * 枚举：AssetsType
     * PHYSICAL_MACHINE（物理机）;VIRTUAL_MACHINE（虚拟机）;MIDDLEWARE（中间件）
     * DATABASE（数据库）；APPLICATION_SYSTEM（应用系统）；MACHINE_ROOM（机房）；MACHINE_CABINET（机柜）
     * */
    private String targetType;

    /**
     * 告警描述，不超过2048字符，超出后自动截断
     * */
    private String description;

    /**
     * 时间戳，精确到毫秒
     * 新的告警则是开始时间
     * 已有的告警，则为结束时间
     * */
    private long timestamp;

    /**
     * 恢复时间戳
     * */
    private long recoveredTimestamp;

    private String openAppId;

    /**
     * 标签,限制50个以内
     * 实例："resource":"171.10.40.110","check":"api latency > 500ms","ip":"10.110.149.140"
     * */
    private Map<String,String> labels;

    /**
     * 告警原始json信息
     * */
    private String eventJson;
}
