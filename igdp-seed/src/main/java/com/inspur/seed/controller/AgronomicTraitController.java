package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.AgronomicTrait;
import com.inspur.seed.service.IAgronomicTraitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 农艺性状数据Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/breeding/trait")
public class AgronomicTraitController extends BaseController {

    @Autowired
    private IAgronomicTraitService agronomicTraitService;

    /**
     * 分页查询农艺性状数据列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AgronomicTrait agronomicTrait) {
        startPage();
        List<AgronomicTrait> list = agronomicTraitService.selectAgronomicTraitList(agronomicTrait);
        return getDataTable(list);
    }

    /**
     * 获取农艺性状数据详情
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam("traitId") String traitId) {
        return AjaxResult.success(agronomicTraitService.selectAgronomicTraitById(traitId));
    }

    /**
     * 新增农艺性状数据
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody AgronomicTrait agronomicTrait) {
        String traitId = agronomicTraitService.insertAgronomicTrait(agronomicTrait);
        return AjaxResult.success("Added successfully", traitId);
    }

    /**
     * 修改农艺性状数据
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody AgronomicTrait agronomicTrait) {
        return toAjax(agronomicTraitService.updateAgronomicTrait(agronomicTrait));
    }

    /**
     * 删除农艺性状数据
     */
    @GetMapping("/remove")
    public AjaxResult remove(@RequestParam("traitIds") String traitIds) {
        String[] ids = traitIds.split(",");
        return toAjax(agronomicTraitService.deleteAgronomicTraitByIds(ids));
    }

    /**
     * 提交审核
     */
    @PostMapping("/submit/{id}")
    public AjaxResult submit(@PathVariable String id) {
        int affectedRows = agronomicTraitService.submitAgronomicTraitAudit(id);
        if (affectedRows > 0) {
            // 提交审核成功
            return AjaxResult.success("农艺性状提交审核成功");
        } else {
            // 提交审核失败（如id不存在、状态异常等）
            return AjaxResult.error("农艺性状提交审核失败");
        }
    }
}
