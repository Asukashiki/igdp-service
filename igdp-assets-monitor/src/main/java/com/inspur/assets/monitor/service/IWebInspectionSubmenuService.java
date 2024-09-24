package com.inspur.assets.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.assets.monitor.domain.WebAutoTestInput;
import com.inspur.assets.monitor.domain.WebInspectionSubmenu;
import com.inspur.common.core.domain.AjaxResult;

import java.util.List;

public interface IWebInspectionSubmenuService extends IService<WebInspectionSubmenu> {
    /**
     *
     * @param param 检测元素下的子序列元素
     * @return 子序列元素
     */
    List<WebInspectionSubmenu> selectWebInspectionSubmenu(WebAutoTestInput param);

    /**
     *
     * @param webInspectionSubmenu 元素信息
     */
    AjaxResult insertWebInspectionSubmenu(WebInspectionSubmenu webInspectionSubmenu);

    /**
     *
     * @param webInspectionSubmenu 更新元素信息
     */
    AjaxResult updateWebInspectionSubmenu(WebInspectionSubmenu webInspectionSubmenu);

    /**
     *
     * @param webInspectionSubmenu
     */
    AjaxResult deleteWebInspectionSubmenu(WebInspectionSubmenu webInspectionSubmenu);


    /**
     *
     * @param webAutoTestInput 删除巡检目标下的所以元素
     */
    void deleteOneElementAllOrderWebInspectionSubmenu(WebAutoTestInput webAutoTestInput);



}
