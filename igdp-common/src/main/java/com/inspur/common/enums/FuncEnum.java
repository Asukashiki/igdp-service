package com.inspur.common.enums;

import com.github.yulichang.wrapper.enums.BaseFuncEnum;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName FuncEnum
 * @date 2024/7/30 10:13
 */
public enum FuncEnum implements BaseFuncEnum {

    /**
     * 空值替换
     *
     */
    COALESCE("COALESCE(%s,''");

    private final String sql;
    FuncEnum(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return this.sql;
    }
}
