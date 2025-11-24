package com.inspur.farmland.management.controller;

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
    public FarmerCertification getCertificationByUserId(@PathVariable Long userId) {
        return farmerCertificationService.getCertificationByUserId(userId);
    }

    /**
     * 获取待审批列表
     */
    @GetMapping("/pending")
    public List<FarmerCertification> getPendingCertifications() {
        return farmerCertificationService.getPendingCertifications();
    }

    /**
     * 审批通过
     */
    @PostMapping("/{certId}/approve")
    public boolean approveCertification(@PathVariable Long certId, @RequestParam Long approverId) {
        return farmerCertificationService.approveCertification(certId, approverId);
    }

    /**
     * 审批驳回
     */
    @PostMapping("/{certId}/reject")
    public boolean rejectCertification(@PathVariable Long certId, @RequestParam Long approverId, 
                                      @RequestParam String rejectReason) {
        return farmerCertificationService.rejectCertification(certId, approverId, rejectReason);
    }
}