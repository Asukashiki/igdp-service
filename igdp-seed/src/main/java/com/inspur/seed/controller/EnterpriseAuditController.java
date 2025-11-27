package com.inspur.seed.controller;

import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.EnterpriseAudit;
import com.inspur.seed.domain.EnterpriseInfo;
import com.inspur.seed.domain.vo.EnterpriseAuditVO;
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
    //@SaCheckPermission("seed:enterprise:audit:handle")
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
     * @param enterpriseName 企业名称
     * @param enterpriseId 企业ID
     * @param licenseNo 许可证号
     * @param certificationStatus 审核状态
     * @return 查询结果
     */
    //@SaCheckPermission("seed:enterprise:audit:list")
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String enterpriseName,
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) String licenseNo,
            @RequestParam(required = false) Integer certificationStatus) {

        // 使用RuoYi框架提供的分页方法
        startPage();
        
        // 调用服务方法查询数据，包含所有筛选条件
        List<EnterpriseAuditVO> list = enterpriseAuditService.queryAuditListWithFilter(
                enterpriseName, enterpriseId, licenseNo, certificationStatus);
        
        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }

    /**
     * 根据审核ID查询审核记录详情
     *
     * @param auditId 审核ID
     * @return 查询结果
     */
    //@SaCheckPermission("seed:enterprise:audit:query")
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
   // @SaCheckPermission("seed:enterprise:audit:pending")
    @GetMapping("/pending")
    public TableDataInfo pendingList(@RequestParam(required = false) String keyword) {
        // 使用RuoYi框架提供的分页方法
        startPage();
        
        // 查询认证状态为0（待审核）的企业
        List<EnterpriseInfo> list = enterpriseCertifyService.queryCertifyList(keyword, null, 0);
        
        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }

    /**
     * 根据企业ID查询最新审核记录
     *
     * @param enterpriseId 企业ID
     * @return 查询结果
     */
    //@SaCheckPermission("seed:enterprise:audit:query")
    @GetMapping("/latest/{enterpriseId}")
    public AjaxResult getLatest(@PathVariable String enterpriseId) {
        EnterpriseAudit audit = enterpriseAuditService.queryLatestByEnterpriseId(enterpriseId);
        if (audit == null) {
            return AjaxResult.error("暂无审核记录");
        }
        return AjaxResult.success(audit);
    }
}