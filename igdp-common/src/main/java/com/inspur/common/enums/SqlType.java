package com.inspur.common.enums;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName AssetsSyncType
 * @date 2024/7/16 15:37
 */
public enum SqlType {
    /**
     * 查询
     * */
    SELECT("select"),
    /**
     * 更新
     * */
    UPDATE("update"),
    /**
     * 插入
     * */
    INSERT("insert"),
    /**
     * 删除
     * */
    DELETE("delete"),
    ;

    /**
     * sql开头
     * insert update select delete
     * */
    private final String startWith;

    SqlType(String startWith) {
        this.startWith = startWith;
    }

    public String startWith(){
        return startWith;
    }
}
