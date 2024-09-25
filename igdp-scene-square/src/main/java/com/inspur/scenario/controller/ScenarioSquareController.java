package com.inspur.scenario.controller;


import com.inspur.common.core.domain.AjaxResult;
import com.inspur.scenario.domain.ScenarioSquareLLmEntity;
import com.inspur.scenario.service.IScenarioSquareService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 区域
 */
@RestController
@RequestMapping("/scenario")
public class ScenarioSquareController {
    final IScenarioSquareService scenarioSquareService;

    public ScenarioSquareController(IScenarioSquareService scenarioSquareService) {
        this.scenarioSquareService = scenarioSquareService;
    }

    /**
     * 新增/更新
     *
     * @param baseInfo
     * @return
     */
    @PostMapping("/save")
    public AjaxResult save(@RequestBody ScenarioSquareLLmEntity baseInfo) {

        if (StringUtils.isEmpty(baseInfo.getId())) {

            return scenarioSquareService.insertInfo(baseInfo);
        } else {
            return scenarioSquareService.upadateInfo(baseInfo);
        }

    }

    @GetMapping("/page")
    public AjaxResult page(ScenarioSquareLLmEntity scenario, Integer pageNum, Integer pageSize) {

        return scenarioSquareService.pageList(scenario,pageNum,pageSize);


    }
    @GetMapping("/info")
    public AjaxResult info( String id) {

        return scenarioSquareService.info(id);


    }

    /**
     * 发布
     * @param id
     * @return
     */
    @PostMapping("/publish")
    public AjaxResult publish(String id) {

        if (StringUtils.isEmpty(id)) {
            return AjaxResult.error("id 缺失");
        } else {
         return  scenarioSquareService.publish(id);
        }
    }
    /**
     * 发布
     * @param id
     * @return
     */
    @PostMapping("/cancel/publish")
    public AjaxResult cancelPublish(String id) {

        if (StringUtils.isEmpty(id)) {
            return AjaxResult.error("id 缺失");
        } else {
            return  scenarioSquareService.cancelPublish(id);
        }
    }
    /**
     * 删除
     *
     * @param
     * @return
     */
    @PostMapping("/delete")
    public AjaxResult delete(String id) {

        if (StringUtils.isEmpty(id)) {
            return AjaxResult.error("id 缺失");
        } else {
            return AjaxResult.success(scenarioSquareService.removeById(id));
        }
    }




}
