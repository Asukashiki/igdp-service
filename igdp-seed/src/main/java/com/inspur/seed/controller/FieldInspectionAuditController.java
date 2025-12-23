package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.BreedingBatch;
import com.inspur.seed.domain.PlotInfo;
import com.inspur.seed.domain.dto.BreedingYieldDataDTO;
import com.inspur.seed.domain.vo.BreedingYieldDataVO;
import com.inspur.seed.service.IBreedingBatchService;
import com.inspur.seed.service.IBreedingYieldDataService;
import com.inspur.seed.service.IPlotInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 田间检验数据采集审核Controller
 *
 * @author igdp
 * @date 2025-12-22
 */
@RestController
@RequestMapping("/seed/field-inspection")
public class FieldInspectionAuditController  extends BaseController {

    @Autowired
    private IBreedingYieldDataService breedingYieldDataService;

    @Autowired
    private IBreedingBatchService breedingBatchService;

    @Autowired
    private IPlotInfoService plotInfoService;

    /**
     * 获取待审核的田间检验数据列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BreedingYieldDataDTO dto) {
        // 使用RuoYi框架提供的分页方法
        startPage();

        // 当workflowStatus为空时，查询所有状态（包括已审核和未审核）
        // 如果前端传入特定状态，则按该状态筛选

        List<BreedingYieldDataVO> list = breedingYieldDataService.selectBreedingYieldDataList(dto);
        return getDataTable(list);
    }

    /**
     * 获取育种批次下拉选项
     */
    @GetMapping("/batch/selectList")
    public AjaxResult getBatchSelectList() {
        List<BreedingBatch> list = breedingBatchService.selectBatchOptions();
        return AjaxResult.success(list);
    }

    /**
     * 获取地块下拉选项
     */
    @GetMapping("/plot/selectList")
    public AjaxResult getPlotSelectList() {
        List<PlotInfo> list = plotInfoService.selectPlotOptions(null, null);
        return AjaxResult.success(list);
    }

    /**
     * 获取田间检验数据详情（用于审核页面展示）
     */
    @GetMapping("/audit/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(breedingYieldDataService.selectBreedingYieldDataById(id));
    }

    /**
     * 同意审核
     */
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody BreedingYieldDataDTO breedingYieldDataDTO) {
        // 验证审核意见是否填写
        if (breedingYieldDataDTO.getRemark() == null || breedingYieldDataDTO.getRemark().trim().isEmpty()) {
            return AjaxResult.error("审核意见为必填项");
        }

        // 设置审核相关信息
        breedingYieldDataDTO.setAuditBy(SecurityUtils.getUsername());
        breedingYieldDataDTO.setAuditTime(LocalDateTime.now());
        breedingYieldDataDTO.setWorkflowStatus("S2"); // S2: 审核通过

        // 更新数据
        return toAjax(breedingYieldDataService.updateBreedingYieldData(breedingYieldDataDTO));
    }

    /**
     * 不同意审核（退回）
     */
    @PostMapping("/reject")
    public AjaxResult reject(@RequestBody BreedingYieldDataDTO breedingYieldDataDTO) {
        // 验证审核意见是否填写
        if (breedingYieldDataDTO.getRemark() == null || breedingYieldDataDTO.getRemark().trim().isEmpty()) {
            return AjaxResult.error("审核意见为必填项");
        }

        // 设置审核相关信息
        breedingYieldDataDTO.setAuditBy(SecurityUtils.getUsername());
        breedingYieldDataDTO.setAuditTime(LocalDateTime.now());
        breedingYieldDataDTO.setWorkflowStatus("S3"); // S3: 审核驳回

        // 更新数据
        return toAjax(breedingYieldDataService.updateBreedingYieldData(breedingYieldDataDTO));
    }

    /**
     * 作废（取消审核通过）
     */
    @PostMapping("/void")
    public AjaxResult voidAudit(@RequestBody BreedingYieldDataDTO breedingYieldDataDTO) {
        // 验证审核意见是否填写
        if (breedingYieldDataDTO.getRemark() == null || breedingYieldDataDTO.getRemark().trim().isEmpty()) {
            return AjaxResult.error("作废原因为必填项");
        }

        // 设置审核相关信息
        breedingYieldDataDTO.setAuditBy(SecurityUtils.getUsername());
        breedingYieldDataDTO.setAuditTime(LocalDateTime.now());
        breedingYieldDataDTO.setWorkflowStatus("S10"); // S10: 作废

        // 更新数据
        return toAjax(breedingYieldDataService.updateBreedingYieldData(breedingYieldDataDTO));
    }

    /**
     * 判断操作是否成功
     */
    public AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
