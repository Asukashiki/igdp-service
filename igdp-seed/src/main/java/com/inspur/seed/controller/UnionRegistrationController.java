package com.inspur.seed.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.common.utils.LoginHelper;
import com.inspur.seed.domain.UnionInfo;
import com.inspur.seed.domain.dto.UnionRegistrationDTO;
import com.inspur.seed.service.IUnionRegistrationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Union注册管理控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/seed/union/registration")
public class UnionRegistrationController extends BaseController {

    @Resource
    private IUnionRegistrationService unionRegistrationService;

    /**
     * 提交Union注册申请
     *
     * @param dto Union注册申请数据
     * @return 提交结果
     */
    //@SaCheckPermission("seed:union:registration:submit")
    @Log(title = "Union注册申请", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    public AjaxResult submit(@Validated @RequestBody UnionRegistrationDTO dto) {
        try {
            String enterpriseId = unionRegistrationService.submitRegistration(dto);

            Map<String, Object> data = new HashMap<>();
            data.put("enterpriseId", enterpriseId);
            data.put("certificationStatus", 1); // 1=已注册
            data.put("submitTime", dto.getUnionInfo().getOperationTime());

            return AjaxResult.success("注册成功", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 保存Union注册草稿
     *
     * @param dto Union注册申请数据
     * @return 保存结果
     */
    //@SaCheckPermission("seed:union:registration:save")
    @Log(title = "Union注册草稿", businessType = BusinessType.UPDATE)
    @PostMapping("/save")
    public AjaxResult save(@Validated @RequestBody UnionRegistrationDTO dto) {
        try {
            String enterpriseId = unionRegistrationService.saveDraft(dto);

            Map<String, Object> data = new HashMap<>();
            data.put("enterpriseId", enterpriseId);
            data.put("certificationStatus", -1);

            return AjaxResult.success("草稿保存成功", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询Union注册列表
     *
     * @param enterpriseName 企业名称
     * @param enterpriseRegistrationId 企业注册ID
     * @param unifiedSocialCreditCode 统一社会信用代码
     * @param seedEnterpriseLicenseNumber 种子企业许可证编号
     * @param enterpriseType 企业类型
     * @param certificationStatus 认证状态
     * @return 查询结果
     */
    //@SaCheckPermission("seed:union:registration:list")
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String enterpriseName,
            @RequestParam(required = false) String enterpriseRegistrationId,
            @RequestParam(required = false) String unifiedSocialCreditCode,
            @RequestParam(required = false) String seedEnterpriseLicenseNumber,
            @RequestParam(required = false) String enterpriseType,
            @RequestParam(required = false) Integer certificationStatus) {
        
        startPage();
        List<UnionInfo> list = unionRegistrationService.queryList(
                enterpriseName, 
                enterpriseRegistrationId, 
                unifiedSocialCreditCode,
                seedEnterpriseLicenseNumber,
                enterpriseType,
                certificationStatus
        );
        return getDataTable(list);
    }

    /**
     * 根据企业ID查询Union详情
     *
     * @param enterpriseId 企业ID
     * @return 查询结果
     */
    //@SaCheckPermission("seed:union:registration:query")
    @GetMapping("/{enterpriseId}")
    public AjaxResult getInfo(@PathVariable String enterpriseId) {
        UnionRegistrationDTO dto = unionRegistrationService.queryByEnterpriseId(enterpriseId);
        if (dto == null) {
            return AjaxResult.error("Union信息不存在");
        }
        return AjaxResult.success(dto);
    }

    /**
     * 根据当前用户查询Union信息
     *
     * @return 查询结果
     */
    //@SaCheckPermission("seed:union:registration:query")
    @GetMapping("/info")
    public AjaxResult getInfoByUser() {
        String userId = LoginHelper.getUsername();
        UnionRegistrationDTO dto = unionRegistrationService.queryByUserId(userId);
        if (dto == null) {
            return AjaxResult.error("Union信息不存在");
        }
        return AjaxResult.success(dto);
    }

    /**
     * 删除Union注册信息
     *
     * @param enterpriseIds 企业ID数组
     * @return 删除结果
     */
    //@SaCheckPermission("seed:union:registration:remove")
    @Log(title = "Union注册", businessType = BusinessType.DELETE)
    @DeleteMapping("/{enterpriseIds}")
    public AjaxResult remove(@PathVariable String[] enterpriseIds) {
        return toAjax(unionRegistrationService.deleteByEnterpriseIds(enterpriseIds));
    }

    /**
     * 更新认证状态
     *
     * @param enterpriseId 企业ID
     * @param status 认证状态
     * @param rejectReason 驳回原因
     * @return 更新结果
     */
    //@SaCheckPermission("seed:union:registration:audit")
    @Log(title = "Union认证状态更新", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus")
    public AjaxResult updateStatus(
            @RequestParam String enterpriseId,
            @RequestParam Integer status,
            @RequestParam(required = false) String rejectReason) {
        try {
            unionRegistrationService.updateCertificationStatus(enterpriseId, status, rejectReason);
            return AjaxResult.success("状态更新成功");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 获取union详情
     *
     * @param id unionId
     * @return union详情
     */
    @GetMapping("getUnionInfoById/{id}")
    public AjaxResult getUnionInfo(@PathVariable("id") String id) {
        UnionInfo union = unionRegistrationService.getById(id);
        if (union == null) {
            return AjaxResult.error("union不存在");
        }
        return AjaxResult.success(union);
    }
}
