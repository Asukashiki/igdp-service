package com.inspur.assets.monitor.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.assets.monitor.domain.AssetsAlertCur;

import java.util.List;
import java.util.Map;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName IAssetsAlertCurService
 * @date 2024/7/8 11:21
 */
public interface IAssetsAlertCurService extends IService<AssetsAlertCur> {
    /**
     * 查询当前报警信息列表
     * @param param 查询条件
     * @return 列表集合
     * */
    List<AssetsAlertCur> getList(AssetsAlertCur param);
    /**
     * 保存告警信息
     * 重复数据不保存:通过target以及资产类型进行唯一资产确定
     * 异步同步资产信息到告警信息中
     * @param assetsAlertCur 告警信息
     * */
    void saveAssetsAlert(AssetsAlertCur assetsAlertCur);
    /**
     * 更新告警信息解除
     * 根据target、告警类型、资产类型进行告警解除，同时同步到历史告警中
     * @param assetsAlertCur 告警信息
     * */
    void reliveAlert(AssetsAlertCur assetsAlertCur);

    /**
     * 根据应用系统编号查询告警
     * @param param
     * @return 集合列表
     */
    List<AssetsAlertCur> getWarnList(AssetsAlertCur param);
    /**
     * 查询历史报警信息列表
     * @param param 查询条件
     * @return 列表集合
     * */
    List<AssetsAlertCur> getListHistory(AssetsAlertCur param);
    /**
     * 查询历史报警信息曲线图
     * @param param 查询条件
     * @return 列表集合
     * */
    List<Map<String, Object>> getListDiagram(AssetsAlertCur param);

    /**
     * 查询历史报警信息列表图
     * @param param 查询条件
     * @return 列表集合
     * */
    List<Map<String, Object>> getListChart(AssetsAlertCur param);

    /**
     * 查询设备告警信息柱状图
     * @param param 查询条件
     * @return 列表集合
     * */
    List<Map<String, Object>> getListBarchart(AssetsAlertCur param);

    /**
     * 告警大屏统计告警等级实践分布
     * @return json
     */
    JSONObject statisticsWithStatus();
}
