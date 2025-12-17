package com.inspur.agriculture.input.controller.registration;

import com.inspur.agriculture.input.domain.registration.OrgRegistration;
import com.inspur.agriculture.input.dto.registration.AuditDTO;
import com.inspur.agriculture.input.dto.registration.OrgRegistrationDTO;
import com.inspur.agriculture.input.service.registration.IOrgRegistrationService;
import com.inspur.agriculture.input.vo.registration.OrgRegistrationDetailVO;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 机构注册申请控制器
 * 用于 Union（联合会）和 Cooperative（合作社）的资质注册与审核
 *
 * @author igdp
 */
@RestController
@RequestMapping("/input/org-registration")
public class OrgRegistrationController {

    @Autowired
    private IOrgRegistrationService orgRegistrationService;

    /**
     * 提交/修改注册申请
     * - 如果是新增：插入数据，状态置为 0 (待审核)，密码需加密
     * - 如果是修改（驳回后重填）：根据ID更新数据，将状态重置为 0 (待审核)
     *
     * @param dto 注册申请DTO
     * @return 申请ID
     */
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody OrgRegistrationDTO dto) {
        String id = orgRegistrationService.submitRegistration(dto);
        return AjaxResult.success("提交成功", id);
    }

    /**
     * 审核操作
     * - 校验权限：UNION类型需Zone级别用户，COOPERATIVE类型需Woreda级别用户
     * - 更新审核状态并记录审核日志
     * - 如果通过，同步账号到用户中心
     *
     * @param dto 审核DTO
     * @return 操作结果
     */
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody AuditDTO dto) {
        orgRegistrationService.audit(dto);
        return AjaxResult.success("审核操作成功");
    }

    /**
     * 分页查询申请列表
     *
     * @param orgName     机构名称（模糊搜索）
     * @param orgType     机构类型
     * @param auditStatus 审核状态：0-待审核, 1-已通过, 2-已驳回
     * @param regionCode  区域代码
     * @param page        页码
     * @param pageSize    每页条数
     * @return 分页结果
     */
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(required = false) String orgName,
            @RequestParam(required = false) String orgType,
            @RequestParam(required = false) Integer auditStatus,
            @RequestParam(required = false) String regionCode,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        OrgRegistration query = new OrgRegistration();
        query.setOrgName(orgName);
        query.setOrgType(orgType);
        query.setAuditStatus(auditStatus);
        query.setRegionCode(regionCode);

        Map<String, Object> result = orgRegistrationService.listRegistrations(query, page, pageSize);
        return AjaxResult.success(result);
    }

    /**
     * 获取申请详情（含审核历史）
     *
     * @param id 注册申请ID
     * @return 详情VO（包含基础信息和审核日志列表）
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam String id) {
        OrgRegistrationDetailVO detail = orgRegistrationService.getDetail(id);
        return AjaxResult.success(detail);
    }

    /**
     * 检查用户名是否可用
     *
     * @param username  用户名
     * @param excludeId 排除的ID（修改时排除自身）
     * @return true-可用，false-已存在
     */
    @GetMapping("/checkUsername")
    public AjaxResult checkUsername(
            @RequestParam String username,
            @RequestParam(required = false) String excludeId
    ) {
        boolean unique = orgRegistrationService.checkUsernameUnique(username, excludeId);
        return AjaxResult.success(unique);
    }
}
