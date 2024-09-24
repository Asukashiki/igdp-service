package com.inspur.assets.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.assets.monitor.domain.WebInspectionPlan;
import com.inspur.common.core.domain.AjaxResult;

import java.util.List;
public interface IWebInspectionPlanService extends IService<WebInspectionPlan> {

    List<WebInspectionPlan> SelectWebInspectionPlan();

    AjaxResult insertWebInspectionPlan(WebInspectionPlan webInspectionPlan);

    AjaxResult updateWebInspectionPlan(WebInspectionPlan webInspectionPlan);

    AjaxResult deleteWebInspectionPlan(int jobid);

    AjaxResult updateStatus(int jobid, String status);

    List<WebInspectionPlan> QueryWebInspectionPlan(WebInspectionPlan param);
}
