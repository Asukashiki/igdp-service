package com.inspur.farmland.management.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.farmland.management.bean.entity.LandInfo;
import com.inspur.farmland.management.service.ILandInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 土地信息Controller
 * 
 * @author inspur
 */
@RestController
@RequestMapping("/api/land")
public class LandInfoController {

    @Autowired
    private ILandInfoService landInfoService;

    /**
     * 根据用户ID获取土地列表
     */
    @GetMapping("/user/{userId}")
    public AjaxResult getLandsByUserId(@PathVariable String userId) {
        List<LandInfo> list = landInfoService.getLandsByUserId(userId);
        return AjaxResult.success(list);
    }

    /**
     * 添加土地信息
     */
    @PostMapping("/add")
    public AjaxResult addLandInfo(@RequestBody LandInfo landInfo) {
        boolean result = landInfoService.addLandInfo(landInfo);
        if (result) {
            return AjaxResult.success("添加土地信息成功", landInfo);
        }
        return AjaxResult.error("添加土地信息失败");
    }

    /**
     * 更新土地信息
     */
    @PostMapping("/update")
    public AjaxResult updateLandInfo(@RequestBody LandInfo landInfo) {
        boolean result = landInfoService.updateLandInfo(landInfo);
        if (result) {
            return AjaxResult.success("更新土地信息成功", landInfo);
        }
        return AjaxResult.error("更新土地信息失败");
    }

    /**
     * 删除土地信息
     */
    @PostMapping("/delete/{landId}")
    public AjaxResult deleteLandInfo(@PathVariable Long landId) {
        boolean result = landInfoService.deleteLandInfo(landId);
        if (result) {
            return AjaxResult.success("删除土地信息成功");
        }
        return AjaxResult.error("删除土地信息失败");
    }
}