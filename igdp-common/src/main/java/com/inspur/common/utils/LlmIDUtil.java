package com.inspur.common.utils;

import cn.hutool.core.util.IdUtil;

public class LlmIDUtil {
    public static String GUID() {
        return IdUtil.fastSimpleUUID();
    }
}
