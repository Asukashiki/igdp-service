package com.inspur.scenario.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.scenario.domain.ScenarioSquareLLmEntity;

public interface IScenarioSquareService extends IService<ScenarioSquareLLmEntity> {


    AjaxResult insertInfo(ScenarioSquareLLmEntity baseInfo);

    AjaxResult upadateInfo(ScenarioSquareLLmEntity baseInfo);

    AjaxResult pageList(ScenarioSquareLLmEntity scenario, Integer pageNum, Integer pageSize);

    AjaxResult info(String id);

    AjaxResult publish(String id);

    AjaxResult cancelPublish(String id);
}
