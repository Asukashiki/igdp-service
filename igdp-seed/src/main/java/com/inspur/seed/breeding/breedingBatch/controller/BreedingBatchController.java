package com.inspur.seed.breeding.breedingBatch.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.breeding.breedingBatch.domain.entity.BreedingBatch;
import com.inspur.seed.domain.dto.BreedingBatchDTO;
import com.inspur.seed.breeding.breedingBatch.service.IBreedingBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 育种批次管理Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/breeding/batch")
public class BreedingBatchController extends BaseController {

    @Autowired
    private IBreedingBatchService breedingBatchService;

    /**
     * 分页查询育种批次列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BreedingBatch breedingBatch) {
        startPage();
        List<BreedingBatch> list = breedingBatchService.selectBreedingBatchList(breedingBatch);
        return getDataTable(list);
    }

    /**
     * 分页查询育种批次列表
     */
    @GetMapping("/voidedList")
    public TableDataInfo voidedList(BreedingBatch breedingBatch) {
        startPage();
        List<BreedingBatch> list = breedingBatchService.selectBreedingBatchVoidedList(breedingBatch);
        return getDataTable(list);
    }

    /**
     * 获取育种批次详细信息
     */
    @GetMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestParam("dataId") String dataId) {
        return AjaxResult.success(breedingBatchService.selectBreedingBatchById(dataId));
    }

    /**
     * 新增育种批次
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BreedingBatch breedingBatch) {
        String dataId = breedingBatchService.insertBreedingBatch(breedingBatch);
        return AjaxResult.success("新增成功", dataId);
    }

    /**
     * 修改育种批次
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BreedingBatch breedingBatch) {
        return toAjax(breedingBatchService.updateBreedingBatch(breedingBatch));
    }

    /**
     * 删除育种批次
     */
    @GetMapping("/remove")
    public AjaxResult remove(@RequestParam("dataIds") String dataIds) {
        String[] ids = dataIds.split(",");
        return toAjax(breedingBatchService.deleteBreedingBatchByIds(ids));
    }

    /**
     * 获取育种批次下拉列表
     */
    @GetMapping("/options")
    public AjaxResult getOptions() {
        return AjaxResult.success(breedingBatchService.selectBatchOptions());
    }

    /**
     * 提交审核
     */
    @PostMapping("/submitAudit")
    public AjaxResult submitAudit(@RequestBody BreedingBatch breedingBatch) {
        return toAjax(breedingBatchService.submitAudit(breedingBatch.getDataId()));
    }

    /**
     * 审核通过
     */
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody BreedingBatchDTO breedingBatchDTO) {
        return toAjax(breedingBatchService.approve(breedingBatchDTO));
    }

    /**
     * 审核驳回
     */
    @PostMapping("/reject")
    public AjaxResult reject(@RequestBody BreedingBatchDTO breedingBatchDTO) {
        return toAjax(breedingBatchService.reject(breedingBatchDTO));
    }

    /**
     * 归档
     */
    @PostMapping("/archive")
    public AjaxResult archive(@RequestBody BreedingBatch breedingBatch) {
        return toAjax(breedingBatchService.archive(breedingBatch.getDataId()));
    }

    /**
     * 作废
     */
    @PostMapping("/cancel")
    public AjaxResult cancel(@RequestBody BreedingBatch breedingBatch) {
        return toAjax(breedingBatchService.cancel(breedingBatch.getDataId()));
    }
}
