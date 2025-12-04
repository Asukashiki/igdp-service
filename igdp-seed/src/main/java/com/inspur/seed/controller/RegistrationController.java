package com.inspur.seed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.dto.registration.*;
import com.inspur.seed.domain.vo.registration.EnterpriseDetailVO;
import com.inspur.seed.domain.vo.registration.EnterprisePageVO;
import com.inspur.seed.service.IRegistrationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Union/Cooperative注册申请控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/seed/registration")
public class RegistrationController extends BaseController {

    @Resource
    private IRegistrationService registrationService;

    /**
     * 新增机构注册申请
     *
     * @param dto 新增DTO
     * @return 操作结果
     */
    @Log(title = "Union/Cooperative Registration", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody EnterpriseAddDTO dto) {
        try {
            String id = registrationService.addRegistration(dto);

            Map<String, Object> data = new HashMap<>();
            data.put("id", id);

            return AjaxResult.success("Operation successful", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改机构注册申请
     *
     * @param dto 修改DTO
     * @return 操作结果
     */
    @Log(title = "Union/Cooperative Registration", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody EnterpriseUpdateDTO dto) {
        try {
            registrationService.updateRegistration(dto);
            return AjaxResult.success("Operation successful");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 提交机构注册申请
     *
     * @param dto 提交DTO
     * @return 操作结果
     */
    @Log(title = "Union/Cooperative Registration", businessType = BusinessType.UPDATE)
    @PostMapping("/submit")
    public AjaxResult submit(@Validated @RequestBody EnterpriseSubmitDTO dto) {
        try {
            registrationService.submitRegistration(dto);
            return AjaxResult.success("Submitted successfully");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询机构注册申请详情
     *
     * @param id 机构ID
     * @return 操作结果
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam String id) {
        try {
            EnterpriseDetailVO detail = registrationService.getDetail(id);
            return AjaxResult.success("Operation successful", detail);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 分页查询机构注册申请
     *
     * @param dto 查询DTO
     * @return 操作结果
     */
    @PostMapping("/page")
    public AjaxResult page(@RequestBody EnterprisePageDTO dto) {
        try {
            IPage<EnterprisePageVO> pageResult = registrationService.page(dto);
            return AjaxResult.success("Operation successful", pageResult);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除机构注册申请
     *
     * @param id 机构ID
     * @return 操作结果
     */
    @Log(title = "Union/Cooperative Registration", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult delete(@RequestParam String id) {
        try {
            registrationService.deleteRegistration(id);
            return AjaxResult.success("Deleted successfully");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
