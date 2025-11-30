package com.inspur.seed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.dto.BreedingTestAddDTO;
import com.inspur.seed.domain.dto.BreedingTestQueryDTO;
import com.inspur.seed.domain.dto.BreedingTestUpdateDTO;
import com.inspur.seed.domain.vo.BreedingTestVO;
import com.inspur.seed.service.IBreedingTestInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 繁殖检测信息控制器
 *
 * @author igdp
 * @date 2025-11-29
 */
@RestController
@RequestMapping("/seed/breeding/test")
public class BreedingTestInfoController extends BaseController {

    @Resource
    private IBreedingTestInfoService breedingTestInfoService;

    /**
     * 分页查询繁殖检测信息
     */
    @PostMapping("/page")
    public AjaxResult page(@Validated @RequestBody BreedingTestQueryDTO queryDTO) {
        try {
            IPage<BreedingTestVO> result = breedingTestInfoService.queryPage(queryDTO);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询繁殖检测信息列表（不分页）
     */
    @PostMapping("/list")
    public AjaxResult list(@Validated @RequestBody BreedingTestQueryDTO queryDTO) {
        try {
            List<BreedingTestVO> result = breedingTestInfoService.queryList(queryDTO);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增繁殖检测
     */
    @Log(title = "繁殖检测管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BreedingTestAddDTO addDTO) {
        try {
            String id = breedingTestInfoService.add(addDTO);
            return AjaxResult.success("新增成功", id);
        } catch (Exception e) {
            return AjaxResult.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改繁殖检测
     */
    @Log(title = "繁殖检测管理", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody BreedingTestUpdateDTO updateDTO) {
        try {
            boolean result = breedingTestInfoService.update(updateDTO);
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
     * 查询繁殖检测详情
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam(value = "id") String id) {
        try {
            BreedingTestVO result = breedingTestInfoService.detail(id);
            if (result == null) {
                return AjaxResult.error("繁殖检测不存在");
            }
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除繁殖检测
     */
    @Log(title = "繁殖检测管理", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody List<String> ids) {
        try {
            boolean result = breedingTestInfoService.delete(ids);
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
