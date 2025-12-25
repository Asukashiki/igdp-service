package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.dto.LaboratoryTestDataDTO;
import com.inspur.seed.domain.vo.LaboratoryTestDataVO;
import com.inspur.seed.service.ILaboratoryTestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实验室测试数据采集Controller
 *
 * @author igdp
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/seed/laboratory/test")
public class LaboratoryTestDataController extends BaseController {

    @Autowired
    private ILaboratoryTestDataService laboratoryTestDataService;

    /**
     * 查询实验室测试数据列表
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LaboratoryTestDataDTO dto) {
        startPage();
        List<LaboratoryTestDataVO> list = laboratoryTestDataService.selectLaboratoryTestDataList(dto);
        return getDataTable(list);
    }

    /**
     * 获取实验室测试数据详细信息
     */
    @GetMapping("/{dataId}")
    public AjaxResult getInfo(@PathVariable String dataId) {
        LaboratoryTestDataVO vo = laboratoryTestDataService.selectLaboratoryTestDataById(dataId);
        return AjaxResult.success(vo);
    }

    /**
     * 新增实验室测试数据
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody LaboratoryTestDataDTO dto) {
        return AjaxResult.success(laboratoryTestDataService.insertLaboratoryTestData(dto));
    }

    /**
     * 修改实验室测试数据
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody LaboratoryTestDataDTO dto) {
        return AjaxResult.success(laboratoryTestDataService.updateLaboratoryTestData(dto));
    }

    /**
     * 删除实验室测试数据
     */
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody String[] dataIds) {
        return AjaxResult.success(laboratoryTestDataService.deleteLaboratoryTestDataByIds(dataIds));
    }

    /**
     * 提交审核
     */
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody LaboratoryTestDataDTO dto) {
        return AjaxResult.success(laboratoryTestDataService.submitForApproval(dto.getDataId()));
    }

    /**
     * 审核通过
     */
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody LaboratoryTestDataDTO dto) {
        return AjaxResult.success(laboratoryTestDataService.approveLaboratoryTestData(dto.getDataId(), dto.getAuditOpinion()));
    }

    /**
     * 审核退回
     */
    @PostMapping("/reject")
    public AjaxResult reject(@RequestBody LaboratoryTestDataDTO dto) {
        return AjaxResult.success(laboratoryTestDataService.rejectLaboratoryTestData(dto.getDataId(), dto.getAuditOpinion()));
    }

    /**
     * 归档
     */
    @PostMapping("/archive")
    public AjaxResult archive(@RequestBody LaboratoryTestDataDTO dto) {
        return AjaxResult.success(laboratoryTestDataService.archiveLaboratoryTestData(dto.getDataId()));
    }

    /**
     * 作废数据
     */
    @PostMapping("/cancel")
    public AjaxResult cancel(@RequestBody LaboratoryTestDataDTO dto) {
        return AjaxResult.success(laboratoryTestDataService.cancelLaboratoryTestData(dto.getDataId()));
    }

    /**
     * 作废审核记录
     */
    @PostMapping("/audit/cancel")
    public AjaxResult cancelAudit(@RequestBody LaboratoryTestDataDTO dto) {
        return AjaxResult.success(laboratoryTestDataService.cancelAuditRecord(dto.getDataId()));
    }

    /**
     * 查询审核列表
     */
    @PostMapping("/audit/list")
    public TableDataInfo auditList(@RequestBody LaboratoryTestDataDTO dto) {
        startPage();
        List<LaboratoryTestDataVO> list = laboratoryTestDataService.selectLaboratoryTestDataList(dto);
        return getDataTable(list);
    }
}
