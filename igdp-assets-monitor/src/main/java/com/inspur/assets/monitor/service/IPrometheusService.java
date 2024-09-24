package com.inspur.assets.monitor.service;

import cn.hutool.json.JSONObject;
import com.inspur.assets.monitor.domain.payload.PrometheusQueryPayload;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * 请求普罗米修斯接口数据
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName IN9eService
 * @date 2024/9/19 15:06
 */
public interface IPrometheusService {

    /**
     * 即时查询接口
     *
     * @param payload 查询条件
     * @return 查询结果jsonObject
     */
    JSONObject query(PrometheusQueryPayload payload);

    /**
     * 范围区间查询
     *
     * @param payload 查询条件
     * @return 查询结果jsonObject
     */
    JSONObject queryRange(PrometheusQueryPayload payload);

    /**
     * 取所有设备平均值
     * 获取所有设备的指定时间的数据，然后取平均值
     * @param label 指标内容：cpu使用率：cpu_usage_active；内存使用率：mem_used_percent
     * @param identList 指定实例列表，为空则查询所有
     * @param dateTime 时间点
     * @return 计算结果
     */
    BigDecimal getAllAvg(String label, List<String> identList, LocalDateTime dateTime);


    /**
     * 获取指定指标的指定时间区间内的最大峰值
     * startTime 为空，则查当天
     * @param label 指标 cpu使用率：cpu_usage_active；内存使用率：mem_used_percent
     * @param identList 服务器实例列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param step 取数间隔，单位为秒，默认60秒
     * @return 统计结果：区间内最大值、平均最大值、对应的环比（取昨日同时段对应的值进行环比计算）
     * */
    JSONObject getMaxMinAvg(String label, List<String> identList, LocalDateTime startTime,LocalDateTime endTime,Integer step);


    /**
     * 获取指定时间范围内的指标采集数据列表
     * @param label 指标名
     * @param identList 实列列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param step 时间间隔
     * @return 统计列表
     * */
    List<JSONObject> getRangeByIdent(String label, List<String> identList, LocalDateTime startTime,LocalDateTime endTime,Integer step);
}
