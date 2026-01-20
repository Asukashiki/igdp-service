package com.inspur.agriculture.input.controller.inventory;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.dto.inventory.InboundAuditDTO;
import com.inspur.agriculture.input.dto.inventory.InboundConfirmDTO;
import com.inspur.agriculture.input.dto.inventory.InboundOrderDTO;
import com.inspur.agriculture.input.dto.inventory.InboundOrderQueryDTO;
import com.inspur.agriculture.input.service.inventory.IInboundOrderService;
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
 * @date 2025-12-03
 */
@RestController
@RequestMapping("/inventory/inbound/orders")
public class InboundOrderController {

    @Autowired
    private IInboundOrderService inboundOrderService;

    /**
     * 查询入库单列表
     *
     * @param queryDTO 查询条件
     * @param page 页码
     * @param pageSize 每页数量
     * @return 入库单列表
     */
    @GetMapping
    public AjaxResult list(
            InboundOrderQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        try {
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (queryDTO.getInboundStatus() != null && !"all".equals(queryDTO.getInboundStatus())) {
                params.put("inboundStatus", queryDTO.getInboundStatus());
            }
            if (queryDTO.getInboundType() != null && !"all".equals(queryDTO.getInboundType())) {
                params.put("inboundType", queryDTO.getInboundType());
            }
            if (queryDTO.getInboundOrderId() != null && !queryDTO.getInboundOrderId().isEmpty()) {
                params.put("inboundOrderId", queryDTO.getInboundOrderId());
            }
            if (queryDTO.getOrganCode() != null && !queryDTO.getOrganCode().isEmpty()) {
                params.put("organCode", queryDTO.getOrganCode());
            }

            // 开启分页
            PageHelper.startPage(page, pageSize);
            List<Map<String, Object>> list = inboundOrderService.selectInboundOrderList(params);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("total", pageInfo.getTotal());
            result.put("page", pageInfo.getPageNum());
            result.put("page_size", pageInfo.getPageSize());
            result.put("items", pageInfo.getList());

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询入库单列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取入库单详情
     *
     * @param inboundOrderId 入库单ID
     * @return 入库单详情
     */
    @GetMapping("/{inboundOrderId}")
    public AjaxResult getInfo(@PathVariable String inboundOrderId) {
        try {
            Map<String, Object> inboundOrder = inboundOrderService.selectInboundOrderById(inboundOrderId);
            if (inboundOrder == null) {
                return AjaxResult.error("入库单不存在");
            }
            return AjaxResult.success(inboundOrder);
        } catch (Exception e) {
            return AjaxResult.error("查询入库单详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建入库单
     *
     * @param dto 入库单创建信息
     * @return 创建结果
     */
    @PostMapping
    public AjaxResult create(@Validated @RequestBody InboundOrderDTO dto) {
        try {
            // 构建入库单对象
            com.inspur.agriculture.input.domain.inventory.InboundOrder inboundOrder =
                new com.inspur.agriculture.input.domain.inventory.InboundOrder();
            inboundOrder.setInboundType(dto.getInboundType());
            inboundOrder.setWarehouseId(dto.getWarehouseId());
            inboundOrder.setRelatedOrderNo(dto.getRelatedOrderNo());
            inboundOrder.setSupplierType(dto.getSupplierType());
            inboundOrder.setSupplierId(dto.getSupplierId());
            inboundOrder.setInboundUser(dto.getInboundUser());
            inboundOrder.setApplyTime(dto.getApplyTime());
            inboundOrder.setOperator(dto.getOperator());
            inboundOrder.setRemark(dto.getRemark());
            inboundOrder.setFormRemark(dto.getRemark()); // 同时设置表单备注字段
            inboundOrder.setSupplierName(dto.getSupplierName());
            inboundOrder.setSupplierContact(dto.getSupplierContact());
            inboundOrder.setSupplierPhone(dto.getSupplierPhone());

            // 构建明细列表
            List<Map<String, Object>> details = new java.util.ArrayList<>();
            for (InboundOrderDTO.InboundDetail detail : dto.getDetails()) {
                Map<String, Object> detailMap = new HashMap<>();
                detailMap.put("materialId", detail.getMaterialId());
                detailMap.put("materialType", detail.getMaterialType());
                detailMap.put("materialName",detail.getMaterialName());
                detailMap.put("materialBatchId",detail.getBatchNo());
                detailMap.put("batchNo",detail.getBatchNo());
                detailMap.put("quantity", detail.getQuantity());
                detailMap.put("specModel", detail.getSpecModel());
                detailMap.put("unitOfMeasure", detail.getUnitOfMeasure());
                detailMap.put("expiryDate", detail.getExpiryDate());
                detailMap.put("agriculturalInputType", detail.getAgriculturalInputType());
                detailMap.put("variety", detail.getVariety());
                detailMap.put("productionBatchNo", detail.getProductionBatchNo());
                details.add(detailMap);
            }

            // 创建入库单
            String inboundOrderId = inboundOrderService.createInboundOrder(inboundOrder, details);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("inbound_order_id", inboundOrderId);
            // 假设批次号在创建时生成，这里需要从返回值中获取
            // result.put("inbound_batch_id", inboundBatchId);

            return AjaxResult.success("创建成功", result);
        } catch (Exception e) {
            return AjaxResult.error("创建入库单失败: " + e.getMessage());
        }
    }

    /**
     * 审核入库单
     *
     * @param inboundOrderId 入库单ID
     * @param dto 审核信息
     * @return 审核结果
     */
    @PutMapping("/{inboundOrderId}/audit")
    public AjaxResult audit(
            @PathVariable String inboundOrderId,
            @Validated @RequestBody InboundAuditDTO dto
    ) {
        try {
            boolean success = inboundOrderService.auditInboundOrder(
                    inboundOrderId,
                    dto.getAuditStatus(),
                    dto.getAuditUser(),
                    dto.getAuditTime(),
                    dto.getRemark()
            );

            if (success) {
                return AjaxResult.success("审核成功");
            } else {
                return AjaxResult.error("审核失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("审核入库单失败: " + e.getMessage());
        }
    }

    /**
     * 执行入库
     * 业务逻辑：
     * 1. 校验仓库是否存在和容量充足
     * 2. 校验投入品存在性
     * 3. 自动生成批次号和二维码
     * 4. 更新库存
     * 5. 更新入库单状态为"已入库"
     * 6. 记录库存变动日志
     *
     * @param inboundOrderId 入库单ID
     * @param dto 入库确认信息
     * @return 入库结果
     */
    @PutMapping("/{inboundOrderId}/confirm")
    public AjaxResult confirm(
            @PathVariable String inboundOrderId,
            @Validated @RequestBody InboundConfirmDTO dto
    ) {
        try {
            Map<String, Object> result = inboundOrderService.confirmInbound(
                    inboundOrderId,
                    dto.getInboundTime(),
                    dto.getOperator()
            );

            return AjaxResult.success("Successful entry into the warehouse", result);
        } catch (Exception e) {
            return AjaxResult.error("The entry into the warehouse failed: " + e.getMessage());
        }
    }

    /**
     * 取消入库单
     *
     * @param inboundOrderId 入库单ID
     * @param operator 操作人
     * @return 取消结果
     */
    @PutMapping("/{inboundOrderId}/cancel")
    public AjaxResult cancel(
            @PathVariable String inboundOrderId,
            @RequestParam String operator
    ) {
        try {
            boolean success = inboundOrderService.cancelInboundOrder(inboundOrderId, operator);
            if (success) {
                return AjaxResult.success("取消成功");
            } else {
                return AjaxResult.error("取消失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("取消入库单失败: " + e.getMessage());
        }
    }

    /**
     * 统计入库单数量
     *
     * @param warehouseId 仓库ID（可选）
     * @return 统计结果
     */
    @GetMapping("/count")
    public AjaxResult count(@RequestParam(required = false) String warehouseId) {
        try {
            Map<String, Object> params = new HashMap<>();
            if (warehouseId != null && !warehouseId.isEmpty()) {
                params.put("warehouseId", warehouseId);
            }

            int count = inboundOrderService.countInboundOrders(params);
            return AjaxResult.success(count);
        } catch (Exception e) {
            return AjaxResult.error("统计入库单数量失败: " + e.getMessage());
        }
    }

    /**
     * 查询待审核入库单数量
     *
     * @param warehouseId 仓库ID（可选）
     * @return 待审核数量
     */
    @GetMapping("/pending/count")
    public AjaxResult countPending(@RequestParam(required = false) String warehouseId) {
        try {
            int count = inboundOrderService.countPendingOrders(warehouseId);
            return AjaxResult.success(count);
        } catch (Exception e) {
            return AjaxResult.error("查询待审核数量失败: " + e.getMessage());
        }
    }

    /**
     * 按状态统计入库单
     *
     * @param warehouseId 仓库ID（可选）
     * @return 统计结果
     */
    @GetMapping("/stats/status")
    public AjaxResult statsByStatus(@RequestParam(required = false) String warehouseId) {
        try {
            List<Map<String, Object>> stats = inboundOrderService.countByStatus(warehouseId);
            return AjaxResult.success(stats);
        } catch (Exception e) {
            return AjaxResult.error("统计失败: " + e.getMessage());
        }
    }

    /**
     * 按入库类型统计入库单
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计结果
     */
    @GetMapping("/stats/type")
    public AjaxResult statsByType(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        try {
            List<Map<String, Object>> stats = inboundOrderService.countByType(startDate, endDate);
            return AjaxResult.success(stats);
        } catch (Exception e) {
            return AjaxResult.error("统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取分发单列表（用于关联单号下拉框）
     * 显示格式：分发单名称 (分发单编号)
     *
     * @return 分发单列表
     */
    @GetMapping("/release-orders")
    public AjaxResult getReleaseOrders() {
        try {
            List<Map<String, Object>> releaseOrders = inboundOrderService.selectReleaseOrderList();
            return AjaxResult.success(releaseOrders);
        } catch (Exception e) {
            return AjaxResult.error("查询分发单列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据关联单号（分发单ID）获取分发投入品明细并匹配库存
     * 匹配规则：根据投入品类型和品类匹配库存中的投入品
     *
     * @param releaseId 分发单ID
     * @param warehouseId 仓库ID
     * @return 分发投入品明细及匹配的库存信息
     */
    @GetMapping("/release-details/{releaseId}")
    public AjaxResult getReleaseDetails(
            @PathVariable String releaseId,
            @RequestParam String warehouseId
    ) {
        try {
            Map<String, Object> result = inboundOrderService.matchReleaseStock(releaseId, warehouseId);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询分发单明细失败: " + e.getMessage());
        }
    }
}
