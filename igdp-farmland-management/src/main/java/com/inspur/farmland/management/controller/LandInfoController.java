package com.inspur.farmland.management.controller;

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
    public List<LandInfo> getLandsByUserId(@PathVariable Long userId) {
        return landInfoService.getLandsByUserId(userId);
    }

    /**
     * 添加土地信息
     */
    @PostMapping
    public boolean addLandInfo(@RequestBody LandInfo landInfo) {
        return landInfoService.addLandInfo(landInfo);
    }

    /**
     * 更新土地信息
     */
    @PutMapping
    public boolean updateLandInfo(@RequestBody LandInfo landInfo) {
        return landInfoService.updateLandInfo(landInfo);
    }

    /**
     * 删除土地信息
     */
    @DeleteMapping("/{landId}")
    public boolean deleteLandInfo(@PathVariable Long landId) {
        return landInfoService.deleteLandInfo(landId);
    }
}