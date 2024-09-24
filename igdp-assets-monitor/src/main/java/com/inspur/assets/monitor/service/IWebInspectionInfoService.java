package com.inspur.assets.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.assets.monitor.domain.WebInspectionInfo;
import com.inspur.common.core.domain.AjaxResult;

import java.util.List;


/**
 * @author wangxinyang
 * @version 1.0
 * @ClassName IWebInspectionInfoService
 * @date 2024/7/30 10:43
 */
public interface IWebInspectionInfoService extends IService<WebInspectionInfo> {
    /**
     *
     *
     * @return 列表集合
     */
    List<WebInspectionInfo> selectWebInspectionInfoList();

    /**
     *
     * @param webInspectionInfo
     * @return 任务元素
     */
    List<WebInspectionInfo> queryWebInspectionInfoList(WebInspectionInfo webInspectionInfo);

    /**
     *
     * @param webInspectionInfo 插入的任务信息
     */
    AjaxResult insertWebInspectionInfo(WebInspectionInfo webInspectionInfo);

    /**
     *
     * @param webInspectionInfo 更新的任务信息
     */
    AjaxResult updateWebInspectionInfo(WebInspectionInfo webInspectionInfo);

    /**
     *
     * @param taskID 任务序号
     * @return
     */
    AjaxResult deleteWebInspectionInfoById(int taskID);


    AjaxResult updateTestFrequency(int taskID,String Cron);

    public AjaxResult updateStatus(int taskID,String param);
}
