package com.inspur.seed.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.BusinessType;
import com.inspur.common.utils.LoginHelper;
import com.inspur.seed.domain.EnterpriseInfo;
import com.inspur.seed.service.IEnterpriseCertifyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 种子企业认证控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/seed/enterprise/certify")
public class EnterpriseCertifyController extends BaseController {

    @Resource
    private IEnterpriseCertifyService enterpriseCertifyService;

    /**
     * 提交认证申请
     *
     * @param enterpriseInfo 企业认证信息
     * @return 提交结果
     */
    //@SaCheckPermission("seed:enterprise:certify:submit")
    @Log(title = "种子企业认证", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    public AjaxResult submit(@Validated @RequestBody EnterpriseInfo enterpriseInfo) {
        try {
            String enterpriseId = enterpriseCertifyService.submitCertifyApplication(enterpriseInfo);

            Map<String, Object> data = new HashMap<>();
            data.put("enterpriseId", enterpriseId);
            data.put("certificationStatus", 0);
            data.put("submitTime", enterpriseInfo.getOperationTime());

            return AjaxResult.success("提交成功", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询认证申请列表
     *
     * @param keyword 关键词（企业名称/信用代码/许可证编号）
     * @param enterpriseType 企业类型
     * @param certificationStatus 认证状态
     * @return 查询结果
     */
    //@SaCheckPermission("seed:enterprise:certify:list")
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String enterpriseType,
            @RequestParam(required = false) Integer certificationStatus) {
        List<EnterpriseInfo> list = enterpriseCertifyService.queryCertifyList(keyword, enterpriseType, certificationStatus);

        Map<String, Object> data = new HashMap<>();
        data.put("total", list.size());
        data.put("list", list);

        return AjaxResult.success("查询成功", data);
    }

    /**
     * 根据企业ID查询认证信息详情
     *
     * @param enterpriseId 企业ID
     * @return 查询结果
     */
   // @SaCheckPermission("seed:enterprise:certify:query")
    @GetMapping("/{enterpriseId}")
    public AjaxResult getInfo(@PathVariable String enterpriseId) {
        EnterpriseInfo enterpriseInfo = enterpriseCertifyService.queryByEnterpriseId(enterpriseId);
        if (enterpriseInfo == null) {
            return AjaxResult.error("企业信息不存在");
        }
        return AjaxResult.success(enterpriseInfo);
    }

    /**
     * 根据用户ID查询认证信息详情
     *
     * @return 查询结果
     */
    // @SaCheckPermission("seed:enterprise:certify:query")
    @GetMapping("/info")
    public AjaxResult getInfoUserId() {
        String userId = LoginHelper.getUsername();
        EnterpriseInfo enterpriseInfo = enterpriseCertifyService.queryByUserId(userId);
        // 即使没有企业信息，也返回成功（data 为 null）
        // 这是正常的业务状态，不应该返回错误码
        return AjaxResult.success(enterpriseInfo);
    }

    /**
     * 保存表单（暂存草稿）
     *
     * @param enterpriseInfo 企业认证信息
     * @return 保存结果
     */
   // @SaCheckPermission("seed:enterprise:certify:save")
    @Log(title = "种子企业认证草稿", businessType = BusinessType.UPDATE)
    @PostMapping("/save")
    public AjaxResult save(@Validated @RequestBody EnterpriseInfo enterpriseInfo) {
        try {
            String enterpriseId;
            // 如果企业ID为空，说明是新保存草稿
            if (enterpriseInfo.getEnterpriseId() == null || enterpriseInfo.getEnterpriseId().isEmpty()) {
                enterpriseId = enterpriseCertifyService.saveDraft(enterpriseInfo);

                Map<String, Object> data = new HashMap<>();
                data.put("enterpriseId", enterpriseId);
                data.put("certificationStatus", -1);
                data.put("saveTime", enterpriseInfo.getOperationTime());

                return AjaxResult.success("草稿保存成功", data);
            } else {
                // 更新企业草稿信息
                boolean result = enterpriseCertifyService.updateDraft(enterpriseInfo);
                if (result) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("enterpriseId", enterpriseInfo.getEnterpriseId());
                    data.put("updateTime", enterpriseInfo.getUpdateTime());

                    return AjaxResult.success("草稿更新成功", data);
                } else {
                    return AjaxResult.error("草稿保存失败");
                }
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
