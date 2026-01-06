package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.breeding.farming.domain.entity.FarmingRecord;
import com.inspur.seed.breeding.farming.service.IFarmingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 农事记录数据采集审核Controller
 *
 * @author igdp
 * @date 2025-12-23
 */
@RestController
@RequestMapping("/seed/farming/record/audit")
public class FarmingRecordAuditController extends BaseController {

    @Autowired
    private IFarmingRecordService farmingRecordService;

    /**
     * 查询待审核的农事记录数据列表
     */
    @PostMapping("/pending/list")
    public AjaxResult getPendingAuditList(@RequestBody FarmingRecord farmingRecord) {
        // 设置查询条件：只查询待审批状态的记录
        farmingRecord.setWorkflowStatus("S1");
        List<FarmingRecord> list = farmingRecordService.selectFarmingRecordList(farmingRecord);
        return AjaxResult.success(list);
    }

    /**
     * 查询所有状态的农事记录数据列表
     */
    @PostMapping("/all/list")
    public AjaxResult getAllStatusList(@RequestBody FarmingRecord farmingRecord) {
        // 查询所有状态的记录
        List<FarmingRecord> list = farmingRecordService.selectFarmingRecordList(farmingRecord);
        return AjaxResult.success(list);
    }

    /**
     * 获取农事记录审核详情
     */
    @GetMapping("/{farmingId}")
    public AjaxResult getFarmingRecordAuditDetail(@PathVariable String farmingId) {
        FarmingRecord farmingRecord = farmingRecordService.selectFarmingRecordById(farmingId);
        return AjaxResult.success(farmingRecord);
    }

    /**
     * 审核农事记录数据
     */
    @PostMapping("/audit")
    public AjaxResult auditFarmingRecord(@RequestBody FarmingRecord farmingRecord) {
        // 验证必填字段
        if (farmingRecord.getFarmingId() == null || farmingRecord.getFarmingId().trim().isEmpty()) {
            return AjaxResult.error("Farming ID is required");
        }

        if (farmingRecord.getWorkflowStatus() == null || farmingRecord.getWorkflowStatus().trim().isEmpty()) {
            return AjaxResult.error("Workflow status is required");
        }

        // 验证审核意见是否填写
        if (farmingRecord.getAuditRemark() == null || farmingRecord.getAuditRemark().trim().isEmpty()) {
            return AjaxResult.error("Audit remark is required");
        }

        // 设置审核相关信息
        farmingRecord.setAuditBy(SecurityUtils.getUsername());
        farmingRecord.setAuditTime(new Date());

        // 更新数据
        int result = farmingRecordService.updateFarmingRecord(farmingRecord);
        return result > 0 ? AjaxResult.success("Audit completed successfully") : AjaxResult.error("Audit failed");
    }

    /**
     * 作废农事记录数据
     */
    @PostMapping("/cancel/{farmingId}")
    public AjaxResult cancelFarmingRecord(@PathVariable String farmingId) {
        FarmingRecord farmingRecord = new FarmingRecord();
        farmingRecord.setFarmingId(farmingId);
        farmingRecord.setWorkflowStatus("S10"); // S10: 作废
        farmingRecord.setAuditBy(SecurityUtils.getUsername());
        farmingRecord.setAuditTime(new Date());
        farmingRecord.setAuditRemark("Cancelled by system");

        int result = farmingRecordService.updateFarmingRecord(farmingRecord);
        return result > 0 ? AjaxResult.success("Record cancelled successfully") : AjaxResult.error("Cancel failed");
    }

    /**
     * 同意审核
     */
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody FarmingRecord farmingRecord) {
        // 验证审核意见是否填写
        if (farmingRecord.getAuditRemark() == null || farmingRecord.getAuditRemark().trim().isEmpty()) {
            return AjaxResult.error("Audit remark is required");
        }

        // 设置审核相关信息
        farmingRecord.setAuditBy(SecurityUtils.getUsername());
        farmingRecord.setAuditTime(new Date());
        farmingRecord.setWorkflowStatus("S2"); // S2: 已审批

        // 更新数据
        int result = farmingRecordService.updateFarmingRecord(farmingRecord);
        return result > 0 ? AjaxResult.success("Approved successfully") : AjaxResult.error("Approval failed");
    }

    /**
     * 不同意审核（退回）
     */
    @PostMapping("/reject")
    public AjaxResult reject(@RequestBody FarmingRecord farmingRecord) {
        // 验证审核意见是否填写
        if (farmingRecord.getAuditRemark() == null || farmingRecord.getAuditRemark().trim().isEmpty()) {
            return AjaxResult.error("Audit remark is required");
        }

        // 设置审核相关信息
        farmingRecord.setAuditBy(SecurityUtils.getUsername());
        farmingRecord.setAuditTime(new Date());
        farmingRecord.setWorkflowStatus("S3"); // S3: 已退回

        // 更新数据
        int result = farmingRecordService.updateFarmingRecord(farmingRecord);
        return result > 0 ? AjaxResult.success("Rejected successfully") : AjaxResult.error("Rejection failed");
    }
}
