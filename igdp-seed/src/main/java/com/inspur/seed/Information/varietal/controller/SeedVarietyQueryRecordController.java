package com.inspur.seed.Information.varietal.controller;

import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.Information.varietal.service.ISeedVarietyQueryRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 种子品种查询记录控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/seed/variety/public")
public class SeedVarietyQueryRecordController extends BaseController {

    @Resource
    private ISeedVarietyQueryRecordService seedVarietyQueryRecordService;

    /**
     * 品种公示列表查询
     *
     * @param varietyName 品种名称
     * @param year 发布年度
     * @param cropType 作物类型
     * @return 查询结果
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String varietyName,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String cropType) {
        // 使用RuoYi框架提供的分页方法
        startPage();

        List<Map<String, Object>> list = seedVarietyQueryRecordService.queryVarietyPublicList(varietyName, year, cropType);

        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }

    /**
     * 品种详情查询
     *
     * @param publishId 品种发布ID
     * @return 查询结果
     */
    @GetMapping("/detail/{publishId}")
    public AjaxResult detail(@PathVariable String publishId) {
        try {
            Map<String, Object> detail = seedVarietyQueryRecordService.queryVarietyDetail(publishId);
            return AjaxResult.success("查询成功", detail);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 记录查询行为
     *
     * @param params 查询参数
     * @return 记录结果
     */
    @Log(title = "种子品种查询记录", businessType = BusinessType.INSERT)
    @PostMapping("/record")
    public AjaxResult record(@RequestBody Map<String, Object> params) {
        try {
            String queryKeyword = (String) params.get("queryKeyword");
            String ipAddress = (String) params.get("ipAddress");
            Integer queryResultCount = (Integer) params.get("queryResultCount");
            String viewedPublishId = (String) params.get("viewedPublishId");

            String queryId = seedVarietyQueryRecordService.recordQueryBehavior(
                    queryKeyword, ipAddress, queryResultCount, viewedPublishId);

            Map<String, Object> data = new HashMap<>();
            data.put("queryId", queryId);

            return AjaxResult.success("记录成功", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
