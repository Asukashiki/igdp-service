package com.inspur.data.treating.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.inspur.common.enums.BaseEnum;
import lombok.Getter;

/**
 * 统一统计类别枚举
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName StatisticsCategory
 * @date 2024/7/17 15:44
 */
@Getter
public enum StatisticsItemCategory implements BaseEnum {
    /**
     * 资产状态数量
     */
    ASSETS_STATUS_NUM("assets_status_num", "资产状态数量"),
    ASSETS_TYPE_NUM("assets_type_num", "资产类型数量"),
    ASSETS_MONITOR_NUM("assets_monitor_num", "监控状态"),
    ASSETS_ALERT_NUM("assets_alert_num", "资产告警数量"),
    ;

    @EnumValue
    private final String code;

    public final String description;

    StatisticsItemCategory(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
