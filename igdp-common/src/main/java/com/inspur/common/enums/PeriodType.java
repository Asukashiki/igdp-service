package com.inspur.common.enums;

import lombok.Getter;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName PeriodType
 * @date 2024/7/18 11:14
 */
@Getter
public enum PeriodType {
    /**
     * 每年
     * */
    YEAR("year","每年"),
    MONTH("month","每月"),
    DAY("day","每日"),
    HOUR("hour","每小时"),
    MINUTE("minute","每分钟"),
    ;

    private final String code;
    private final String name;

    PeriodType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
