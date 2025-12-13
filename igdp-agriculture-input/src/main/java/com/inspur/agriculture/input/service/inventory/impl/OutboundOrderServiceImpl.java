package com.inspur.agriculture.input.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.agriculture.input.domain.inventory.*;
import com.inspur.agriculture.input.mapper.inventory.*;
import com.inspur.agriculture.input.service.inventory.IInboundOrderService;
import com.inspur.agriculture.input.service.inventory.IOutboundOrderService;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 出库单服务实现类
 *
 * @author igdp
 */
@Service
public class OutboundOrderServiceImpl implements IOutboundOrderService {

    @Autowired
    private OutboundOrderMapper outboundOrderMapper;

    @Autowired
    private OutboundOrderDetailMapper outboundOrderDetailMapper;

    @Autowired
    private OutboundBatchSplitMapper outboundBatchSplitMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockLogMapper stockLogMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private IInboundOrderService inboundOrderService;

    @Override
    public List<Map<String, Object>> selectOutboundOrderList(Map<String, Object> params) {
        return outboundOrderMapper.selectOutboundOrderList(params);
    }

    @Override
    public Map<String, Object> selectOutboundOrderById(String outboundOrderId) {
        if (StringUtils.isEmpty(outboundOrderId)) {
            throw new ServiceException("出库单ID不能为空");
        }
        Map<String, Object> result = outboundOrderMapper.selectOutboundOrderById(outboundOrderId);
        if (result == null) {
            throw new ServiceException("出库单不存在");
        }
        // 查询明细（含批次拆分信息）
        List<Map<String, Object>> details = outboundOrderDetailMapper.selectDetailsWithBatchSplits(outboundOrderId);
        result.put("details", details);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOutboundOrder(OutboundOrder outboundOrder, List<Map<String, Object>> details) {
        // 校验参数
        if (outboundOrder == null) {
            throw new ServiceException("出库单信息不能为空");
        }
        if (details == null || details.isEmpty()) {
            throw new ServiceException("出库明细不能为空");
        }

        // 校验仓库是否存在
        if (StringUtils.isEmpty(outboundOrder.getWarehouseId())) {
            throw new ServiceException("仓库ID不能为空");
        }
        Warehouse warehouse = warehouseMapper.selectById(Long.valueOf(outboundOrder.getWarehouseId()));
        if (warehouse == null) {
            throw new ServiceException("仓库不存在");
        }
        if (!"1".equals(warehouse.getStatus())) {
            throw new ServiceException("仓库已停用，无法出库");
        }

        // 生成出库单ID和批次号
        String outboundOrderId = "OUT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String outboundBatchId = "BATCH-OUT-" + System.currentTimeMillis();

        // 设置出库单信息
        outboundOrder.setId(UUID.randomUUID().toString().replace("-", ""));
        outboundOrder.setOutboundOrderId(outboundOrderId);
        outboundOrder.setOutboundBatchId(outboundBatchId);
        outboundOrder.setOutboundStatus("pending");
        outboundOrder.setCreatedAt(new Date());
        outboundOrder.setUpdatedAt(new Date());

        // 插入出库单
        outboundOrderMapper.insert(outboundOrder);

        // 插入出库明细
        List<OutboundOrderDetail> detailList = new ArrayList<>();
        for (Map<String, Object> detail : details) {
            OutboundOrderDetail detailEntity = new OutboundOrderDetail();
            detailEntity.setId(UUID.randomUUID().toString().replace("-", ""));
            detailEntity.setDetailId("DET-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
            detailEntity.setOutboundOrderId(outboundOrderId);
            detailEntity.setMaterialId(detail.get("materialId").toString());
            detailEntity.setMaterialType(detail.get("materialType").toString());
            detailEntity.setMaterialName(detail.get("materialName").toString());
            detailEntity.setMaterialBatchId(detail.get("materialBatchId") != null ? detail.get("materialBatchId").toString() : null);
            detailEntity.setQuantity(new BigDecimal(detail.get("quantity").toString()));
            detailEntity.setSpecModel(detail.get("specModel") != null ? detail.get("specModel").toString() : null);
            detailEntity.setUnitOfMeasure(detail.get("unitOfMeasure") != null ? detail.get("unitOfMeasure").toString() : null);
            detailEntity.setOperator(outboundOrder.getOperator());
            detailEntity.setCreatedAt(new Date());
            detailEntity.setUpdatedAt(new Date());
            detailList.add(detailEntity);
        }
        outboundOrderDetailMapper.batchInsert(detailList);

        return outboundOrderId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditOutboundOrder(String outboundOrderId, String auditStatus, String auditUser, Date auditTime, String remark) {
        // 校验参数
        if (StringUtils.isEmpty(outboundOrderId)) {
            throw new ServiceException("出库单ID不能为空");
        }
        if (StringUtils.isEmpty(auditStatus)) {
            throw new ServiceException("审核状态不能为空");
        }
        if (!Arrays.asList("approved", "rejected").contains(auditStatus)) {
            throw new ServiceException("审核状态无效");
        }

        // 查询出库单
        LambdaQueryWrapper<OutboundOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutboundOrder::getOutboundOrderId, outboundOrderId);
        OutboundOrder outboundOrder = outboundOrderMapper.selectOne(wrapper);
        if (outboundOrder == null) {
            throw new ServiceException("出库单不存在");
        }

        // 校验状态
        if (!"pending".equals(outboundOrder.getOutboundStatus())) {
            throw new ServiceException("只有待审核状态的出库单才能审核");
        }

        // 更新出库单
        outboundOrder.setAuditUser(auditUser);
        outboundOrder.setAuditTime(auditTime != null ? auditTime : new Date());
        outboundOrder.setRemark(remark);
        outboundOrder.setUpdatedAt(new Date());

        // 根据审核结果更新状态
        if ("approved".equals(auditStatus)) {
            outboundOrder.setOutboundStatus("completed");

            // 审批通过时同步更新库存（并发安全）
            // 校验仓库
            Warehouse warehouse = warehouseMapper.selectById(Long.valueOf(outboundOrder.getWarehouseId()));
            if (warehouse == null) {
                throw new ServiceException("仓库不存在");
            }
            if (!"1".equals(warehouse.getStatus())) {
                throw new ServiceException("仓库已停用，无法出库");
            }

            // 查询出库明细
            LambdaQueryWrapper<OutboundOrderDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(OutboundOrderDetail::getOutboundOrderId, outboundOrderId);
            List<OutboundOrderDetail> details = outboundOrderDetailMapper.selectList(detailWrapper);

            BigDecimal totalOutboundQuantity = BigDecimal.ZERO;

            // 处理每个明细
            for (OutboundOrderDetail detail : details) {
                BigDecimal requiredQuantity = detail.getQuantity();
                totalOutboundQuantity = totalOutboundQuantity.add(requiredQuantity);

                // 按FIFO规则查询可用库存
                List<Stock> availableStocks = stockMapper.selectAvailableStockFIFO(
                        outboundOrder.getWarehouseId(),
                        detail.getMaterialId(),
                        requiredQuantity
                );

                if (availableStocks == null || availableStocks.isEmpty()) {
                    throw new ServiceException("物料[" + detail.getMaterialId() + "]库存不足");
                }

                // 计算总可用库存
                BigDecimal totalAvailable = BigDecimal.ZERO;
                for (Stock stock : availableStocks) {
                    totalAvailable = totalAvailable.add(stock.getQuantity());
                }

                if (totalAvailable.compareTo(requiredQuantity) < 0) {
                    throw new ServiceException("物料[" + detail.getMaterialId() + "]库存不足，需要：" + requiredQuantity + "，可用：" + totalAvailable);
                }

                // 校验批次是否过期
                Date now = new Date();
                for (Stock stock : availableStocks) {
                    if (stock.getExpiryDate() != null && stock.getExpiryDate().before(now)) {
                        throw new ServiceException("批次[" + stock.getMaterialBatchId() + "]已过期，不能出库");
                    }
                }

                // 按FIFO原则扣减库存（使用悲观锁）
                BigDecimal remainingRequired = requiredQuantity;
                for (Stock stock : availableStocks) {
                    if (remainingRequired.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }

                    // 使用悲观锁重新查询库存（FOR UPDATE）
                    Stock lockedStock = stockMapper.selectByBatchForUpdate(
                            outboundOrder.getWarehouseId(),
                            detail.getMaterialId(),
                            stock.getMaterialBatchId()
                    );

                    if (lockedStock == null) {
                        throw new ServiceException("批次[" + stock.getMaterialBatchId() + "]库存记录不存在");
                    }

                    // 使用加锁后的最新库存数量
                    BigDecimal stockQuantity = lockedStock.getQuantity();

                    // 再次校验库存是否充足（防止并发扣减）
                    if (stockQuantity.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new ServiceException("批次[" + stock.getMaterialBatchId() + "]库存不足（已被其他出库单占用）");
                    }

                    BigDecimal outboundQuantity;
                    BigDecimal afterQuantity;

                    if (stockQuantity.compareTo(remainingRequired) >= 0) {
                        // 当前批次库存充足
                        outboundQuantity = remainingRequired;
                        afterQuantity = stockQuantity.subtract(outboundQuantity);
                        remainingRequired = BigDecimal.ZERO;
                    } else {
                        // 当前批次库存不足，需要拆分
                        outboundQuantity = stockQuantity;
                        afterQuantity = BigDecimal.ZERO;
                        remainingRequired = remainingRequired.subtract(outboundQuantity);
                    }

                    // 更新库存
                    lockedStock.setQuantity(afterQuantity);
                    lockedStock.setOutboundQuantity(
                            (lockedStock.getOutboundQuantity() != null ? lockedStock.getOutboundQuantity() : BigDecimal.ZERO)
                                    .add(outboundQuantity)
                    );
                    lockedStock.setUpdatedAt(new Date());
                    stockMapper.updateById(lockedStock);

                    // 记录批次拆分
                    OutboundBatchSplit split = new OutboundBatchSplit();
                    split.setId(UUID.randomUUID().toString().replace("-", ""));
                    split.setOutboundDetailId(detail.getDetailId());
                    split.setInboundBatchId(lockedStock.getMaterialBatchId());
                    split.setSplitQuantity(outboundQuantity);
                    split.setRemainingQuantity(afterQuantity);
                    split.setCreatedAt(new Date());
                    outboundBatchSplitMapper.insert(split);

                    // 记录库存变动日志
                    StockLog stockLog = new StockLog();
                    stockLog.setId(UUID.randomUUID().toString().replace("-", ""));
                    stockLog.setWarehouseId(outboundOrder.getWarehouseId());
                    stockLog.setMaterialId(detail.getMaterialId());
                    stockLog.setMaterialBatchId(lockedStock.getMaterialBatchId());
                    stockLog.setOperationType("outbound");
                    stockLog.setChangeQuantity(outboundQuantity.negate()); // 出库为负数
                    stockLog.setBeforeQuantity(stockQuantity);
                    stockLog.setAfterQuantity(afterQuantity);
                    stockLog.setReferenceOrderId(outboundOrderId);
                    stockLog.setOperator(auditUser);
                    stockLog.setCreatedAt(new Date());
                    stockLogMapper.insert(stockLog);
                }

                // 更新出库明细（使用第一个批次作为主批次）
                if (!availableStocks.isEmpty()) {
                    detail.setMaterialBatchId(availableStocks.get(0).getMaterialBatchId());
                    detail.setUpdatedAt(new Date());
                    outboundOrderDetailMapper.updateById(detail);
                }
            }

            // 更新仓库已用容量（减少）
            warehouseMapper.updateUsedCapacity(Long.valueOf(outboundOrder.getWarehouseId()), totalOutboundQuantity.negate());

        } else {
            outboundOrder.setOutboundStatus("rejected");
        }

        return outboundOrderMapper.updateById(outboundOrder) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> confirmOutbound(String outboundOrderId, Date outboundTime, String operator) {
        // 校验参数
        if (StringUtils.isEmpty(outboundOrderId)) {
            throw new ServiceException("出库单ID不能为空");
        }

        // 查询出库单
        LambdaQueryWrapper<OutboundOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutboundOrder::getOutboundOrderId, outboundOrderId);
        OutboundOrder outboundOrder = outboundOrderMapper.selectOne(wrapper);
        if (outboundOrder == null) {
            throw new ServiceException("出库单不存在");
        }

        // 校验状态（必须是已审核状态）
        if (!"approved".equals(outboundOrder.getOutboundStatus())) {
            throw new ServiceException("只有已审核的出库单才能执行出库");
        }

        // 校验仓库
        Warehouse warehouse = warehouseMapper.selectById(Long.valueOf(outboundOrder.getWarehouseId()));
        if (warehouse == null) {
            throw new ServiceException("仓库不存在");
        }
        if (!"1".equals(warehouse.getStatus())) {
            throw new ServiceException("仓库已停用，无法出库");
        }

        // 查询出库明细
        LambdaQueryWrapper<OutboundOrderDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(OutboundOrderDetail::getOutboundOrderId, outboundOrderId);
        List<OutboundOrderDetail> details = outboundOrderDetailMapper.selectList(detailWrapper);

        List<Map<String, Object>> updatedStock = new ArrayList<>();
        List<Map<String, Object>> batchSplits = new ArrayList<>();

        // 查询批次拆分信息（库存已在审批时扣减）
        for (OutboundOrderDetail detail : details) {
            // 查询该明细的批次拆分记录
            LambdaQueryWrapper<OutboundBatchSplit> splitWrapper = new LambdaQueryWrapper<>();
            splitWrapper.eq(OutboundBatchSplit::getOutboundDetailId, detail.getDetailId());
            List<OutboundBatchSplit> splits = outboundBatchSplitMapper.selectList(splitWrapper);

            for (OutboundBatchSplit split : splits) {
                // 批次拆分信息
                Map<String, Object> splitInfo = new HashMap<>();
                splitInfo.put("inbound_batch_id", split.getInboundBatchId());
                splitInfo.put("split_quantity", split.getSplitQuantity());
                splitInfo.put("remaining_quantity", split.getRemainingQuantity());
                batchSplits.add(splitInfo);

                // 库存信息
                Map<String, Object> stockInfo = new HashMap<>();
                stockInfo.put("material_id", detail.getMaterialId());
                stockInfo.put("warehouse_id", outboundOrder.getWarehouseId());
                stockInfo.put("batch_id", split.getInboundBatchId());
                stockInfo.put("outbound_quantity", split.getSplitQuantity());
                stockInfo.put("remaining_quantity", split.getRemainingQuantity());
                updatedStock.add(stockInfo);
            }

            // 更新出库明细时间
            detail.setOutboundTime(outboundTime != null ? outboundTime : new Date());
            detail.setUpdatedAt(new Date());
            outboundOrderDetailMapper.updateById(detail);
        }

        // 注：库存已在审批通过时扣减，此处不再重复扣减

        // 更新出库单状态
        outboundOrder.setOutboundStatus("completed");
        outboundOrder.setOutboundTime(outboundTime != null ? outboundTime : new Date());
        outboundOrder.setOutboundUser(operator != null ? operator : outboundOrder.getOperator());
        outboundOrder.setUpdatedAt(new Date());
        outboundOrderMapper.updateById(outboundOrder);

        // 出库完成后，自动创建对应的入库单
        String inboundOrderId = null;
        boolean autoApproved = false; // 是否自动审核通过

        // 判断是否需要创建入库单（所有出库类型都需要）
        if (outboundOrder.getOutboundType() != null) {
            try {
                // 校验目标仓库ID
                if (StringUtils.isEmpty(outboundOrder.getOutboundObjectId())) {
                    throw new ServiceException("出库对象ID不能为空");
                }

                // 确定入库类型和入库仓库
                Integer inboundType;
                String targetWarehouseId;
                String supplierType;
                String remark;

                if (outboundOrder.getOutboundType() == 2) {
                    // 调拨出库 -> 调拨入库（目标仓库）
                    inboundType = 2;
                    targetWarehouseId = outboundOrder.getOutboundObjectId();
                    supplierType = "内部调拨";
                    remark = "调拨入库 - 来自出库单：" + outboundOrderId;
                    autoApproved = true; // 调拨出库自动审核通过
                } else {
                    // 销售出库等其他类型 -> 生成入库单但不自动审核
                    // 这里假设是退货入库或其他类型的入库
                    inboundType = 1; // 采购入库或其他入库类型
                    targetWarehouseId = outboundOrder.getOutboundObjectId();
                    supplierType = "销售退货";
                    remark = "出库关联入库单 - 来自出库单：" + outboundOrderId;
                    autoApproved = false; // 需要人工审核
                }

                // 校验目标仓库是否存在
                Warehouse targetWarehouse = warehouseMapper.selectById(Long.valueOf(targetWarehouseId));
                if (targetWarehouse == null) {
                    throw new ServiceException("目标仓库不存在：" + targetWarehouseId);
                }
                if (!"1".equals(targetWarehouse.getStatus())) {
                    throw new ServiceException("目标仓库已停用，无法入库");
                }

                // 构建入库单
                InboundOrder inboundOrder = new InboundOrder();
                inboundOrder.setInboundType(inboundType);
                inboundOrder.setWarehouseId(targetWarehouseId);
                inboundOrder.setRelatedOrderNo(outboundOrderId); // 关联出库单号
                inboundOrder.setSupplierType(supplierType);
                inboundOrder.setSupplierId(outboundOrder.getWarehouseId()); // 来源仓库ID作为供应商ID
                inboundOrder.setInboundUser(operator != null ? operator : outboundOrder.getOperator());
                inboundOrder.setApplyTime(new Date());
                inboundOrder.setOperator(operator != null ? operator : outboundOrder.getOperator());
                inboundOrder.setRemark(remark);

                // 构建入库明细（从出库明细转换）
                List<Map<String, Object>> inboundDetails = new ArrayList<>();
                for (OutboundOrderDetail detail : details) {
                    Map<String, Object> inboundDetail = new HashMap<>();
                    inboundDetail.put("materialId", detail.getMaterialId());
                    inboundDetail.put("materialType", detail.getMaterialType());
                    inboundDetail.put("materialName", detail.getMaterialName());
                    inboundDetail.put("quantity", detail.getQuantity());
                    inboundDetail.put("specModel", detail.getSpecModel());
                    inboundDetail.put("unitOfMeasure", detail.getUnitOfMeasure());
                    inboundDetail.put("materialBatchId",detail.getMaterialBatchId());
                    // 从批次拆分信息中获取第一个批次的过期日期
                    if (!batchSplits.isEmpty()) {
                        String firstBatchId = (String) batchSplits.get(0).get("inbound_batch_id");
                        // 查询该批次的过期日期
                        LambdaQueryWrapper<Stock> stockWrapper = new LambdaQueryWrapper<>();
                        stockWrapper.eq(Stock::getMaterialBatchId, firstBatchId);
                        stockWrapper.eq(Stock::getMaterialId, detail.getMaterialId());
                        stockWrapper.last("LIMIT 1");
                        Stock stockInfo = stockMapper.selectOne(stockWrapper);
                        if (stockInfo != null && stockInfo.getExpiryDate() != null) {
                            inboundDetail.put("expiryDate", stockInfo.getExpiryDate());
                        } else {
                            // 如果没有过期日期，设置为1年后
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.YEAR, 1);
                            inboundDetail.put("expiryDate", calendar.getTime());
                        }
                    } else {
                        // 默认设置为1年后
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.YEAR, 1);
                        inboundDetail.put("expiryDate", calendar.getTime());
                    }

                    inboundDetails.add(inboundDetail);
                }

                // 创建入库单
                inboundOrderId = inboundOrderService.createInboundOrder(inboundOrder, inboundDetails);

                // 如果是调拨出库，自动审核并执行入库
                if (autoApproved) {
                    // 自动审核通过
                    inboundOrderService.auditInboundOrder(
                        inboundOrderId,
                        "approved",
                        "系统自动审核",
                        new Date(),
                        "调拨出库自动生成的入库单，已自动审核通过"
                    );

                    // 自动执行入库
                    inboundOrderService.confirmInbound(
                        inboundOrderId,
                        outboundTime != null ? outboundTime : new Date(),
                        operator != null ? operator : outboundOrder.getOperator()
                    );
                }

            } catch (Exception e) {
                // 入库单创建失败不影响出库流程，但记录错误日志
                throw new ServiceException("出库成功，但创建关联入库单失败：" + e.getMessage());
            }
        }

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("outbound_order_id", outboundOrderId);
        result.put("updated_stock", updatedStock);
        result.put("batch_splits", batchSplits);

        // 返回生成的入库单ID
        if (inboundOrderId != null) {
            result.put("inbound_order_id", inboundOrderId);
            result.put("auto_approved", autoApproved);
            if (autoApproved) {
                result.put("transfer_success", true);
                result.put("transfer_message", "调拨入库单已自动创建并完成入库");
            } else {
                result.put("transfer_success", false);
                result.put("transfer_message", "关联入库单已创建，等待审核");
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOutboundOrder(String outboundOrderId, String operator) {
        if (StringUtils.isEmpty(outboundOrderId)) {
            throw new ServiceException("出库单ID不能为空");
        }

        LambdaQueryWrapper<OutboundOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutboundOrder::getOutboundOrderId, outboundOrderId);
        OutboundOrder outboundOrder = outboundOrderMapper.selectOne(wrapper);

        if (outboundOrder == null) {
            throw new ServiceException("出库单不存在");
        }

        if ("completed".equals(outboundOrder.getOutboundStatus())) {
            throw new ServiceException("已完成的出库单不能取消");
        }

        outboundOrder.setOutboundStatus("cancelled");
        outboundOrder.setUpdatedAt(new Date());
        return outboundOrderMapper.updateById(outboundOrder) > 0;
    }

    @Override
    public int countOutboundOrders(Map<String, Object> params) {
        return outboundOrderMapper.countOutboundOrders(params);
    }

    @Override
    public int countPendingOrders(String warehouseId) {
        return outboundOrderMapper.countPendingOrders(warehouseId);
    }

    @Override
    public List<Map<String, Object>> countByStatus(String warehouseId) {
        return outboundOrderMapper.countByStatus(warehouseId);
    }

    @Override
    public List<Map<String, Object>> countByType(String startDate, String endDate) {
        return outboundOrderMapper.countByType(startDate, endDate);
    }

    @Override
    public Map<String, Object> validateStock(String warehouseId, String materialId, BigDecimal quantity) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 校验参数
            if (StringUtils.isEmpty(warehouseId)) {
                result.put("valid", false);
                result.put("message", "仓库ID不能为空");
                return result;
            }
            if (StringUtils.isEmpty(materialId)) {
                result.put("valid", false);
                result.put("message", "物料ID不能为空");
                return result;
            }
            if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
                result.put("valid", false);
                result.put("message", "出库数量必须大于0");
                return result;
            }

            // 查询可用库存（FIFO）
            List<Stock> availableStocks = stockMapper.selectAvailableStockFIFO(warehouseId, materialId, quantity);

            if (availableStocks == null || availableStocks.isEmpty()) {
                result.put("valid", false);
                result.put("message", "库存不足");
                result.put("available_quantity", BigDecimal.ZERO);
                return result;
            }

            // 计算总可用库存
            BigDecimal totalAvailable = BigDecimal.ZERO;
            for (Stock stock : availableStocks) {
                totalAvailable = totalAvailable.add(stock.getQuantity());
            }

            // 校验库存是否充足
            if (totalAvailable.compareTo(quantity) < 0) {
                result.put("valid", false);
                result.put("message", "库存不足，需要：" + quantity + "，可用：" + totalAvailable);
                result.put("available_quantity", totalAvailable);
                result.put("required_quantity", quantity);
                result.put("shortage", quantity.subtract(totalAvailable));
            } else {
                result.put("valid", true);
                result.put("message", "库存充足");
                result.put("available_quantity", totalAvailable);
                result.put("required_quantity", quantity);
            }

        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "校验失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> validateStockBatch(String warehouseId, List<Map<String, Object>> details) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> insufficientItems = new ArrayList<>();
        boolean allValid = true;

        try {
            // 校验参数
            if (StringUtils.isEmpty(warehouseId)) {
                result.put("valid", false);
                result.put("message", "仓库ID不能为空");
                return result;
            }
            if (details == null || details.isEmpty()) {
                result.put("valid", false);
                result.put("message", "出库明细不能为空");
                return result;
            }

            // 逐个校验每个明细
            for (Map<String, Object> detail : details) {
                String materialId = detail.get("materialId") != null ? detail.get("materialId").toString() : null;
                String materialName = detail.get("materialName") != null ? detail.get("materialName").toString() : "";
                BigDecimal quantity = detail.get("quantity") != null ? new BigDecimal(detail.get("quantity").toString()) : BigDecimal.ZERO;

                if (StringUtils.isEmpty(materialId) || quantity.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                // 调用单个校验方法
                Map<String, Object> validateResult = validateStock(warehouseId, materialId, quantity);

                if (!(Boolean) validateResult.get("valid")) {
                    allValid = false;
                    Map<String, Object> insufficientItem = new HashMap<>();
                    insufficientItem.put("material_id", materialId);
                    insufficientItem.put("material_name", materialName);
                    insufficientItem.put("required_quantity", quantity);
                    insufficientItem.put("available_quantity", validateResult.get("available_quantity"));
                    insufficientItem.put("shortage", validateResult.get("shortage"));
                    insufficientItem.put("message", validateResult.get("message"));
                    insufficientItems.add(insufficientItem);
                }
            }

            // 构建返回结果
            result.put("valid", allValid);
            if (allValid) {
                result.put("message", "所有物料库存充足");
            } else {
                result.put("message", "部分物料库存不足");
                result.put("insufficient_items", insufficientItems);
            }

        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "批量校验失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> selectReleaseOrderList() {
        return outboundOrderMapper.selectReleaseOrderList();
    }

    @Override
    public Map<String, Object> validateReleaseStock(String releaseId, String warehouseId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 校验参数
            if (StringUtils.isEmpty(releaseId)) {
                result.put("valid", false);
                result.put("message", "分发单ID不能为空");
                return result;
            }
            if (StringUtils.isEmpty(warehouseId)) {
                result.put("valid", false);
                result.put("message", "仓库ID不能为空");
                return result;
            }

            // 查询分发单主表信息
            Map<String, Object> releaseMain = outboundOrderMapper.selectReleaseMainById(releaseId);
            if (releaseMain == null) {
                result.put("valid", false);
                result.put("message", "分发单不存在");
                return result;
            }

            // 查询分发单明细（包含投入品的农资类型和品种）
            List<Map<String, Object>> releaseDetails = outboundOrderMapper.selectReleaseDetailsByReleaseId(releaseId);

            if (releaseDetails == null || releaseDetails.isEmpty()) {
                result.put("valid", false);
                result.put("message", "分发单明细不存在");
                return result;
            }

            // 校验每个投入品的库存
            List<Map<String, Object>> insufficientItems = new ArrayList<>();
            List<Map<String, Object>> enrichedDetails = new ArrayList<>();
            boolean allValid = true;

            for (Map<String, Object> detail : releaseDetails) {
                Long inputId = detail.get("input_id") != null ? Long.parseLong(detail.get("input_id").toString()) : null;
                String inputName = detail.get("input_name") != null ? detail.get("input_name").toString() : "";
                String inputType = detail.get("input_type") != null ? detail.get("input_type").toString() : "";
                String agriculturalInputType = detail.get("agricultural_input_type") != null ? detail.get("agricultural_input_type").toString() : "";
                String variety = detail.get("input_variety") != null ? detail.get("input_variety").toString() : "";
                BigDecimal required = detail.get("required") != null ? new BigDecimal(detail.get("required").toString()) : BigDecimal.ZERO;

                if (inputId == null || required.compareTo(BigDecimal.ZERO) <= 0) {
                    enrichedDetails.add(detail);
                    continue;
                }

                // 查询库存
                List<Stock> availableStocks = stockMapper.selectAvailableStockFIFO(
                        warehouseId,
                        inputId.toString(),
                        required
                );

                // 计算总可用库存
                BigDecimal totalAvailable = BigDecimal.ZERO;
                if (availableStocks != null && !availableStocks.isEmpty()) {
                    for (Stock stock : availableStocks) {
                        totalAvailable = totalAvailable.add(stock.getQuantity());
                    }
                }

                // 为明细添加库存校验信息
                Map<String, Object> enrichedDetail = new HashMap<>(detail);
                enrichedDetail.put("available_quantity", totalAvailable);
                enrichedDetail.put("stock_sufficient", totalAvailable.compareTo(required) >= 0);

                // 确保包含农资类型和品种
                enrichedDetail.put("agricultural_input_type", agriculturalInputType);
                enrichedDetail.put("variety", variety);

                // 检查库存是否充足
                if (totalAvailable.compareTo(required) < 0) {
                    allValid = false;
                    enrichedDetail.put("shortage", required.subtract(totalAvailable));

                    Map<String, Object> insufficientItem = new HashMap<>();
                    insufficientItem.put("input_id", inputId);
                    insufficientItem.put("input_name", inputName);
                    insufficientItem.put("input_type", inputType);
                    insufficientItem.put("agricultural_input_type", agriculturalInputType);
                    insufficientItem.put("variety", variety);
                    insufficientItem.put("required_quantity", required);
                    insufficientItem.put("available_quantity", totalAvailable);
                    insufficientItem.put("shortage", required.subtract(totalAvailable));
                    insufficientItem.put("message", "库存不足，需要：" + required + "，可用：" + totalAvailable);
                    insufficientItems.add(insufficientItem);
                }

                enrichedDetails.add(enrichedDetail);
            }

            // 构建返回结果 - 按照前端期望的格式
            result.put("main", releaseMain);
            result.put("details", enrichedDetails);
            result.put("valid", allValid);

            if (allValid) {
                result.put("message", "所有投入品库存充足");
            } else {
                result.put("message", "部分投入品库存不足");
                result.put("insufficient_items", insufficientItems);
            }

        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "校验失败：" + e.getMessage());
        }

        return result;
    }
}
