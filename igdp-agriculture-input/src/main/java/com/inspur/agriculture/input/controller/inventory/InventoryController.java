//package com.inspur.agriculture.input.controller.inventory;
//
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.inspur.agriculture.input.dto.inventory.InventoryQueryDTO;
//import com.inspur.agriculture.input.service.inventory.IInventoryService;
//import com.inspur.agriculture.input.vo.inventory.InventoryVO;
//import com.inspur.common.core.domain.AjaxResult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 库存查询 Controller
// *
// * @author inspur
// * @date 2025-11-26
// */
//@RestController
//@RequestMapping("/inventory/stock")
//public class InventoryController {
//
//    @Autowired
//    private IInventoryService inventoryService;
//
//    /**
//     * 查询库存列表
//     */
//    @GetMapping("/list")
//    public AjaxResult list(
//            InventoryQueryDTO queryDTO,
//            @RequestParam(defaultValue = "1") Integer page,
//            @RequestParam(defaultValue = "10") Integer pageSize
//    ) {
//        try {
//            PageHelper.startPage(page, pageSize);
//            List<InventoryVO> list = inventoryService.getInventoryList(queryDTO);
//            PageInfo<InventoryVO> pageInfo = new PageInfo<>(list);
//
//            // 统计汇总
//            Map<String, Object> summary = new HashMap<>();
//            int totalQuantity = 0;
//            int normalCount = 0;
//            int nearExpiryCount = 0;
//            int expiredCount = 0;
//
//            for (InventoryVO vo : pageInfo.getList()) {
//                totalQuantity += vo.getCurrentQuantity();
//                if ("0".equals(vo.getStockStatus())) {
//                    normalCount++;
//                } else if ("1".equals(vo.getStockStatus())) {
//                    nearExpiryCount++;
//                } else if ("2".equals(vo.getStockStatus())) {
//                    expiredCount++;
//                }
//            }
//
//            summary.put("totalQuantity", totalQuantity);
//            summary.put("normalCount", normalCount);
//            summary.put("nearExpiryCount", nearExpiryCount);
//            summary.put("expiredCount", expiredCount);
//
//            Map<String, Object> result = new HashMap<>();
//            result.put("list", pageInfo.getList());
//            result.put("total", pageInfo.getTotal());
//            result.put("page", pageInfo.getPageNum());
//            result.put("pageSize", pageInfo.getPageSize());
//            result.put("summary", summary);
//
//            return AjaxResult.success(result);
//        } catch (Exception e) {
//            return AjaxResult.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 查询库存详情
//     */
//    @GetMapping("/{inventoryId}")
//    public AjaxResult getInfo(@PathVariable String inventoryId) {
//        try {
//            InventoryVO inventory = inventoryService.getInventoryById(inventoryId);
//            if (inventory == null) {
//                return AjaxResult.error("库存记录不存在");
//            }
//            return AjaxResult.success(inventory);
//        } catch (Exception e) {
//            return AjaxResult.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 查询库存预警列表
//     */
//    @GetMapping("/warning")
//    public AjaxResult warningList(
//            @RequestParam(required = false) Long warehouseId,
//            @RequestParam(defaultValue = "all") String warningType,
//            @RequestParam(defaultValue = "1") Integer page,
//            @RequestParam(defaultValue = "10") Integer pageSize
//    ) {
//        try {
//            PageHelper.startPage(page, pageSize);
//            List<InventoryVO> list = inventoryService.getWarningList(warehouseId, warningType);
//            PageInfo<InventoryVO> pageInfo = new PageInfo<>(list);
//
//            // 统计汇总
//            Map<String, Object> summary = new HashMap<>();
//            int nearExpiryCount = 0;
//            int expiredCount = 0;
//
//            for (InventoryVO vo : pageInfo.getList()) {
//                if ("1".equals(vo.getStockStatus())) {
//                    nearExpiryCount++;
//                } else if ("2".equals(vo.getStockStatus())) {
//                    expiredCount++;
//                }
//            }
//
//            summary.put("nearExpiryCount", nearExpiryCount);
//            summary.put("expiredCount", expiredCount);
//
//            Map<String, Object> result = new HashMap<>();
//            result.put("list", pageInfo.getList());
//            result.put("total", pageInfo.getTotal());
//            result.put("page", pageInfo.getPageNum());
//            result.put("pageSize", pageInfo.getPageSize());
//            result.put("summary", summary);
//
//            return AjaxResult.success(result);
//        } catch (Exception e) {
//            return AjaxResult.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 按投入品汇总库存
//     */
//    @GetMapping("/summary/by-input")
//    public AjaxResult summaryByInput(
//            @RequestParam(required = false) Long warehouseId,
//            @RequestParam(required = false) String inputType
//    ) {
//        try {
//            List<Map<String, Object>> list = inventoryService.getSummaryByInput(warehouseId, inputType);
//            return AjaxResult.success(list);
//        } catch (Exception e) {
//            return AjaxResult.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 按仓库汇总库存
//     */
//    @GetMapping("/summary/by-warehouse")
//    public AjaxResult summaryByWarehouse(
//            @RequestParam(required = false) Long supplierId
//    ) {
//        try {
//            List<Map<String, Object>> list = inventoryService.getSummaryByWarehouse(supplierId);
//            return AjaxResult.success(list);
//        } catch (Exception e) {
//            return AjaxResult.error(e.getMessage());
//        }
//    }
//}
