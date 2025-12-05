package com.inspur.agriculture.input.controller.inventory;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.dto.inventory.OutboundAuditDTO;
import com.inspur.agriculture.input.dto.inventory.OutboundConfirmDTO;
import com.inspur.agriculture.input.dto.inventory.OutboundOrderDTO;
import com.inspur.agriculture.input.dto.inventory.OutboundOrderQueryDTO;
import com.inspur.agriculture.input.service.inventory.IOutboundOrderService;
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
 * @date 2025-12-03
 */
@RestController
@RequestMapping("/api/outbound/orders")
public class OutboundOrderController {

    @Autowired
    private IOutboundOrderService outboundOrderService;

    /**
     * 查询出库单列表
     *
     * @param queryDTO 查询条件
     * @param page 页码
     * @param pageSize 每页数量
     * @return 出库单列表
     */
    @GetMapping
    public AjaxResult list(
            OutboundOrderQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        try {
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (queryDTO.getOutboundStatus() != null && !"all".equals(queryDTO.getOutboundStatus())) {
                params.put("outboundStatus", queryDTO.getOutboundStatus());
            }
            if (queryDTO.getOutboundType() != null && !"all".equals(queryDTO.getOutboundType())) {
                params.put("outboundType", queryDTO.getOutboundType());
            }
            if (queryDTO.getOutboundOrderId() != null && !queryDTO.getOutboundOrderId().isEmpty()) {
                params.put("outboundOrderId", queryDTO.getOutboundOrderId());
            }
            if (queryDTO.getRelatedOrderNo() != null && !queryDTO.getRelatedOrderNo().isEmpty()) {
                params.put("relatedOrderNo", queryDTO.getRelatedOrderNo());
            }

            // 开启分页
            PageHelper.startPage(page, pageSize);
            List<Map<String, Object>> list = outboundOrderService.selectOutboundOrderList(params);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("total", pageInfo.getTotal());
            result.put("page", pageInfo.getPageNum());
            result.put("page_size", pageInfo.getPageSize());
            result.put("items", pageInfo.getList());

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询出库单列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取出库单详情
     *
     * @param outboundOrderId 出库单ID
     * @return 出库单详情
     */
    @GetMapping("/{outboundOrderId}")
    public AjaxResult getInfo(@PathVariable String outboundOrderId) {
        try {
            Map<String, Object> outboundOrder = outboundOrderService.selectOutboundOrderById(outboundOrderId);
            if (outboundOrder == null) {
                return AjaxResult.error("出库单不存在");
            }
            return AjaxResult.success(outboundOrder);
        } catch (Exception e) {
            return AjaxResult.error("查询出库单详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建出库单
     *
     * @param dto 出库单创建信息
     * @return 创建结果
     */
    @PostMapping
    public AjaxResult create(@Validated @RequestBody OutboundOrderDTO dto) {
        try {
            // 构建出库单对象
            com.inspur.agriculture.input.domain.inventory.OutboundOrder outboundOrder =
                new com.inspur.agriculture.input.domain.inventory.OutboundOrder();
            outboundOrder.setOutboundType(dto.getOutboundType());
            outboundOrder.setWarehouseId(dto.getWarehouseId());
            outboundOrder.setRelatedOrderNo(dto.getRelatedOrderNo());
            outboundOrder.setOutboundObjectId(dto.getOutboundObjectId());
            outboundOrder.setOutboundObjectName(dto.getOutboundObjectName());
            outboundOrder.setOutboundUser(dto.getOutboundUser());
            outboundOrder.setOutboundDept(dto.getOutboundDept());
            outboundOrder.setOperator(dto.getOperator());
            outboundOrder.setRemark(dto.getRemark());

            // 构建明细列表
            List<Map<String, Object>> details = new java.util.ArrayList<>();
            for (OutboundOrderDTO.OutboundDetail detail : dto.getDetails()) {
                Map<String, Object> detailMap = new HashMap<>();
                detailMap.put("materialId", detail.getMaterialId());
                detailMap.put("materialType", detail.getMaterialType());
                detailMap.put("materialName",detail.getMaterialName());
                detailMap.put("materialBatchId",detail.getMaterialBatchId());
                detailMap.put("quantity", detail.getQuantity());
                detailMap.put("specModel", detail.getSpecModel());
                detailMap.put("unitOfMeasure", detail.getUnitOfMeasure());
                details.add(detailMap);
            }

            // 创建出库单
            String outboundOrderId = outboundOrderService.createOutboundOrder(outboundOrder, details);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("outbound_order_id", outboundOrderId);
            // 假设批次号在创建时生成
            // result.put("outbound_batch_id", outboundBatchId);

            return AjaxResult.success("创建成功", result);
        } catch (Exception e) {
            return AjaxResult.error("创建出库单失败: " + e.getMessage());
        }
    }

    /**
     * 审核出库单
     *
     * @param outboundOrderId 出库单ID
     * @param dto 审核信息
     * @return 审核结果
     */
    @PutMapping("/{outboundOrderId}/audit")
    public AjaxResult audit(
            @PathVariable String outboundOrderId,
            @Validated @RequestBody OutboundAuditDTO dto
    ) {
        try {
            boolean success = outboundOrderService.auditOutboundOrder(
                    outboundOrderId,
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
            return AjaxResult.error("审核出库单失败: " + e.getMessage());
        }
    }

    /**
     * 执行出库
     * 业务逻辑：
     * 1. 校验库存充足性
     * 2. 校验批次有效性（检查过期日期）
     * 3. 按FIFO原则扣减库存（先进先出）
     * 4. 如果单个批次库存不足，自动拆分至多个入库批次
     * 5. 记录批次拆分明细
     * 6. 更新库存
     * 7. 更新出库单状态为"已出库"
     * 8. 记录库存变动日志
     *
     * @param outboundOrderId 出库单ID
     * @param dto 出库确认信息
     * @return 出库结果（包含批次拆分信息）
     */
    @PutMapping("/{outboundOrderId}/confirm")
    public AjaxResult confirm(
            @PathVariable String outboundOrderId,
            @Validated @RequestBody OutboundConfirmDTO dto
    ) {
        try {
            Map<String, Object> result = outboundOrderService.confirmOutbound(
                    outboundOrderId,
                    dto.getOutboundTime(),
                    dto.getOperator()
            );

            return AjaxResult.success("出库成功", result);
        } catch (Exception e) {
            return AjaxResult.error("执行出库失败: " + e.getMessage());
        }
    }

    /**
     * 取消出库单
     *
     * @param outboundOrderId 出库单ID
     * @param operator 操作人
     * @return 取消结果
     */
    @PutMapping("/{outboundOrderId}/cancel")
    public AjaxResult cancel(
            @PathVariable String outboundOrderId,
            @RequestParam String operator
    ) {
        try {
            boolean success = outboundOrderService.cancelOutboundOrder(outboundOrderId, operator);
            if (success) {
                return AjaxResult.success("取消成功");
            } else {
                return AjaxResult.error("取消失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("取消出库单失败: " + e.getMessage());
        }
    }

    /**
     * 统计出库单数量
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

            int count = outboundOrderService.countOutboundOrders(params);
            return AjaxResult.success(count);
        } catch (Exception e) {
            return AjaxResult.error("统计出库单数量失败: " + e.getMessage());
        }
    }

    /**
     * 查询待审核出库单数量
     *
     * @param warehouseId 仓库ID（可选）
     * @return 待审核数量
     */
    @GetMapping("/pending/count")
    public AjaxResult countPending(@RequestParam(required = false) String warehouseId) {
        try {
            int count = outboundOrderService.countPendingOrders(warehouseId);
            return AjaxResult.success(count);
        } catch (Exception e) {
            return AjaxResult.error("查询待审核数量失败: " + e.getMessage());
        }
    }

    /**
     * 按状态统计出库单
     *
     * @param warehouseId 仓库ID（可选）
     * @return 统计结果
     */
    @GetMapping("/stats/status")
    public AjaxResult statsByStatus(@RequestParam(required = false) String warehouseId) {
        try {
            List<Map<String, Object>> stats = outboundOrderService.countByStatus(warehouseId);
            return AjaxResult.success(stats);
        } catch (Exception e) {
            return AjaxResult.error("统计失败: " + e.getMessage());
        }
    }

    /**
     * 按出库类型统计出库单
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
            List<Map<String, Object>> stats = outboundOrderService.countByType(startDate, endDate);
            return AjaxResult.success(stats);
        } catch (Exception e) {
            return AjaxResult.error("统计失败: " + e.getMessage());
        }
    }
}
