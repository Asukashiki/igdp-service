package com.inspur.agriculture.inventory.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.inventory.domain.req.StockCheckCreateReq;
import com.inspur.agriculture.inventory.domain.req.StockCheckListQuery;
import com.inspur.agriculture.inventory.domain.req.StockCheckReviewReq;
import com.inspur.agriculture.inventory.domain.req.StockCheckUpdateReq;
import com.inspur.agriculture.inventory.domain.vo.StockCheckDetailVO;
import com.inspur.agriculture.inventory.domain.vo.StockCheckListVO;
import com.inspur.agriculture.inventory.domain.vo.WarehouseInventoryItemVO;
import com.inspur.agriculture.inventory.service.IStockCheckService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存盘点 Controller
 */
@RestController
@RequestMapping("/inventory/stock-check")
public class StockCheckController extends BaseController {

    @Autowired
    private IStockCheckService stockCheckService;

    /**
     * 1. 获取盘点单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(StockCheckListQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<StockCheckListVO> list = stockCheckService.getList(query);
        PageInfo<StockCheckListVO> pageInfo = new PageInfo<>(list);

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(pageInfo.getTotal());
        return rspData;
    }

    /**
     * 2. 获取盘点单详情
     */
    @GetMapping("/{checkId}")
    public AjaxResult getInfo(@PathVariable("checkId") String checkId) {
        StockCheckDetailVO detail = stockCheckService.getDetail(checkId);
        return AjaxResult.success(detail);
    }

    /**
     * 3. 新建盘点单
     */
    @PostMapping("/create")
    public AjaxResult add(@Validated @RequestBody StockCheckCreateReq req) {
        String checkId = stockCheckService.createStockCheck(req);
        Map<String, String> data = new HashMap<>();
        data.put("checkId", checkId);
        return AjaxResult.success("盘点单创建成功", data);
    }

    /**
     * 4. 编辑盘点单
     */
    @PutMapping("/{checkId}")
    public AjaxResult edit(@PathVariable("checkId") String checkId, @Validated @RequestBody StockCheckUpdateReq req) {
        stockCheckService.updateStockCheck(checkId, req);
        return AjaxResult.success("更新成功");
    }

    /**
     * 5. 删除盘点单
     */
    @DeleteMapping("/{checkId}")
    public AjaxResult remove(@PathVariable("checkId") String checkId) {
        stockCheckService.deleteStockCheck(checkId);
        return AjaxResult.success("删除成功");
    }

    /**
     * 6. 提交盘点单
     */
    @PostMapping("/{checkId}/submit")
    public AjaxResult submit(@PathVariable("checkId") String checkId) {
        stockCheckService.submitStockCheck(checkId);
        return AjaxResult.success("提交成功");
    }

    /**
     * 7. 取消盘点单
     */
    @PostMapping("/{checkId}/cancel")
    public AjaxResult cancel(@PathVariable("checkId") String checkId) {
        stockCheckService.cancelStockCheck(checkId);
        return AjaxResult.success("取消成功");
    }

    /**
     * 8. 审核通过
     */
    @PostMapping("/{checkId}/approve")
    public AjaxResult approve(@PathVariable("checkId") String checkId,
            @Validated @RequestBody StockCheckReviewReq req) {
        stockCheckService.approveStockCheck(checkId, req);

        // 按照文档，需返回包含 adjustements 的数据等，这里简化回显：
        Map<String, Object> data = new HashMap<>();
        data.put("checkId", checkId);
        data.put("checkStatus", "ADJUSTED");
        return AjaxResult.success("审核通过，库存已自动调整", data);
    }

    /**
     * 9. 审核驳回
     */
    @PostMapping("/{checkId}/reject")
    public AjaxResult reject(@PathVariable("checkId") String checkId, @Validated @RequestBody StockCheckReviewReq req) {
        stockCheckService.rejectStockCheck(checkId, req);
        return AjaxResult.success("已驳回");
    }

    /**
     * 10. 获取仓库当前库存
     */
    @GetMapping("/warehouse-inventory")
    public AjaxResult warehouseInventory(@RequestParam("warehouseId") String warehouseId) {
        List<WarehouseInventoryItemVO> items = stockCheckService.getWarehouseInventory(warehouseId);
        Map<String, Object> data = new HashMap<>();
        data.put("warehouseId", warehouseId);
        data.put("items", items);
        return AjaxResult.success(data);
    }

    /**
     * 11. 获取仓库盘点状态
     */
    @GetMapping("/warehouse-status")
    public AjaxResult warehouseStatus(@RequestParam("warehouseId") String warehouseId) {
        boolean isChecking = stockCheckService.isWarehouseChecking(warehouseId);
        Map<String, Object> data = new HashMap<>();
        data.put("warehouseId", warehouseId);
        data.put("isChecking", isChecking);
        if (isChecking) {
            data.put("message", "该仓库正在盘点中，建议盘点完成后再操作");
        } else {
            data.put("message", "仓库状态正常");
        }
        return AjaxResult.success(data);
    }
}
