package com.inspur.data.treating.service;

/**
 * 统一数据统计接口
 * @author liyunlong
 * @version 1.0
 * @ClassName IUnifyStatisticsService
 * @date 2024/7/17 16:18
 */
public interface IUnifyStatisticsService {
    /**
     * 统计任务处理
     * 每日统计任务
     * */
    void statisticsTaskDay();
    /**
     * 统计任务处理
     * 每分钟统计一次的任务
     * */
    void statisticsTaskMinute();
    /**
     * 统计任务
     * 每小时统计一次的任务
     * */
    void statisticsTaskHour();
    /**
     * 统计任务
     * 每周统计一次的任务
     * */
    void statisticsTaskWeek();
    /**
     * 每月统计一次的任务
     * */
    void statisticsTaskMonth();

    /**
     * 每年统计一次的任务
     * */
    void statisticsTaskYear();

}
