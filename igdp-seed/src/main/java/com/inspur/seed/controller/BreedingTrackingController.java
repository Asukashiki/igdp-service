package com.inspur.seed.controller;

import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.BreedingTracking;
import com.inspur.seed.service.IBreedingTrackingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 育种跟踪管理控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/rest/system/breedingTracking")
public class BreedingTrackingController extends BaseController {

    @Resource
    private IBreedingTrackingService breedingTrackingService;

    /**
     * 新增育种跟踪记录
     *
     * @param breedingTracking 育种跟踪记录信息
     * @return 新增结果
     */
    @Log(title = "育种跟踪管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BreedingTracking breedingTracking) {
        try {
            String trackingId = breedingTrackingService.addBreedingTracking(breedingTracking);
            Map<String, String> data = new HashMap<>();
            data.put("trackingId", trackingId);
            return AjaxResult.success("操作成功", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询育种跟踪记录列表
     *
     * @param batchId 育种批次ID
     * @param stageName 阶段名称
     * @param stageCompletionDateStart 阶段完成日期起始
     * @param stageCompletionDateEnd 阶段完成日期结束
     * @return 查询结果
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String batchId,
            @RequestParam(required = false) String stageName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate stageCompletionDateStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate stageCompletionDateEnd) {
        // 使用RuoYi框架提供的分页方法
        startPage();
        
        List<BreedingTracking> list = breedingTrackingService.queryBreedingTrackingList(batchId, stageName, stageCompletionDateStart, stageCompletionDateEnd);
        
        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }

    /**
     * 根据跟踪ID查询育种跟踪记录详情
     *
     * @param trackingId 跟踪ID
     * @return 查询结果
     */
    @GetMapping("/info/{trackingId}")
    public AjaxResult getInfo(@PathVariable String trackingId) {
        BreedingTracking breedingTracking = breedingTrackingService.queryByTrackingId(trackingId);
        if (breedingTracking == null) {
            return AjaxResult.error("育种跟踪记录不存在");
        }
        return AjaxResult.success("查询成功", breedingTracking);
    }

    /**
     * 编辑育种跟踪记录
     *
     * @param breedingTracking 育种跟踪记录信息
     * @return 编辑结果
     */
    @Log(title = "育种跟踪管理", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody BreedingTracking breedingTracking) {
        try {
            boolean result = breedingTrackingService.editBreedingTracking(breedingTracking);
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
     * 删除育种跟踪记录
     *
     * @param trackingId 跟踪ID
     * @return 删除结果
     */
    @Log(title = "育种跟踪管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{trackingId}")
    public AjaxResult remove(@PathVariable String trackingId) {
        try {
            boolean result = breedingTrackingService.removeBreedingTracking(trackingId);
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