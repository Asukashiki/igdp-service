package com.inspur.assets.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.assets.monitor.domain.WebAutoTestInput;
import com.inspur.assets.monitor.domain.WebInspectionInfo;
import com.inspur.common.core.domain.AjaxResult;

import java.util.List;
public interface IWebAutoTestInputService extends IService<WebAutoTestInput> {

    /**
     *
     *
     * @return 该任务下巡检元素内容
     */
    List<WebAutoTestInput> selectWebAutoTestInput();

    /**
     *
     * @param webAutoTestInput 插入的元素信息
     */
    AjaxResult insertWebAutoTestInput(WebAutoTestInput[] webAutoTestInput);

    /**
     *
     * @param webAutoTestInput 更新的元素信息
     */
    AjaxResult updateWebAutoTestInput(WebAutoTestInput[] webAutoTestInput);

    /**
     *
     * @param id 所要删除的指定的元素信息
     */
    AjaxResult deleteWebAutoTestInput(int[] id);

    /**
     *
     * @param taskID 所以删除的指定任务的全部元素
     */
    Boolean deleteOneTaskAllWebAutoTestInput(int taskID);


    List<WebAutoTestInput> queryTargetTestInput(int taskID);



}
