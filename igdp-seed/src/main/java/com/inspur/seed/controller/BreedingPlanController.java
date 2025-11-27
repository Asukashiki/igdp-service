package com.inspur.seed.controller;

import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.BreedingPlan;
import com.inspur.seed.service.IBreedingPlanService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 育种计划管理控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/rest/system/breedingPlan")
public class BreedingPlanController extends BaseController {

    @Resource
    private IBreedingPlanService breedingPlanService;

    /**
     * 新增育种计划
     *
     * @param breedingPlan 育种计划信息
     * @return 新增结果
     */
    @Log(title = "育种计划管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BreedingPlan breedingPlan) {
        try {
            String planId = breedingPlanService.addBreedingPlan(breedingPlan);
            return AjaxResult.success("操作成功");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询育种计划列表
     *
     * @param enterpriseId 企业ID
     * @param breedingYear 育种年度
     * @param cropType 作物类型
     * @return 查询结果
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) Integer breedingYear,
            @RequestParam(required = false) String cropType) {
        // 使用RuoYi框架提供的分页方法
        startPage();
        
        List<BreedingPlan> list = breedingPlanService.queryBreedingPlanList(enterpriseId, breedingYear, cropType);
        
        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }

    /**
     * 根据计划ID查询育种计划详情
     *
     * @param planId 计划ID
     * @return 查询结果
     */
    @GetMapping("/info/{planId}")
    public AjaxResult getInfo(@PathVariable String planId) {
        BreedingPlan breedingPlan = breedingPlanService.queryByPlanId(planId);
        if (breedingPlan == null) {
            return AjaxResult.error("育种计划不存在");
        }
        return AjaxResult.success("查询成功", breedingPlan);
    }

    /**
     * 编辑育种计划
     *
     * @param breedingPlan 育种计划信息
     * @return 编辑结果
     */
    @Log(title = "育种计划管理", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody BreedingPlan breedingPlan) {
        try {
            boolean result = breedingPlanService.editBreedingPlan(breedingPlan);
            if (result) {
                return AjaxResult.success("操作成功");
            } else {
                return AjaxResult.error("操作失败");
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除育种计划
     *
     * @param planId 计划ID
     * @return 删除结果
     */
    @Log(title = "育种计划管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{planId}")
    public AjaxResult remove(@PathVariable String planId) {
        try {
            boolean result = breedingPlanService.removeBreedingPlan(planId);
            if (result) {
                return AjaxResult.success("操作成功");
            } else {
                return AjaxResult.error("操作失败");
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}