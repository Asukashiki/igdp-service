package com.inspur.agriculture.input.controller.inventory;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.dto.inventory.WarehouseDTO;
import com.inspur.agriculture.input.dto.inventory.WarehouseQueryDTO;
import com.inspur.agriculture.input.service.inventory.IWarehouseService;
import com.inspur.agriculture.input.vo.inventory.WarehouseVO;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库管理 Controller
 *
 * @author inspur
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/inventory/warehouse")
public class WarehouseController {

    @Autowired
    private IWarehouseService warehouseService;

    /**
     * 查询仓库列表
     */
    @GetMapping("/list")
    public AjaxResult list(
            WarehouseQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        try {
            PageHelper.startPage(page, pageSize);
            List<WarehouseVO> list = warehouseService.getWarehouseList(queryDTO);
            PageInfo<WarehouseVO> pageInfo = new PageInfo<>(list);

            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("page", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询仓库详情
     */
    @GetMapping("/{warehouseId}")
    public AjaxResult getInfo(@PathVariable Long warehouseId) {
        try {
            WarehouseVO warehouse = warehouseService.getWarehouseById(warehouseId);
            if (warehouse == null) {
                return AjaxResult.error("仓库不存在");
            }
            return AjaxResult.success(warehouse);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 添加仓库
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody WarehouseDTO dto) {
        try {
            int rows = warehouseService.addWarehouse(dto);
            if (rows > 0) {
                return AjaxResult.success("添加成功");
            }
            return AjaxResult.error("添加失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 更新仓库
     */
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody WarehouseDTO dto) {
        try {
            int rows = warehouseService.updateWarehouse(dto);
            if (rows > 0) {
                return AjaxResult.success("更新成功");
            }
            return AjaxResult.error("更新失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除仓库
     */
    @PostMapping("/delete/{warehouseId}")
    public AjaxResult delete(@PathVariable Long warehouseId) {
        try {
            int rows = warehouseService.deleteWarehouse(warehouseId);
            if (rows > 0) {
                return AjaxResult.success("删除成功");
            }
            return AjaxResult.error("删除失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
