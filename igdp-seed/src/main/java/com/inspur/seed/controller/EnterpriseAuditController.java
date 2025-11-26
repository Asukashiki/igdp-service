package com.inspur.seed.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.EnterpriseAudit;
import com.inspur.seed.domain.EnterpriseInfo;
import com.inspur.seed.service.IEnterpriseAuditService;
import com.inspur.seed.service.IEnterpriseCertifyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 种子企业审核控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/seed/enterprise/audit")
public class EnterpriseAuditController extends BaseController {

    @Resource
    private IEnterpriseAuditService enterpriseAuditService;

    @Resource
    private IEnterpriseCertifyService enterpriseCertifyService;

    /**
     * 处理企业审核
     *
     * @param enterpriseAudit 审核信息
     * @return 审核结果
     */
    @SaCheckPermission("seed:enterprise:audit:handle")
    @Log(title = "种子企业审核", businessType = BusinessType.UPDATE)
    @PostMapping("/handle")
    public AjaxResult handle(@Validated @RequestBody EnterpriseAudit enterpriseAudit) {
        try {
            String auditId = enterpriseAuditService.handleAudit(enterpriseAudit);

            Map<String, Object> data = new HashMap<>();
            data.put("enterpriseId", enterpriseAudit.getEnterpriseId());
            data.put("certificationStatus", enterpriseAudit.getAuditResult());
            data.put("auditTime", enterpriseAudit.getAuditTime());

            return AjaxResult.success("审核完成", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询审核记录列表
     *
     * @param enterpriseId 企业ID
     * @param auditResult 审核结果
     * @param auditStage 审核阶段
     * @return 查询结果
     */
    @SaCheckPermission("seed:enterprise:audit:list")
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) Integer auditResult,
            @RequestParam(required = false) String auditStage) {
        List<EnterpriseAudit> list = enterpriseAuditService.queryAuditList(enterpriseId, auditResult, auditStage);

        // 为每条审核记录附加企业名称信息
        for (EnterpriseAudit audit : list) {
            EnterpriseInfo enterpriseInfo = enterpriseCertifyService.queryByEnterpriseId(audit.getEnterpriseId());
            if (enterpriseInfo != null) {
                // 可以通过扩展EnterpriseAudit类添加enterpriseName字段，或者使用Map返回
                // 这里简化处理，只返回原始数据
            }
        }

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
    @SaCheckPermission("seed:enterprise:audit:query")
    @GetMapping("/{auditId}")
    public AjaxResult getInfo(@PathVariable String auditId) {
        EnterpriseAudit audit = enterpriseAuditService.queryByAuditId(auditId);
        if (audit == null) {
            return AjaxResult.error("审核记录不存在");
        }
        return AjaxResult.success(audit);
    }

    /**
     * 查询待审核列表（认证状态为待审核的企业）
     *
     * @param keyword 关键词（企业名称/信用代码/许可证编号）
     * @return 查询结果
     */
    @SaCheckPermission("seed:enterprise:audit:pending")
    @GetMapping("/pending")
    public AjaxResult pendingList(@RequestParam(required = false) String keyword) {
        // 查询认证状态为0（待审核）的企业
        List<EnterpriseInfo> list = enterpriseCertifyService.queryCertifyList(keyword, null, 0);

        Map<String, Object> data = new HashMap<>();
        data.put("total", list.size());
        data.put("list", list);

        return AjaxResult.success("查询成功", data);
    }

    /**
     * 根据企业ID查询最新审核记录
     *
     * @param enterpriseId 企业ID
     * @return 查询结果
     */
    @SaCheckPermission("seed:enterprise:audit:query")
    @GetMapping("/latest/{enterpriseId}")
    public AjaxResult getLatest(@PathVariable String enterpriseId) {
        EnterpriseAudit audit = enterpriseAuditService.queryLatestByEnterpriseId(enterpriseId);
        if (audit == null) {
            return AjaxResult.error("暂无审核记录");
        }
        return AjaxResult.success(audit);
    }
}
