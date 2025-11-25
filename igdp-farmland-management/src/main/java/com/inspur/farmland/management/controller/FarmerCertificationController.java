package com.inspur.farmland.management.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.farmland.management.bean.entity.FarmerCertification;
import com.inspur.farmland.management.service.IFarmerCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 农民认证Controller
 * 
 * @author inspur
 */
@RestController
@RequestMapping("/api/farmer/certification")
public class FarmerCertificationController {

    @Autowired
    private IFarmerCertificationService farmerCertificationService;

    /**
     * 根据用户ID查询认证状态
     */
    @GetMapping("/user/{userId}")
    public AjaxResult getCertificationByUserId(@PathVariable String userId) {
        FarmerCertification certification = farmerCertificationService.getCertificationByUserId(userId);
        if (certification == null) {
            return AjaxResult.error("未找到认证信息");
        }
        return AjaxResult.success(certification);
    }

    /**
     * 获取待审批列表
     */
    @GetMapping("/pending")
    public AjaxResult getPendingCertifications() {
        List<FarmerCertification> list = farmerCertificationService.getPendingCertifications();
        return AjaxResult.success(list);
    }

    /**
     * 审批通过
     */
    @PostMapping("/{certId}/approve")
    public AjaxResult approveCertification(@PathVariable Long certId, @RequestParam String approverId) {
        boolean result = farmerCertificationService.approveCertification(certId, approverId);
        if (result) {
            return AjaxResult.success("审批通过成功");
        }
        return AjaxResult.error("审批通过失败");
    }

    /**
     * 审批驳回
     */
    @PostMapping("/{certId}/reject")
    public AjaxResult rejectCertification(@PathVariable Long certId, @RequestParam String approverId,
                                      @RequestParam String rejectReason) {
        boolean result = farmerCertificationService.rejectCertification(certId, approverId, rejectReason);
        if (result) {
            return AjaxResult.success("审批驳回成功");
        }
        return AjaxResult.error("审批驳回失败");
    }
}