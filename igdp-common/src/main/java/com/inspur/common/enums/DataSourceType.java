package com.inspur.common.enums;

/**
 * 数据源
 * 
 * @author liyunlong
 */
public enum DataSourceType
{
    /**
     * 主库
     */
    MASTER,

    /**
     * 从库
     */
    SLAVE,
    /**
     * 低代码WEB服务数据库
     * */
    IST_WEB,
    /**
     * 低代码流程服务数据库
     * */
    IST_WORKFLOW
}
