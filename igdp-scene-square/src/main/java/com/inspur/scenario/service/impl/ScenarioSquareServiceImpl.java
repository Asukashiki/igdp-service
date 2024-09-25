package com.inspur.scenario.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.LlmEntityUtil;
import com.inspur.scenario.domain.ScenarioSquareLLmEntity;
import com.inspur.scenario.mapper.IScenarioSquareMapper;
import com.inspur.scenario.service.IScenarioSquareService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 场景广场
 */
@Service
@AllArgsConstructor
public class ScenarioSquareServiceImpl extends ServiceImpl<IScenarioSquareMapper, ScenarioSquareLLmEntity> implements IScenarioSquareService {
    private static Logger logger = LoggerFactory.getLogger(ScenarioSquareServiceImpl.class);


    private final IScenarioSquareMapper scenarioSquareMapper;


    @Override
    public AjaxResult insertInfo(ScenarioSquareLLmEntity baseInfo) {
        LlmEntityUtil.setDefaultValue(baseInfo);
        baseInfo.setVisibility("0");
        scenarioSquareMapper.insert(baseInfo);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult upadateInfo(ScenarioSquareLLmEntity baseInfo) {
        LlmEntityUtil.setDefaultValue(baseInfo);
        scenarioSquareMapper.updateById(baseInfo);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult pageList(ScenarioSquareLLmEntity scenario, Integer pageNum, Integer pageSize) {
        IPage<ScenarioSquareLLmEntity> scenarioPage = scenarioSquareMapper.pageList(scenario.getCategory(), scenario.getLyCategory(), scenario.getZyCategory(), pageNum, pageSize);
        return AjaxResult.success(scenarioPage);
    }

    @Override
    public AjaxResult info(String id) {
        ScenarioSquareLLmEntity scenarioSquareEntity = scenarioSquareMapper.selectById(id);
        return AjaxResult.success(scenarioSquareEntity);
    }

    @Override
    public AjaxResult publish(String id) {
        ScenarioSquareLLmEntity scenarioSquareEntity = scenarioSquareMapper.selectById(id);
        if(scenarioSquareEntity == null){
            return  AjaxResult.error("信息不存在");
        }
        scenarioSquareEntity.setVisibility("1");
        return AjaxResult.success("发布成功");
    }

    @Override
    public AjaxResult cancelPublish(String id) {
        ScenarioSquareLLmEntity scenarioSquareEntity = scenarioSquareMapper.selectById(id);
        if(scenarioSquareEntity == null){
            return  AjaxResult.error("信息不存在");
        }
        scenarioSquareEntity.setVisibility("0");
        return AjaxResult.success("取消发布成功");
    }
}
