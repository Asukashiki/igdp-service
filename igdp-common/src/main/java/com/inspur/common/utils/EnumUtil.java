package com.inspur.common.utils;

import com.inspur.common.enums.BaseEnum;

/**
 * 枚举工具类
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName EnumUtil
 * @date 2024/7/19 10:36
 */
public class EnumUtil {
    public static <T extends BaseEnum> T getByCode(String code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }

    public static <T extends BaseEnum> T getByDescription(String description, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (description.equals(each.getDescription())) {
                return each;
            }
        }
        return null;
    }
}

