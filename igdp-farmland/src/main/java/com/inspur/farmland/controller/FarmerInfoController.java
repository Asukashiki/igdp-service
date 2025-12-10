package com.inspur.farmland.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.utils.poi.ExcelUtil;
import com.inspur.farmland.domain.FarmerInfo;
import com.inspur.farmland.service.IFarmerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 农民管理Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/farmland/farmer")
public class FarmerInfoController extends BaseController {

    @Autowired
    private IFarmerInfoService farmerInfoService;

    /**
     * 获取农民分页列表
     */
    @GetMapping("/page")
    public TableDataInfo page(FarmerInfo farmerInfo) {
        startPage();
        List<FarmerInfo> list = farmerInfoService.selectFarmerInfoList(farmerInfo);
        return getDataTable(list);
    }

    /**
     * 获取农民详情
     */
    @GetMapping("/{farmerId}")
    public AjaxResult getInfo(
            @PathVariable String farmerId) {
        FarmerInfo farmerInfo = farmerInfoService.selectFarmerInfoByFarmerId(farmerId);
        if (farmerInfo == null) {
            return AjaxResult.error("农民信息不存在");
        }
        return AjaxResult.success(farmerInfo);
    }

    /**
     * 新增农民
     */
    @PostMapping
    public AjaxResult add(@RequestBody FarmerInfo farmerInfo) {
        // 校验身份证号唯一性
        if (!farmerInfoService.checkIdCardUnique(farmerInfo.getIdCard(), null)) {
            return AjaxResult.error("身份证号已存在");
        }

        String farmerId = farmerInfoService.insertFarmerInfo(farmerInfo);
        Map<String, Object> result = new HashMap<>();
        result.put("farmerId", farmerId);
        return AjaxResult.success("新增成功", result);
    }

    /**
     * 修改农民
     */
    @PostMapping("/{farmerId}")
    public AjaxResult edit(
            @PathVariable String farmerId,
            @RequestBody FarmerInfo farmerInfo) {
        farmerInfo.setFarmerId(farmerId);

        // 校验农民是否存在
        FarmerInfo existFarmer = farmerInfoService.selectFarmerInfoByFarmerId(farmerId);
        if (existFarmer == null) {
            return AjaxResult.error("农民信息不存在");
        }

        int rows = farmerInfoService.updateFarmerInfo(farmerInfo);
        return toAjax(rows);
    }

    /**
     * 删除农民
     */
    @PostMapping("/{farmerId}/delete")
    public AjaxResult remove(
            @PathVariable String farmerId) {
        int rows = farmerInfoService.deleteFarmerInfoByFarmerId(farmerId);
        return toAjax(rows);
    }

    /**
     * 批量删除农民
     */
    @PostMapping("/batch/delete")
    public AjaxResult removeBatch(@RequestBody Map<String, String[]> params) {
        String[] farmerIds = params.get("farmerIds");
        if (farmerIds == null || farmerIds.length == 0) {
            return AjaxResult.error("请选择要删除的农民");
        }

        Map<String, Integer> result = farmerInfoService.deleteFarmerInfoByIds(farmerIds);
        return AjaxResult.success("删除完成", result);
    }

    /**
     * 获取农民下拉选项
     */
    @GetMapping("/options")
    public AjaxResult options(
            @RequestParam(required = false) String kebeleCode,
            @RequestParam(required = false) String keyword) {
        List<Map<String, Object>> options = farmerInfoService.selectFarmerOptions(kebeleCode, keyword);
        return AjaxResult.success(options);
    }

    /**
     * 下载导入模板
     */
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<FarmerInfo> util = new ExcelUtil<>(FarmerInfo.class);
        util.importTemplateExcel(response, "Farmer Import Template");
    }

    /**
     * 导入农民数据
     */
    @PostMapping("/import")
    public AjaxResult importData(@RequestParam("file") MultipartFile file,
                                 @RequestParam(value = "updateSupport", defaultValue = "false") boolean updateSupport) {
        try {
            ExcelUtil<FarmerInfo> util = new ExcelUtil<>(FarmerInfo.class);
            List<FarmerInfo> farmerList = util.importExcel(file.getInputStream());

            if (farmerList == null || farmerList.isEmpty()) {
                return AjaxResult.error("导入数据为空");
            }

            Map<String, Object> result = farmerInfoService.importFarmerData(farmerList, updateSupport);
            return AjaxResult.success("导入完成", result);
        } catch (Exception e) {
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }

    @GetMapping("/getAllFarmerList")
    public AjaxResult getAllFarmerList() {
        FarmerInfo farmerInfo = new FarmerInfo();
        farmerInfo.setStatus("1");
        List<FarmerInfo> list = farmerInfoService.selectFarmerInfoList(farmerInfo);
        return AjaxResult.success(list);
    }
}
