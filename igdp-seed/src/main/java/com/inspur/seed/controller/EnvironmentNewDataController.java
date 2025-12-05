package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.EnvironmentNewData;
import com.inspur.seed.service.IEnvironmentNewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 环境监测新数据Controller
 * Environment New Data Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/breeding/environmentNew")
public class EnvironmentNewDataController extends BaseController {

    @Autowired
    private IEnvironmentNewDataService environmentNewDataService;

    /**
     * 分页查询环境监测数据列表
     * Query environment new data list with pagination
     */
    @GetMapping("/list")
    public TableDataInfo list(EnvironmentNewData environmentNewData) {
        startPage();
        List<EnvironmentNewData> list = environmentNewDataService.selectEnvironmentNewDataList(environmentNewData);
        return getDataTable(list);
    }

    /**
     * 获取环境监测数据详情
     * Get environment new data detail
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam("envRecordId") String envRecordId) {
        return AjaxResult.success(environmentNewDataService.selectEnvironmentNewDataById(envRecordId));
    }

    /**
     * 新增环境监测数据
     * Add new environment data
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody EnvironmentNewData environmentNewData) {
        String envRecordId = environmentNewDataService.insertEnvironmentNewData(environmentNewData);
        return AjaxResult.success("新增成功 / Added successfully", envRecordId);
    }

    /**
     * 修改环境监测数据
     * Update environment data
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody EnvironmentNewData environmentNewData) {
        return toAjax(environmentNewDataService.updateEnvironmentNewData(environmentNewData));
    }

    /**
     * 删除环境监测数据
     * Delete environment data
     */
    @GetMapping("/remove")
    public AjaxResult remove(@RequestParam("envRecordIds") String envRecordIds) {
        String[] ids = envRecordIds.split(",");
        return toAjax(environmentNewDataService.deleteEnvironmentNewDataByIds(ids));
    }
}
