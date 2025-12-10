package com.inspur.agriculture.input.controller.institution;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.BusinessType;
import com.inspur.agriculture.input.domain.institution.dto.InputAuditApproveDTO;
import com.inspur.agriculture.input.domain.institution.dto.InputAuditRejectDTO;
import com.inspur.agriculture.input.domain.institution.dto.InputEnterprisePageDTO;
import com.inspur.agriculture.input.domain.institution.vo.InputAuditRecordVO;
import com.inspur.agriculture.input.domain.institution.vo.InputEnterprisePageVO;
import com.inspur.agriculture.input.service.institution.IInputRegistrationAuditService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Union/Cooperative注册审核控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/input/registration/audit")
public class InputRegistrationAuditController extends BaseController {

    @Resource
    private IInputRegistrationAuditService auditService;

    /**
     * 分页查询待审核列表
     *
     * @param dto 查询DTO
     * @return 操作结果
     */
    @PostMapping("/page")
    public AjaxResult page(@RequestBody InputEnterprisePageDTO dto) {
        try {
            IPage<InputEnterprisePageVO> pageResult = auditService.pageAuditList(dto);
            return AjaxResult.success("Operation successful", pageResult);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 审核通过
     *
     * @param dto 审核通过DTO
     * @return 操作结果
     */
    @Log(title = "Union/Cooperative Audit", businessType = BusinessType.UPDATE)
    @PostMapping("/approve")
    public AjaxResult approve(@Validated @RequestBody InputAuditApproveDTO dto) {
        try {
            auditService.approve(dto);
            return AjaxResult.success("Approved successfully");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 审核驳回
     *
     * @param dto 审核驳回DTO
     * @return 操作结果
     */
    @Log(title = "Union/Cooperative Audit", businessType = BusinessType.UPDATE)
    @PostMapping("/reject")
    public AjaxResult reject(@Validated @RequestBody InputAuditRejectDTO dto) {
        try {
            auditService.reject(dto);
            return AjaxResult.success("Rejected successfully");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询审核记录列表
     *
     * @param enterpriseId 机构ID
     * @return 操作结果
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam String enterpriseId) {
        try {
            List<InputAuditRecordVO> records = auditService.listAuditRecords(enterpriseId);
            return AjaxResult.success("Operation successful", records);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
