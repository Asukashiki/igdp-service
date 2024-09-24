package com.inspur.common.enums;

/**
 * 枚举通用
 * @author liyunlong
 * @version 1.0
 * @ClassName CodeEnum
 * @date 2024/7/19 10:35
 */
public interface BaseEnum {
    /**
     * 获取编号
     * @return 响应枚举的自定义code
     * */
    String getCode();

    /**
     * 获取描述
     * @return 枚举自定义的描述
     * */
    String getDescription();
}
