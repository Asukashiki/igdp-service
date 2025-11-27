package com.inspur.seed.controller;

import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.BreedingMaterial;
import com.inspur.seed.service.IBreedingMaterialService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 育种材料登记控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/rest/system/breedingMaterial")
public class BreedingMaterialController extends BaseController {

    @Resource
    private IBreedingMaterialService breedingMaterialService;

    /**
     * 新增育种材料登记
     *
     * @param breedingMaterial 育种材料信息
     * @return 新增结果
     */
    @Log(title = "育种材料登记", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BreedingMaterial breedingMaterial) {
        try {
            Map<String, String> result = breedingMaterialService.addBreedingMaterial(breedingMaterial);
            return AjaxResult.success("操作成功", result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询育种材料列表
     *
     * @param batchId 育种批次ID
     * @param seedType 种子类别
     * @param receiveDateStart 接收日期起始
     * @param receiveDateEnd 接收日期结束
     * @return 查询结果
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String batchId,
            @RequestParam(required = false) String seedType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate receiveDateStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate receiveDateEnd) {
        // 使用RuoYi框架提供的分页方法
        startPage();
        
        List<BreedingMaterial> list = breedingMaterialService.queryBreedingMaterialList(batchId, seedType, receiveDateStart, receiveDateEnd);
        
        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }

    /**
     * 根据材料ID查询育种材料详情
     *
     * @param materialId 材料ID
     * @return 查询结果
     */
    @GetMapping("/info/{materialId}")
    public AjaxResult getInfo(@PathVariable String materialId) {
        BreedingMaterial breedingMaterial = breedingMaterialService.queryByMaterialId(materialId);
        if (breedingMaterial == null) {
            return AjaxResult.error("育种材料不存在");
        }
        return AjaxResult.success("查询成功", breedingMaterial);
    }

    /**
     * 编辑育种材料
     *
     * @param breedingMaterial 育种材料信息
     * @return 编辑结果
     */
    @Log(title = "育种材料登记", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody BreedingMaterial breedingMaterial) {
        try {
            boolean result = breedingMaterialService.editBreedingMaterial(breedingMaterial);
            if (result) {
                return AjaxResult.success("操作成功");
            } else {
                return AjaxResult.error("操作失败");
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除育种材料
     *
     * @param materialId 材料ID
     * @return 删除结果
     */
    @Log(title = "育种材料登记", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{materialId}")
    public AjaxResult remove(@PathVariable String materialId) {
        try {
            boolean result = breedingMaterialService.removeBreedingMaterial(materialId);
            if (result) {
                return AjaxResult.success("操作成功");
            } else {
                return AjaxResult.error("操作失败");
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}