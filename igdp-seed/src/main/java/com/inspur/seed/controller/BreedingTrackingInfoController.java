package com.inspur.seed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.dto.BreedingTrackingAddDTO;
import com.inspur.seed.domain.dto.BreedingTrackingQueryDTO;
import com.inspur.seed.domain.dto.BreedingTrackingUpdateDTO;
import com.inspur.seed.domain.vo.BreedingTrackingVO;
import com.inspur.seed.service.IBreedingTrackingInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 繁殖跟踪信息控制器
 *
 * @author igdp
 * @date 2025-11-29
 */
@RestController
@RequestMapping("/seed/breeding/tracking")
public class BreedingTrackingInfoController extends BaseController {

    @Resource
    private IBreedingTrackingInfoService breedingTrackingInfoService;

    /**
     * 分页查询繁殖跟踪信息
     */
    @PostMapping("/page")
    public AjaxResult page(@Validated @RequestBody BreedingTrackingQueryDTO queryDTO) {
        try {
            IPage<BreedingTrackingVO> result = breedingTrackingInfoService.queryPage(queryDTO);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询繁殖跟踪信息列表（不分页）
     */
    @PostMapping("/list")
    public AjaxResult list(@Validated @RequestBody BreedingTrackingQueryDTO queryDTO) {
        try {
            List<BreedingTrackingVO> result = breedingTrackingInfoService.queryList(queryDTO);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增繁殖跟踪
     */
    @Log(title = "繁殖跟踪管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BreedingTrackingAddDTO addDTO) {
        try {
            String id = breedingTrackingInfoService.add(addDTO);
            return AjaxResult.success("新增成功", id);
        } catch (Exception e) {
            return AjaxResult.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改繁殖跟踪
     */
    @Log(title = "繁殖跟踪管理", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody BreedingTrackingUpdateDTO updateDTO) {
        try {
            boolean result = breedingTrackingInfoService.update(updateDTO);
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
     * 查询繁殖跟踪详情
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam(value = "id") String id) {
        try {
            BreedingTrackingVO result = breedingTrackingInfoService.detail(id);
            if (result == null) {
                return AjaxResult.error("繁殖跟踪不存在");
            }
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除繁殖跟踪
     */
    @Log(title = "繁殖跟踪管理", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody List<String> ids) {
        try {
            boolean result = breedingTrackingInfoService.delete(ids);
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
