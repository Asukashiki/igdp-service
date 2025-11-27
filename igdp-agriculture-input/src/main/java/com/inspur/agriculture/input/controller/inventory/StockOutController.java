package com.inspur.agriculture.input.controller.inventory;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.dto.inventory.StockOutDTO;
import com.inspur.agriculture.input.dto.inventory.StockOutQueryDTO;
import com.inspur.agriculture.input.service.inventory.IStockOutService;
import com.inspur.agriculture.input.vo.inventory.StockOutVO;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库管理 Controller
 *
 * @author inspur
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/inventory/stock-out")
public class StockOutController {

    @Autowired
    private IStockOutService stockOutService;

    /**
     * 查询出库单列表
     */
    @GetMapping("/list")
    public AjaxResult list(
            StockOutQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        try {
            PageHelper.startPage(page, pageSize);
            List<StockOutVO> list = stockOutService.getStockOutList(queryDTO);
            PageInfo<StockOutVO> pageInfo = new PageInfo<>(list);

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
     * 查询出库单详情
     */
    @GetMapping("/{stockOutId}")
    public AjaxResult getInfo(@PathVariable String stockOutId) {
        try {
            StockOutVO stockOut = stockOutService.getStockOutById(stockOutId);
            if (stockOut == null) {
                return AjaxResult.error("出库单不存在");
            }
            return AjaxResult.success(stockOut);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 创建出库单
     */
    @PostMapping
    public AjaxResult create(@Validated @RequestBody StockOutDTO dto) {
        try {
            String stockOutId = stockOutService.createStockOut(dto);
            Map<String, Object> result = new HashMap<>();
            result.put("stockOutId", stockOutId);
            result.put("status", "0");
            result.put("statusDesc", "未出库");
            return AjaxResult.success("创建成功", result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 确认出库
     */
    @PostMapping("/{stockOutId}/confirm")
    public AjaxResult confirm(@PathVariable String stockOutId) {
        try {
            int rows = stockOutService.confirmStockOut(stockOutId);
            if (rows > 0) {
                return AjaxResult.success("出库确认成功");
            }
            return AjaxResult.error("出库确认失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
