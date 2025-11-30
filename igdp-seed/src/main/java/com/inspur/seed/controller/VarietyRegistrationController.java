package com.inspur.seed.controller;

import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.VarietyRegistration;
import com.inspur.seed.service.IVarietyRegistrationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品种登记控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/seed/variety/registration")
public class VarietyRegistrationController extends BaseController {

    @Resource
    private IVarietyRegistrationService varietyRegistrationService;

    /**
     * 提交品种登记申请
     *
     * @param varietyRegistration 品种登记信息
     * @return 提交结果
     */
    //@SaCheckPermission("seed:variety:registration:submit")
    @Log(title = "品种登记", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    public AjaxResult submit(@Validated @RequestBody VarietyRegistration varietyRegistration) {
        try {
            String registrationId = varietyRegistrationService.submitRegistration(varietyRegistration);

            Map<String, Object> data = new HashMap<>();
            data.put("registrationId", registrationId);
            data.put("registrationNo", varietyRegistration.getRegistrationNo());
            data.put("recordStatus", 0);
            data.put("submitTime", varietyRegistration.getOperationTime());

            return AjaxResult.success("提交成功", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询品种登记列表
     *
     * @param varietyName 品种名称
     * @param cropType 作物类型
     * @param recordStatus 备案状态
     * @return 查询结果
     */
   // @SaCheckPermission("seed:variety:registration:list")
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String varietyName,
            @RequestParam(required = false) String cropType,
            @RequestParam(required = false) String recordStatus
            ) {
        // 使用RuoYi框架提供的分页方法
        startPage();
        
        // 调用服务方法查询数据
        List<VarietyRegistration> list = varietyRegistrationService.queryRegistrationList(varietyName, cropType, recordStatus);
        
        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }

    /**
     * 根据登记ID查询品种登记详情
     *
     * @param registrationId 登记ID
     * @return 查询结果
     */
   // @SaCheckPermission("seed:variety:registration:query")
    @GetMapping("/{registrationId}")
    public AjaxResult getInfo(@PathVariable String registrationId) {
        VarietyRegistration varietyRegistration = varietyRegistrationService.queryByRegistrationId(registrationId);
        if (varietyRegistration == null) {
            return AjaxResult.error("品种登记信息不存在");
        }
        return AjaxResult.success(varietyRegistration);
    }

    /**
     * 保存表单
     *
     * @param varietyRegistration 品种登记信息
     * @return 保存结果
     */
    //@SaCheckPermission("seed:variety:registration:save")
    @Log(title = "品种登记", businessType = BusinessType.UPDATE)
    @PostMapping("/save")
    public AjaxResult save(@Validated @RequestBody VarietyRegistration varietyRegistration) {
        try {
            // 如果登记ID为空，说明是新保存
            if (varietyRegistration.getRegistrationId() == null) {
                String registrationId = varietyRegistrationService.submitRegistration(varietyRegistration);
                return AjaxResult.success("表单保存成功", registrationId);
            } else {
                // 更新品种登记信息
                boolean result = varietyRegistrationService.updateById(varietyRegistration);
                if (result) {
                    return AjaxResult.success("表单保存成功");
                } else {
                    return AjaxResult.error("表单保存失败");
                }
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询待发布的品种列表
     *
     * @param varietyName 品种名称
     * @param cropType 作物类型
     * @return 查询结果
     */
    //@SaCheckPermission("seed:variety:registration:pending")
    @GetMapping("/pending")
    public TableDataInfo pendingPublish(
            @RequestParam(required = false) String varietyName,
            @RequestParam(required = false) String cropType) {
        // 使用RuoYi框架提供的分页方法
        startPage();
        
        List<VarietyRegistration> list = varietyRegistrationService.queryPendingPublishList(varietyName, cropType);
        
        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }
}