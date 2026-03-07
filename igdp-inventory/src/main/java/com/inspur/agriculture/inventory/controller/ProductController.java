package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryProduct;
import com.inspur.agriculture.inventory.service.IInventoryProductService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品管理 Controller
 */
@RestController("inventoryProductManageController")
@RequestMapping("/inventory/product-manage")
public class ProductController extends BaseController {

    @Autowired
    private IInventoryProductService productService;

    @PreAuthorize("@ss.hasPermi('inventory:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(InventoryProduct product) {
        startPage();
        List<InventoryProduct> list = productService.selectProductList(product);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('inventory:product:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(productService.selectProductById(id));
    }

    @PreAuthorize("@ss.hasPermi('inventory:product:add')")
    @PostMapping
    public AjaxResult add(@RequestBody InventoryProduct product) {
        return toAjax(productService.createProduct(product));
    }

    @PreAuthorize("@ss.hasPermi('inventory:product:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody InventoryProduct product) {
        return toAjax(productService.updateProduct(product));
    }

    @PreAuthorize("@ss.hasPermi('inventory:product:remove')")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(productService.deleteProduct(id));
    }
}
