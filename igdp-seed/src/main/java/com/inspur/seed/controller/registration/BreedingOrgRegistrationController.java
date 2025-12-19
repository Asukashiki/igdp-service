package com.inspur.seed.controller.registration;

import com.inspur.seed.domain.registration.BreedingOrgRegistration;
import com.inspur.seed.dto.registration.BreedingAuditDTO;
import com.inspur.seed.dto.registration.BreedingOrgRegistrationDTO;
import com.inspur.seed.service.registration.IBreedingOrgRegistrationService;
import com.inspur.seed.vo.registration.BreedingOrgRegistrationDetailVO;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 繁殖机构注册申请控制器
 * 用于繁殖机构的资质注册与审核
 *
 * @author igdp
 */
@RestController
@RequestMapping("/seed/breeding-org-registration")
public class BreedingOrgRegistrationController {

    @Autowired
    private IBreedingOrgRegistrationService breedingOrgRegistrationService;

    /**
     * 提交/修改注册申请
     *
     * @param dto 注册申请DTO
     * @return 申请ID
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody BreedingOrgRegistrationDTO dto) {
        return breedingOrgRegistrationService.submitRegistration(dto);
    }

    /**
     * 审核操作
     *
     * @param dto 审核DTO
     * @return 操作结果
     */
    @PostMapping("/audit")
    public R<String> audit(@RequestBody BreedingAuditDTO dto) {
        return breedingOrgRegistrationService.auditRegistration(dto.getRegistrationId(), dto.getAuditResult(), dto.getAuditComment());
    }

    /**
     * 分页查询申请列表
     *
     * @param orgName     机构名称
     * @param orgType     机构类型
     * @param auditStatus 审核状态
     * @param page        页码
     * @param pageSize    每页条数
     * @return 分页结果
     */
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(required = false) String orgName,
            @RequestParam(required = false) String orgType,
            @RequestParam(required = false) Integer auditStatus,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("orgName", orgName);
        params.put("orgType", orgType);
        params.put("auditStatus", auditStatus);
        params.put("page", page);
        params.put("pageSize", pageSize);

        Map<String, Object> result = breedingOrgRegistrationService.queryList(params);
        return AjaxResult.success(result);
    }

    /**
     * 获取申请详情（含审核历史）
     *
     * @param id 注册申请ID
     * @return 详情VO
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam String id) {
        BreedingOrgRegistrationDetailVO detail = breedingOrgRegistrationService.getDetail(id);
        return AjaxResult.success(detail);
    }

    /**
     * 检查用户名是否可用
     *
     * @param username  用户名
     * @param excludeId 排除的ID
     * @return true-可用，false-已存在
     */
    @GetMapping("/checkUsername")
    public AjaxResult checkUsername(
            @RequestParam String username,
            @RequestParam(required = false) String excludeId
    ) {
        boolean unique = breedingOrgRegistrationService.checkUsernameUnique(username, excludeId);
        return AjaxResult.success(unique);
    }
}
