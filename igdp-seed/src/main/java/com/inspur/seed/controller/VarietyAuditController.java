package com.inspur.seed.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.VarietyAudit;
import com.inspur.seed.domain.VarietyRegistration;
import com.inspur.seed.service.IVarietyAuditService;
import com.inspur.seed.service.IVarietyRegistrationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品种审核控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/seed/variety/audit")
public class VarietyAuditController extends BaseController {

    @Resource
    private IVarietyAuditService varietyAuditService;

    @Resource
    private IVarietyRegistrationService varietyRegistrationService;

    /**
     * 处理品种审核
     *
     * @param varietyAudit 审核信息
     * @return 审核结果
     */
    @SaCheckPermission("seed:variety:audit:handle")
    @Log(title = "品种审核", businessType = BusinessType.UPDATE)
    @PostMapping("/handle")
    public AjaxResult handle(@Validated @RequestBody VarietyAudit varietyAudit) {
        try {
            String auditId = varietyAuditService.handleAudit(varietyAudit);

            // 获取更新后的登记申请状态
            VarietyRegistration registration = varietyRegistrationService.queryByRegistrationId(varietyAudit.getRegistrationId());

            Map<String, Object> data = new HashMap<>();
            data.put("registrationId", varietyAudit.getRegistrationId());
            data.put("recordStatus", registration != null ? registration.getRecordStatus() : null);
            data.put("auditTime", varietyAudit.getAuditTime());

            return AjaxResult.success("审核完成", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询审核记录列表
     *
     * @param varietyName 品种名称
     * @param enterpriseName 提交单位
     * @param auditResult 审核结果
     * @return 查询结果
     */
    @SaCheckPermission("seed:variety:audit:list")
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(required = false) String varietyName,
            @RequestParam(required = false) String enterpriseName,
            @RequestParam(required = false) Integer auditResult) {
        List<VarietyAudit> list = varietyAuditService.queryAuditList(varietyName, enterpriseName, auditResult);

        Map<String, Object> data = new HashMap<>();
        data.put("total", list.size());
        data.put("list", list);

        return AjaxResult.success("查询成功", data);
    }

    /**
     * 根据审核ID查询审核记录详情
     *
     * @param auditId 审核ID
     * @return 查询结果
     */
    @SaCheckPermission("seed:variety:audit:query")
    @GetMapping("/{auditId}")
    public AjaxResult getInfo(@PathVariable String auditId) {
        VarietyAudit audit = varietyAuditService.queryByAuditId(auditId);
        if (audit == null) {
            return AjaxResult.error("审核记录不存在");
        }
        return AjaxResult.success(audit);
    }

    /**
     * 查询待审核列表（备案状态为审核中的品种）
     *
     * @param varietyName 品种名称
     * @param enterpriseName 企业名称
     * @return 查询结果
     */
    @SaCheckPermission("seed:variety:audit:pending")
    @GetMapping("/pending")
    public AjaxResult pendingList(
            @RequestParam(required = false) String varietyName,
            @RequestParam(required = false) String enterpriseName) {
        // 查询备案状态为0（审核中）的品种登记
        List<VarietyRegistration> list = varietyRegistrationService.queryRegistrationList(varietyName, enterpriseName, null, null);

        // 过滤出审核中的记录
        list.removeIf(item -> item.getRecordStatus() != 0);

        Map<String, Object> data = new HashMap<>();
        data.put("total", list.size());
        data.put("list", list);

        return AjaxResult.success("查询成功", data);
    }

    /**
     * 根据登记ID查询最新审核记录
     *
     * @param registrationId 登记ID
     * @return 查询结果
     */
    @SaCheckPermission("seed:variety:audit:query")
    @GetMapping("/latest/{registrationId}")
    public AjaxResult getLatest(@PathVariable String registrationId) {
        VarietyAudit audit = varietyAuditService.queryLatestByRegistrationId(registrationId);
        if (audit == null) {
            return AjaxResult.error("暂无审核记录");
        }
        return AjaxResult.success(audit);
    }
}
