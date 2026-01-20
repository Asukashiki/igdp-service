package com.inspur.seed.multiplication.basic.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.multiplication.basic.domain.dto.BreedingBatchAddDTO;
import com.inspur.seed.multiplication.basic.domain.dto.BreedingBatchQueryDTO;
import com.inspur.seed.multiplication.basic.domain.dto.BreedingBatchUpdateDTO;
import com.inspur.seed.multiplication.basic.domain.vo.BreedingBatchVO;
import com.inspur.seed.multiplication.basic.service.IBreedingBatchInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 繁殖批次信息控制器
 *
 * @author igdp
 * @date 2025-11-29
 */
@RestController
@RequestMapping("/seed/breeding/batch")
public class BreedingBatchInfoController extends BaseController {

    @Resource
    private IBreedingBatchInfoService breedingBatchInfoService;

    /**
     * 分页查询繁殖批次信息
     */
    @PostMapping("/page")
    public AjaxResult page(@Validated @RequestBody BreedingBatchQueryDTO queryDTO) {
        try {
            IPage<BreedingBatchVO> result = breedingBatchInfoService.queryPage(queryDTO);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询繁殖批次信息列表（不分页）
     */
    @PostMapping("/list")
    public AjaxResult list(@Validated @RequestBody BreedingBatchQueryDTO queryDTO) {
        try {
            List<BreedingBatchVO> result = breedingBatchInfoService.queryList(queryDTO);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增繁殖批次
     */
    @Log(title = "繁殖批次管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BreedingBatchAddDTO addDTO) {
        try {
            String id = breedingBatchInfoService.add(addDTO);
            return AjaxResult.success("新增成功", id);
        } catch (Exception e) {
            return AjaxResult.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改繁殖批次
     */
    @Log(title = "繁殖批次管理", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody BreedingBatchUpdateDTO updateDTO) {
        try {
            boolean result = breedingBatchInfoService.update(updateDTO);
            if (result) {
                return AjaxResult.success("修改成功");
            } else {
                return AjaxResult.error("修改失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 查询繁殖批次详情
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam(value = "id") String id) {
        try {
            BreedingBatchVO result = breedingBatchInfoService.detail(id);
            if (result == null) {
                return AjaxResult.error("繁殖批次不存在");
            }
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除繁殖批次
     */
    @Log(title = "繁殖批次管理", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody List<String> ids) {
        try {
            boolean result = breedingBatchInfoService.delete(ids);
            if (result) {
                return AjaxResult.success("删除成功");
            } else {
                return AjaxResult.error("删除失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }
}
