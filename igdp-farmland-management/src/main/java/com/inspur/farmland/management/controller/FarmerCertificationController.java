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
     * 农民认证申请
     *
     * @param certification 认证申请信息
     * @return 操作结果
     */
    @PostMapping("/apply")
    public AjaxResult applyCertification(@RequestBody FarmerCertification certification) {
        // 参数校验
        if (certification.getUserId() == null || certification.getUserId().trim().isEmpty()) {
            return AjaxResult.error("用户ID不能为空");
        }
        if (certification.getRealName() == null || certification.getRealName().trim().isEmpty()) {
            return AjaxResult.error("真实姓名不能为空");
        }
        if (certification.getIdCard() == null || certification.getIdCard().trim().isEmpty()) {
            return AjaxResult.error("身份证号不能为空");
        }

        // 身份证号格式验证(18位)
        if (!certification.getIdCard().matches("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$")) {
            return AjaxResult.error("身份证号格式不正确");
        }

        // 检查用户是否已经申请过认证
        FarmerCertification existCertification = farmerCertificationService.getCertificationByUserId(certification.getUserId());
        if (existCertification != null) {
            if (existCertification.getStatus() == 1) {
                return AjaxResult.error("您的认证申请正在审核中，请勿重复提交");
            } else if (existCertification.getStatus() == 2) {
                return AjaxResult.error("您已通过认证，无需重复申请");
            }
        }

        boolean result = farmerCertificationService.applyCertification(certification);
        if (result) {
            return AjaxResult.success("认证申请提交成功，请等待审核", certification);
        }
        return AjaxResult.error("认证申请提交失败");
    }

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