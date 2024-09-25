package com.inspur.scenario.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.scenario.domain.ScenarioSquareEntity;

public interface IScenarioSquareService extends IService<ScenarioSquareEntity> {


    AjaxResult insertInfo(ScenarioSquareEntity baseInfo);

    AjaxResult upadateInfo(ScenarioSquareEntity baseInfo);

    AjaxResult pageList(ScenarioSquareEntity scenario,Integer pageNum, Integer pageSize);

    AjaxResult info(String id);

    AjaxResult publish(String id);

    AjaxResult cancelPublish(String id);
}
