package com.inspur.agriculture.input.controller.supplier;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.dto.supplier.SupplierProductDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierProductQueryDTO;
import com.inspur.agriculture.input.service.supplier.ISupplierProductService;
import com.inspur.agriculture.input.vo.supplier.SupplierProductVO;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商投入品Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/supplier/product")
public class SupplierProductController {

    @Autowired
    private ISupplierProductService supplierProductService;

    /**
     * 查询供应商投入品列表（分页）
     *
     * @param queryDTO 查询条件
     * @param page     页码
     * @param pageSize 每页数量
     * @return 供应商投入品列表
     */
    @GetMapping("/list")
    public AjaxResult list(
            SupplierProductQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        try {
            // 启动分页
            PageHelper.startPage(page, pageSize);
            List<SupplierProductVO> list = supplierProductService.getSupplierProductList(queryDTO);
            PageInfo<SupplierProductVO> pageInfo = new PageInfo<>(list);

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
     * 查询供应商投入品详情
     *
     * @param supplierProductId 供应关系ID
     * @return 供应商投入品详情
     */
    @GetMapping("/{supplierProductId}")
    public AjaxResult getInfo(@PathVariable("supplierProductId") Long supplierProductId) {
        try {
            SupplierProductVO vo = supplierProductService.getSupplierProductById(supplierProductId);
            if (vo == null) {
                return AjaxResult.error("供应商投入品关系不存在");
            }
            return AjaxResult.success(vo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 添加供应商投入品关系
     *
     * @param dto 供应商投入品数据
     * @return 操作结果
     */
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SupplierProductDTO dto) {
        try {
            int rows = supplierProductService.addSupplierProduct(dto);
            if (rows > 0) {
                return AjaxResult.success("添加成功");
            }
            return AjaxResult.error("添加失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 更新供应商投入品关系
     *
     * @param supplierProductId 供应关系ID
     * @param dto               供应商投入品数据
     * @return 操作结果
     */
    @PutMapping("/{supplierProductId}")
    public AjaxResult update(
            @PathVariable("supplierProductId") Long supplierProductId,
            @Validated @RequestBody SupplierProductDTO dto
    ) {
        try {
            dto.setSupplierProductId(supplierProductId);
            int rows = supplierProductService.updateSupplierProduct(dto);
            if (rows > 0) {
                return AjaxResult.success("更新成功");
            }
            return AjaxResult.error("更新失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除供应商投入品关系
     *
     * @param supplierProductId 供应关系ID
     * @return 操作结果
     */
    @DeleteMapping("/{supplierProductId}")
    public AjaxResult delete(@PathVariable("supplierProductId") Long supplierProductId) {
        try {
            int rows = supplierProductService.deleteSupplierProduct(supplierProductId);
            if (rows > 0) {
                return AjaxResult.success("删除成功");
            }
            return AjaxResult.error("删除失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 批量删除供应商投入品关系
     *
     * @param supplierProductIds 供应关系ID数组
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public AjaxResult batchDelete(@RequestBody Long[] supplierProductIds) {
        try {
            if (supplierProductIds == null || supplierProductIds.length == 0) {
                return AjaxResult.error("请选择要删除的记录");
            }
            int rows = supplierProductService.batchDeleteSupplierProduct(supplierProductIds);
            if (rows > 0) {
                return AjaxResult.success("批量删除成功");
            }
            return AjaxResult.error("批量删除失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
