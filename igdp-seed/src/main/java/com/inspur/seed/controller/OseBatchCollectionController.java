package com.inspur.seed.controller;

import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.common.utils.poi.ExcelUtil;
import com.inspur.seed.domain.OseBatchCollection;
import com.inspur.seed.service.IOseBatchCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * OSE繁殖批次信息数据采集 - Controller
 * OSE Batch Information Data Collection Controller
 *
 * @author system
 * @date 2026-01-04
 */
@RestController
@RequestMapping("/seed/ose/batch/collection")
public class OseBatchCollectionController extends BaseController {

    @Autowired
    private IOseBatchCollectionService oseBatchCollectionService;

    /**
     * 查询OSE繁殖批次采集列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OseBatchCollection oseBatchCollection) {
        startPage();
        List<OseBatchCollection> list = oseBatchCollectionService.selectOseBatchCollectionList(oseBatchCollection);
        return getDataTable(list);
    }

    /**
     * 导出OSE繁殖批次采集列表
     */
    @Log(title = "OSE繁殖批次采集", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OseBatchCollection oseBatchCollection) {
        List<OseBatchCollection> list = oseBatchCollectionService.selectOseBatchCollectionList(oseBatchCollection);
        ExcelUtil<OseBatchCollection> util = new ExcelUtil<>(OseBatchCollection.class);
        util.exportExcel(response, list, "OSE繁殖批次采集数据");
    }

    /**
     * 获取OSE繁殖批次采集详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(oseBatchCollectionService.getById(id));
    }

    /**
     * 新增OSE繁殖批次采集
     */
    @Log(title = "OSE繁殖批次采集", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody OseBatchCollection oseBatchCollection) {
        return toAjax(oseBatchCollectionService.insertOseBatchCollection(oseBatchCollection));
    }

    /**
     * 修改OSE繁殖批次采集
     */
    @Log(title = "OSE繁殖批次采集", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody OseBatchCollection oseBatchCollection) {
        return toAjax(oseBatchCollectionService.updateOseBatchCollection(oseBatchCollection));
    }

    /**
     * 删除OSE繁殖批次采集
     */
    @Log(title = "OSE繁殖批次采集", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody Long[] ids) {
        return toAjax(oseBatchCollectionService.deleteOseBatchCollectionByIds(ids));
    }
}
