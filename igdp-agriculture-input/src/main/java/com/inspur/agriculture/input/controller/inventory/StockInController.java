package com.inspur.agriculture.input.controller.inventory;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.dto.inventory.StockInDTO;
import com.inspur.agriculture.input.dto.inventory.StockInQueryDTO;
import com.inspur.agriculture.input.service.inventory.IStockInService;
import com.inspur.agriculture.input.vo.inventory.StockInVO;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 入库管理 Controller
 *
 * @author inspur
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/inventory/stock-in")
public class StockInController {

    @Autowired
    private IStockInService stockInService;

    /**
     * 查询入库单列表
     */
    @GetMapping("/list")
    public AjaxResult list(
            StockInQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        try {
            PageHelper.startPage(page, pageSize);
            List<StockInVO> list = stockInService.getStockInList(queryDTO);
            PageInfo<StockInVO> pageInfo = new PageInfo<>(list);

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
     * 查询入库单详情
     */
    @GetMapping("/{stockInId}")
    public AjaxResult getInfo(@PathVariable String stockInId) {
        try {
            StockInVO stockIn = stockInService.getStockInById(stockInId);
            if (stockIn == null) {
                return AjaxResult.error("入库单不存在");
            }
            return AjaxResult.success(stockIn);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 创建入库单
     */
    @PostMapping
    public AjaxResult create(@Validated @RequestBody StockInDTO dto) {
        try {
            String stockInId = stockInService.createStockIn(dto);
            Map<String, Object> result = new HashMap<>();
            result.put("stockInId", stockInId);
            result.put("status", "0");
            result.put("statusDesc", "未入库");
            return AjaxResult.success("创建成功", result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 确认入库
     */
    @PostMapping("/{stockInId}/confirm")
    public AjaxResult confirm(@PathVariable String stockInId) {
        try {
            int rows = stockInService.confirmStockIn(stockInId);
            if (rows > 0) {
                return AjaxResult.success("入库确认成功");
            }
            return AjaxResult.error("入库确认失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
