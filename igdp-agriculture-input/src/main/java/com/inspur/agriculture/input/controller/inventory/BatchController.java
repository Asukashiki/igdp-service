package com.inspur.agriculture.input.controller.inventory;

import com.inspur.agriculture.input.service.inventory.IInventoryService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 批次查询 Controller
 *
 * @author inspur
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/inventory/batch")
public class BatchController {

    @Autowired
    private IInventoryService inventoryService;

    /**
     * 查询可用批次列表(用于出库选择)
     *
     * @param warehouseId 仓库ID
     * @param inputId     投入品ID
     * @return 可用批次列表
     */
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam Long warehouseId,
            @RequestParam Long inputId
    ) {
        try {
            List<Map<String, Object>> list = inventoryService.getAvailableBatchList(warehouseId, inputId);
            return AjaxResult.success(list);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
