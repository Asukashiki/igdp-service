package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.dto.AgronomicTraitRecordDTO;
import com.inspur.seed.domain.entity.AgronomicTraitRecord;
import com.inspur.seed.domain.vo.AgronomicTraitRecordVO;
import com.inspur.seed.service.IAgronomicTraitRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 农艺性状采集主记录Controller
 * 
 * @author inspur
 */
@RestController
@RequestMapping("/breeding/traitRecord")
public class AgronomicTraitRecordController extends BaseController {

    @Autowired
    private IAgronomicTraitRecordService recordService;

    /**
     * 分页查询主记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AgronomicTraitRecord record) {
        startPage();
        List<AgronomicTraitRecord> list = recordService.selectRecordList(record);
        return getDataTable(list);
    }

    /**
     * 获取主记录详情（含明细）
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam("recordId") String recordId) {
        AgronomicTraitRecordVO vo = recordService.selectRecordById(recordId);
        return AjaxResult.success(vo);
    }

    /**
     * 新增主记录（含明细）
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody AgronomicTraitRecordDTO dto) {
        String recordId = recordService.insertRecord(dto);
        return AjaxResult.success("Added successfully", recordId);
    }

    /**
     * 修改主记录（含明细）
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody AgronomicTraitRecordDTO dto) {
        return toAjax(recordService.updateRecord(dto));
    }

    /**
     * 删除主记录（级联删除明细）
     */
    @GetMapping("/remove")
    public AjaxResult remove(@RequestParam("recordIds") String recordIds) {
        String[] ids = recordIds.split(",");
        return toAjax(recordService.deleteRecordByIds(ids));
    }

    /**
     * 生成记录ID
     */
    @GetMapping("/generateId")
    public AjaxResult generateId(@RequestParam("plotId") String plotId) {
        String recordId = recordService.generateRecordId(plotId);
        return AjaxResult.success(recordId);
    }

    /**
     * 提交审核
     */
    @PostMapping("/submit/{id}")
    public AjaxResult submit(@PathVariable String id) {
        return recordService.submitAgronomicTraitAudit(id);
    }
}
