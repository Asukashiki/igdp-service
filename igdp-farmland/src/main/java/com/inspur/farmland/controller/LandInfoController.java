package com.inspur.farmland.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.farmland.domain.LandInfo;
import com.inspur.farmland.service.ILandInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 土地管理Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/farmland/land")
public class LandInfoController extends BaseController {

    @Autowired
    private ILandInfoService landInfoService;

    /**
     * 获取土地分页列表
     */
    @GetMapping("/page")
    public TableDataInfo page(LandInfo landInfo) {
        startPage();
        List<LandInfo> list = landInfoService.selectLandInfoList(landInfo);
        return getDataTable(list);
    }

    /**
     * 获取土地详情
     */
    @GetMapping("/{landId}")
    public AjaxResult getInfo(
            @PathVariable String landId) {
        LandInfo landInfo = landInfoService.selectLandInfoByLandId(landId);
        if (landInfo == null) {
            return AjaxResult.error("土地信息不存在");
        }
        return AjaxResult.success(landInfo);
    }

    /**
     * 新增土地
     */
    @PostMapping
    public AjaxResult add(@RequestBody LandInfo landInfo) {
        AjaxResult validateResult = validateLandInfo(landInfo);
        if (validateResult != null) {
            return validateResult;
        }

        String landId = landInfoService.insertLandInfo(landInfo);
        Map<String, Object> result = new HashMap<>();
        result.put("id", landId);
        result.put("landId", landId);
        return AjaxResult.success("新增成功", result);
    }

    /**
     * 修改土地
     */
    @PostMapping("/{landId}")
    public AjaxResult edit(
            @PathVariable String landId,
            @RequestBody LandInfo landInfo) {
        AjaxResult validateResult = validateLandInfo(landInfo);
        if (validateResult != null) {
            return validateResult;
        }

        landInfo.setLandId(landId);

        // 校验土地是否存在
        LandInfo existLand = landInfoService.selectLandInfoByLandId(landId);
        if (existLand == null) {
            return AjaxResult.error("土地信息不存在");
        }

        int rows = landInfoService.updateLandInfo(landInfo);
        return toAjax(rows);
    }

    /**
     * 删除土地
     */
    @PostMapping("/{landId}/delete")
    public AjaxResult remove(
            @PathVariable String landId) {
        int rows = landInfoService.deleteLandInfoByLandId(landId);
        return toAjax(rows);
    }

    /**
     * 批量删除土地
     */
    @PostMapping("/batch/delete")
    public AjaxResult removeBatch(@RequestBody Map<String, String[]> params) {
        String[] landIds = params.get("landIds");
        if (landIds == null || landIds.length == 0) {
            return AjaxResult.error("请选择要删除的土地");
        }

        Map<String, Integer> result = landInfoService.deleteLandInfoByIds(landIds);
        return AjaxResult.success("删除完成", result);
    }

    /**
     * 关联农民
     */
    @PostMapping("/{landId}/bindFarmer")
    public AjaxResult bindFarmer(
            @PathVariable String landId,
            @RequestBody Map<String, String> params) {
        String farmerId = params.get("farmerId");
        if (farmerId == null || farmerId.trim().isEmpty()) {
            return AjaxResult.error("农民编码不能为空");
        }

        int rows = landInfoService.bindFarmer(landId, farmerId);
        return toAjax(rows);
    }

    /**
     * 解除农民关联
     */
    @PostMapping("/{landId}/unbindFarmer")
    public AjaxResult unbindFarmer(
            @PathVariable String landId) {
        int rows = landInfoService.unbindFarmer(landId);
        return toAjax(rows);
    }

    /**
     * 获取农民的土地列表
     */
    @GetMapping("/farmer/{farmerId}")
    public AjaxResult listByFarmer(
            @PathVariable String farmerId) {
        List<LandInfo> list = landInfoService.selectLandListByFarmerId(farmerId);
        return AjaxResult.success(list);
    }

    /**
     * 土地统计
     */
    @GetMapping("/statistics")
    public AjaxResult statistics(
            @RequestParam(required = false) String kebeleCode,
            @RequestParam(required = false) String woredaCode,
            @RequestParam(required = false) String zoneCode) {
        Map<String, Object> statistics = landInfoService.getLandStatistics(kebeleCode, woredaCode, zoneCode);
        return AjaxResult.success(statistics);
    }

    private AjaxResult validateLandInfo(LandInfo landInfo) {
        if (landInfo == null) {
            return AjaxResult.error("请求体不能为空");
        }
        if (isBlank(landInfo.getFarmerId())) {
            return AjaxResult.error("farmerId不能为空");
        }
        if (isBlank(landInfo.getKebeleId()) && isBlank(landInfo.getKebeleCode())) {
            return AjaxResult.error("kebeleId不能为空");
        }

        BigDecimal area = landInfo.getAreaTa() != null ? landInfo.getAreaTa() : landInfo.getAreaSize();
        if (area == null) {
            return AjaxResult.error("areaTa不能为空");
        }
        if (area.compareTo(BigDecimal.ZERO) <= 0) {
            return AjaxResult.error("areaTa必须大于0");
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
