package com.inspur.farmland.management.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.farmland.management.bean.entity.LandInfo;
import com.inspur.farmland.management.service.ILandInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 查询土地信息列表(分页)
     *
     * @param landName      地块名称
     * @param landType      地块类型
     * @param currentStatus 当前状态
     * @param adCode        行政区划代码
     * @param farmerUserId  所属农民用户ID
     * @param keyword       关键词搜索
     * @param page          页码
     * @param pageSize      每页数量
     * @return 土地信息列表
     */
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(required = false) String landName,
            @RequestParam(required = false) String landType,
            @RequestParam(required = false) String currentStatus,
            @RequestParam(required = false) String adCode,
            @RequestParam(required = false) String farmerUserId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        LandInfo landInfo = new LandInfo();
        landInfo.setLandName(landName);
        landInfo.setLandType(landType);
        landInfo.setCurrentStatus(currentStatus);
        landInfo.setAdCode(adCode);
        landInfo.setFarmerUserId(farmerUserId);

        // 如果有keyword,覆盖landName进行模糊搜索
        if (keyword != null && !keyword.trim().isEmpty()) {
            landInfo.setLandName(keyword);
        }

        // 启动分页
        PageHelper.startPage(page, pageSize);
        List<LandInfo> list = landInfoService.selectLandList(landInfo);
        PageInfo<LandInfo> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("page", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());

        return AjaxResult.success(result);
    }

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