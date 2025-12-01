package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.TrialBasic;
import com.inspur.seed.service.ITrialBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 试验基础信息管理Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/breeding/trial")
public class TrialBasicController extends BaseController {

    @Autowired
    private ITrialBasicService trialBasicService;

    /**
     * 分页查询试验基础信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TrialBasic trialBasic) {
        startPage();
        List<TrialBasic> list = trialBasicService.selectTrialBasicList(trialBasic);
        return getDataTable(list);
    }

    /**
     * 获取试验基础信息详情
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam("trialId") String trialId) {
        return AjaxResult.success(trialBasicService.selectTrialBasicById(trialId));
    }

    /**
     * 新增试验基础信息
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TrialBasic trialBasic) {
        String trialId = trialBasicService.insertTrialBasic(trialBasic);
        return AjaxResult.success("新增成功", trialId);
    }

    /**
     * 修改试验基础信息
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody TrialBasic trialBasic) {
        return toAjax(trialBasicService.updateTrialBasic(trialBasic));
    }

    /**
     * 删除试验基础信息
     */
    @GetMapping("/remove")
    public AjaxResult remove(@RequestParam("trialIds") String trialIds) {
        String[] ids = trialIds.split(",");
        return toAjax(trialBasicService.deleteTrialBasicByIds(ids));
    }

    /**
     * 获取试验下拉列表
     */
    @GetMapping("/options")
    public AjaxResult getOptions(@RequestParam(value = "batchId", required = false) String batchId) {
        return AjaxResult.success(trialBasicService.selectTrialOptions(batchId));
    }
}
