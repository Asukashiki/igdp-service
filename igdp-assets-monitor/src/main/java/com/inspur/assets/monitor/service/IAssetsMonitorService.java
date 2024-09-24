package com.inspur.assets.monitor.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.assets.domain.AssetsInfo;
import com.inspur.assets.monitor.domain.AssetsAlertCur;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author wanghailong
 * @date 2024/7/30
 */
public interface IAssetsMonitorService {
    /**
     * 资产监控概览
     *
     * @param list
     * @return
     */
    List<Map<String, Object>> getMonitoringDetails(List<AssetsInfo> list);


    /**
     * 应用拓扑图
     *
     * @return
     */
    List<Map<String, Object>> getApplication();

    /**
     * 应用监控详情
     *
     * @param
     * @return
     */
    Map<String, Object> getCur(String code);

    /**
     * 虚拟机紧急告警明细
     *
     * @param code,keyword
     * @return
     */
    List<AssetsAlertCur> getMonitoringCur(String code, String keyword);

    /**
     * 指定时间点的所有设备平均使用率
     * 将所有设备对应的指标利用率取平均值
     * cpu、内存、硬盘等使用率平均值
     *
     * @param label     指标内容：cpu使用率：cpu_usage_active；内存使用率：mem_used_percent
     * @param identList 主机ip列表
     * @param dateTime 时间点
     * @return 结果内容：平均利用率，环比
     */
    JSONObject getAvgByAll(String label, List<String> identList, LocalDateTime dateTime);

    /**
     * 获取指定时间区间的最大峰值平均利用率以及最大峰值
     * @param label 指标
     * @param identList 实列列表
     * @param step 监控指标取值间隔，单位位秒
     * @param startTime 开始时间
     * @param endTime 截止时间
     * @return 结果：最大峰值以及环比/平均最大峰值以及环比/最大峰值的ident
     * */
    JSONObject getMaxAvgByAll(String label, List<String> identList,Integer step, LocalDateTime startTime,LocalDateTime endTime);

    /**
     * 使用率曲线图数据
     * @param label 指标 cpu使用率：cpu_usage_active；内存使用率：mem_used_percent；磁盘利用率：disk_used_percent
     * @param identList 主机列表，ip地址列表
     * @param step 数据间隔，单位秒，例如600，则每隔10分钟取一次值返回列表
     * @param startTime 开始时间，为空则默认当天0点
     * @param endTime   截止时间，为空则默认当前时间
     * @return 列表
     */
    List<JSONObject> getRangeByIdent(String label,List<String> identList,Integer step, LocalDateTime startTime, LocalDateTime endTime);

}
