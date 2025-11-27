package com.inspur.seed.controller;

import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.SeedPromotionInfo;
import com.inspur.seed.service.ISeedPromotionInfoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 种子推广信息控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/seed/promotion")
public class SeedPromotionInfoController extends BaseController {

    @Resource
    private ISeedPromotionInfoService seedPromotionInfoService;

    /**
     * 推广内容列表查询
     *
     * @param enterpriseId 企业ID
     * @param title 推广标题
     * @return 查询结果
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = true) String enterpriseId,
            @RequestParam(required = false) String title) {
        // 使用RuoYi框架提供的分页方法
        startPage();
        
        List<SeedPromotionInfo> list = seedPromotionInfoService.queryPromotionList(enterpriseId, title);
        
        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }

    /**
     * 推广内容上传（含链接生成）
     *
     * @param enterpriseId 企业ID
     * @param title 推广标题
     * @param videoFile 视频文件
     * @param promotionSummary 推广摘要
     * @param recommendedVarieties 推荐品种
     * @param validPeriod 有效期（天）
     * @return 上传结果
     */
    @Log(title = "种子推广信息", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public AjaxResult upload(
            @RequestParam(required = true) String enterpriseId,
            @RequestParam(required = true) String title,
            @RequestParam(required = true) MultipartFile videoFile,
            @RequestParam(required = false) String promotionSummary,
            @RequestParam(required = true) String recommendedVarieties,
            @RequestParam(required = true) Integer validPeriod) {
        try {
            Map<String, String> result = seedPromotionInfoService.uploadPromotion(
                    enterpriseId, title, videoFile, promotionSummary, recommendedVarieties, validPeriod);
            return AjaxResult.success("上传成功", result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 访问次数更新（链接访问触发）
     *
     * @param promotionId 推广ID
     * @return 更新结果
     */
    @PostMapping("/visit/{promotionId}")
    public AjaxResult visit(@PathVariable String promotionId) {
        try {
            Integer visitCount = seedPromotionInfoService.updateVisitCount(promotionId);
            Map<String, Object> data = new HashMap<>();
            data.put("visitCount", visitCount);
            return AjaxResult.success("更新成功", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}