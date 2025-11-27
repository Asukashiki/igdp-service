package com.inspur.seed.controller;

import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.VarietyPublish;
import com.inspur.seed.service.IVarietyPublishService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品种发布控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/seed/variety/publish")
public class VarietyPublishController extends BaseController {

    @Resource
    private IVarietyPublishService varietyPublishService;

    /**
     * 发布品种
     *
     * @param varietyPublish 发布信息
     * @return 发布结果
     */
    //@SaCheckPermission("seed:variety:publish:handle")
    @Log(title = "品种发布", businessType = BusinessType.INSERT)
    @PostMapping("/handle")
    public AjaxResult handle(@Validated @RequestBody VarietyPublish varietyPublish) {
        try {
            String publishId = varietyPublishService.publishVariety(varietyPublish);

            Map<String, Object> data = new HashMap<>();
            data.put("publishId", publishId);
            data.put("publishNo", varietyPublish.getPublishNo());
            data.put("publishStatus", 1);
            data.put("publishTime", varietyPublish.getPublishTime());

            return AjaxResult.success("发布成功", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询已发布品种列表
     *
     * @param varietyName 品种名称
     * @param cropType 作物类型
     * @param publishStatus 公示状态
     * @return 查询结果
     */
    //@SaCheckPermission("seed:variety:publish:list")
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String varietyName,
            @RequestParam(required = false) String cropType,
            @RequestParam(required = false) Integer publishStatus) {
        // 使用RuoYi框架提供的分页方法
        startPage();
        
        // 调用服务方法查询数据
        List<VarietyPublish> list = varietyPublishService.queryPublishList(varietyName, cropType, publishStatus);
        
        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }

    /**
     * 根据发布ID查询发布详情
     *
     * @param publishId 发布ID
     * @return 查询结果
     */
    //@SaCheckPermission("seed:variety:publish:query")
    @GetMapping("/{publishId}")
    public AjaxResult getInfo(@PathVariable String publishId) {
        VarietyPublish varietyPublish = varietyPublishService.queryByPublishId(publishId);
        if (varietyPublish == null) {
            return AjaxResult.error("发布记录不存在");
        }
        return AjaxResult.success(varietyPublish);
    }

    /**
     * 下架品种
     *
     * @param publishId 发布ID
     * @return 下架结果
     */
    //@SaCheckPermission("seed:variety:publish:unpublish")
    @Log(title = "品种下架", businessType = BusinessType.UPDATE)
    @PostMapping("/unpublish/{publishId}")
    public AjaxResult unpublish(@PathVariable String publishId) {
        try {
            varietyPublishService.unpublishVariety(publishId);
            return AjaxResult.success("下架成功");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 根据登记ID查询发布记录
     *
     * @param registrationId 登记ID
     * @return 查询结果
     */
    //@SaCheckPermission("seed:variety:publish:query")
    @GetMapping("/registration/{registrationId}")
    public AjaxResult getByRegistration(@PathVariable String registrationId) {
        VarietyPublish varietyPublish = varietyPublishService.queryByRegistrationId(registrationId);
        if (varietyPublish == null) {
            return AjaxResult.error("该品种暂未发布");
        }
        return AjaxResult.success(varietyPublish);
    }
}