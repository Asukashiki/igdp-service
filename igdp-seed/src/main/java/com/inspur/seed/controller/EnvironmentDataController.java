package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.EnvironmentData;
import com.inspur.seed.service.IEnvironmentDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 环境属性数据Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/breeding/environment")
public class EnvironmentDataController extends BaseController {

    @Autowired
    private IEnvironmentDataService environmentDataService;

    /**
     * 分页查询环境属性数据列表
     */
    @GetMapping("/list")
    public TableDataInfo list(EnvironmentData environmentData) {
        startPage();
        List<EnvironmentData> list = environmentDataService.selectEnvironmentDataList(environmentData);
        return getDataTable(list);
    }

    /**
     * 获取环境数据详情
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam("envId") String envId) {
        return AjaxResult.success(environmentDataService.selectEnvironmentDataById(envId));
    }

    /**
     * 新增环境属性数据
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody EnvironmentData environmentData) {
        String envId = environmentDataService.insertEnvironmentData(environmentData);
        return AjaxResult.success("新增成功", envId);
    }

    /**
     * 修改环境属性数据
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody EnvironmentData environmentData) {
        return toAjax(environmentDataService.updateEnvironmentData(environmentData));
    }

    /**
     * 删除环境属性数据
     */
    @GetMapping("/remove")
    public AjaxResult remove(@RequestParam("envIds") String envIds) {
        String[] ids = envIds.split(",");
        return toAjax(environmentDataService.deleteEnvironmentDataByIds(ids));
    }
}
