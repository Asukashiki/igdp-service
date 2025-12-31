package com.inspur.agriculture.input.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.agriculture.input.domain.inventory.*;
import com.inspur.agriculture.input.mapper.inventory.*;
import com.inspur.agriculture.input.service.inventory.IInboundOrderService;
import com.inspur.agriculture.input.service.inventory.IOutboundOrderService;
import com.inspur.agriculture.input.util.UnitConversionUtil;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
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
            throw new ServiceException("The outbound order ID cannot be empty");
        }
        Map<String, Object> result = outboundOrderMapper.selectOutboundOrderById(outboundOrderId);
        if (result == null) {
            throw new ServiceException("The outbound order does not exist");
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
            throw new ServiceException("The outbound order information cannot be empty");
        }
        if (details == null || details.isEmpty()) {
            throw new ServiceException("The outbound order detail cannot be empty");
        }

        // 校验仓库是否存在
        if (StringUtils.isEmpty(outboundOrder.getWarehouseId())) {
            throw new ServiceException("The warehouse ID cannot be empty");
        }
        Warehouse warehouse = warehouseMapper.selectById(Long.valueOf(outboundOrder.getWarehouseId()));
        if (warehouse == null) {
            throw new ServiceException("The warehouse does not exist");
        }
        if (!"1".equals(warehouse.getStatus())) {
            throw new ServiceException("The warehouse has been deactivated and cannot be out of stock");
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

        // 设置出库员为当前登录人
        String currentUser = SecurityUtils.getUsername();
        if (StringUtils.isNotEmpty(currentUser)) {
            outboundOrder.setOutboundUser(currentUser);
        }

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
            detailEntity.setAgriculturalInputType(detail.get("agriculturalInputType") != null ? detail.get("agriculturalInputType").toString() : null);
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
            throw new ServiceException("The outbound order ID cannot be empty");
        }
        if (StringUtils.isEmpty(auditStatus)) {
            throw new ServiceException("The review status cannot be empty");
        }
        if (!Arrays.asList("approved", "rejected").contains(auditStatus)) {
            throw new ServiceException("The review status is invalid");
        }

        // 查询出库单
        LambdaQueryWrapper<OutboundOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutboundOrder::getOutboundOrderId, outboundOrderId);
        OutboundOrder outboundOrder = outboundOrderMapper.selectOne(wrapper);
        if (outboundOrder == null) {
            throw new ServiceException("The outbound order does not exist");
        }

        // 校验状态
        if (!"pending".equals(outboundOrder.getOutboundStatus())) {
            throw new ServiceException("Only the outbound orders with pending review status can be reviewed");
        }

        // 更新出库单
        // 设置审核人为当前登录人
        String currentUser = SecurityUtils.getUsername();
        outboundOrder.setAuditUser(StringUtils.isNotEmpty(currentUser) ? currentUser : auditUser);
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
                throw new ServiceException("The warehouse doesn't exist.");
            }
            if (!"1".equals(warehouse.getStatus())) {
                throw new ServiceException("The warehouse has been shut down and no goods can be dispatched");
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
                    throw new ServiceException("Materials[" + detail.getMaterialId() + "]Insufficient inventory");
                }

                // 计算总可用库存
                BigDecimal totalAvailable = BigDecimal.ZERO;
                for (Stock stock : availableStocks) {
                    totalAvailable = totalAvailable.add(stock.getQuantity());
                }

                if (totalAvailable.compareTo(requiredQuantity) < 0) {
                    throw new ServiceException("Materials[" + detail.getMaterialId() + "]Insufficient inventory，Needed：" + requiredQuantity + "，Available：" + totalAvailable);
                }

                // 校验批次是否过期
                Date now = new Date();
                for (Stock stock : availableStocks) {
                    if (stock.getExpiryDate() != null && stock.getExpiryDate().before(now)) {
                        throw new ServiceException("Batch[" + stock.getMaterialBatchId() + "]Expired and cannot be taken out of the warehouse");
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
                        throw new ServiceException("Batch[" + stock.getMaterialBatchId() + "]There is no inventory record");
                    }

                    // 使用加锁后的最新库存数量
                    BigDecimal stockQuantity = lockedStock.getQuantity();

                    // 再次校验库存是否充足（防止并发扣减）
                    if (stockQuantity.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new ServiceException("Batch[" + stock.getMaterialBatchId() + "]Insufficient inventory（It has been occupied by other outbound orders）");
                    }

                    BigDecimal outboundQuantity;
                    BigDecimal afterQuantity;

                    if (stockQuantity.compareTo(remainingRequired) >= 0) {
                        // 当前批次库存充足
                        outboundQuantity = remainingRequired;
                        afterQuantity = stockQuantity.subtract(outboundQuantity);
                        remainingRequired = BigDecimal.ZERO;
                    } else {
                        // 当前批次Insufficient inventory，需要拆分
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
                    // 更新投入品类型和投入品品类（确保数据一致性）
                    lockedStock.setMaterialType(detail.getMaterialType());
                    lockedStock.setAgriculturalInputType(detail.getAgriculturalInputType());
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
                    // 出库为负数
                    stockLog.setChangeQuantity(outboundQuantity.negate());
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

            // 审批通过后，更新关联的分发单状态和确认接收单状态
            if (StringUtils.isNotEmpty(outboundOrder.getRelatedOrderNo())) {
                try {
                    // 更新分发单状态为Completed
                    outboundOrderMapper.updateReleaseOrderStatus(outboundOrder.getRelatedOrderNo(), "outCompleted");
                    // 更新农民分发单

                } catch (Exception e) {
                    // 记录日志但不影响主流程
                    throw new ServiceException("The outbound approval was successful, but the update of the associated distribution order status failed: " + e.getMessage());
                }
            }

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
            throw new ServiceException("The outbound order ID cannot be empty");
        }

        // 查询出库单
        LambdaQueryWrapper<OutboundOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutboundOrder::getOutboundOrderId, outboundOrderId);
        OutboundOrder outboundOrder = outboundOrderMapper.selectOne(wrapper);
        if (outboundOrder == null) {
            throw new ServiceException("The outbound order does not exist");
        }

        // 校验状态（必须是已审核状态）
   /*     if (!"approved".equals(outboundOrder.getOutboundStatus())) {
            throw new ServiceException("只有已审核的出库单才能执行出库");
        }
*/
        // 校验仓库
        Warehouse warehouse = warehouseMapper.selectById(Long.valueOf(outboundOrder.getWarehouseId()));
        if (warehouse == null) {
            throw new ServiceException("The warehouse does not exist");
        }
        if (!"1".equals(warehouse.getStatus())) {
            throw new ServiceException("The warehouse has been deactivated and cannot be out of stock");
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
        if (outboundOrder.getOutboundType() != null && StringUtils.isNotBlank(outboundOrder.getOutboundObjectId()) && !"CUSTOMER_DEFAULT".equals(outboundOrder.getOutboundObjectId())) {
            try {
                // 校验目标仓库ID
              /*  if (StringUtils.isEmpty(outboundOrder.getOutboundObjectId())) {
                    throw new ServiceException("出库对象ID不能为空");
                }
*/
                // 确定入库类型和入库仓库
                Integer inboundType;
                String targetWarehouseId;
                String supplierType;
                String remark;

                if (outboundOrder.getOutboundType() == 2) {
                    // 调拨出库 -> 调拨入库（目标仓库）
                    inboundType = 2;
                    targetWarehouseId = outboundOrder.getOutboundObjectId();
                    supplierType = "Internal transfer";
                    remark = "Transfer in - from the outbound order：" + outboundOrderId;
                    autoApproved = true; // 调拨出库自动审核通过
                } else {
                    // 销售出库等其他类型 -> 生成入库单但不自动审核
                    // 这里假设是退货入库或其他类型的入库
                    inboundType = 1; // 采购入库或其他入库类型
                    targetWarehouseId = outboundOrder.getOutboundObjectId();
                    supplierType = "Sales return";
                    remark = "The inbound order is associated with the outbound order：" + outboundOrderId;
                    autoApproved = false; // 需要人工审核
                }

                // 校验目标仓库是否存在
                Warehouse targetWarehouse = warehouseMapper.selectById(Long.valueOf(targetWarehouseId));
                if (targetWarehouse == null) {
                    throw new ServiceException("The target warehouse does not exist：" + targetWarehouseId);
                }
                if (!"1".equals(targetWarehouse.getStatus())) {
                    throw new ServiceException("The target warehouse has been deactivated and cannot be stored");
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
                            "Automatic system review",
                            new Date(),
                            "The inbound order was automatically generated by the transfer outbound and has been automatically approved"
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
                throw new ServiceException("The outbound was successful, but the creation of the associated inbound order failed：" + e.getMessage());
            }
        }

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("outbound_order_id", outboundOrderId);
        result.put("updated_stock", updatedStock);
        result.put("batch_splits", batchSplits);
        result.put("message", "The outbound order has been confirmed and completed");

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOutboundOrder(String outboundOrderId, String operator) {
        if (StringUtils.isEmpty(outboundOrderId)) {
            throw new ServiceException("The outbound order ID cannot be empty");
        }

        LambdaQueryWrapper<OutboundOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutboundOrder::getOutboundOrderId, outboundOrderId);
        OutboundOrder outboundOrder = outboundOrderMapper.selectOne(wrapper);

        if (outboundOrder == null) {
            throw new ServiceException("The outbound order does not exist");
        }

        if ("completed".equals(outboundOrder.getOutboundStatus())) {
            throw new ServiceException("Completed outbound orders cannot be cancelled");
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
        // 调用新方法，不传计量单位时使用数量校验
        return validateStockWithUnit(warehouseId, materialId, quantity, null);
    }

    /**
     * 校验库存（支持计量单位转换）
     * 
     * @param warehouseId 仓库ID
     * @param materialId 物料ID
     * @param quantity 出库数量
     * @param unitOfMeasure 计量单位（字典值，如 U101）
     * @return 校验结果
     */
    public Map<String, Object> validateStockWithUnit(String warehouseId, String materialId, BigDecimal quantity, String unitOfMeasure) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 校验参数
            if (StringUtils.isEmpty(warehouseId)) {
                result.put("valid", false);
                result.put("message", "Warehouse ID It cannot be empty.");
                return result;
            }
            if (StringUtils.isEmpty(materialId)) {
                result.put("valid", false);
                result.put("message", "Material ID It cannot be empty.");
                return result;
            }
            if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
                result.put("valid", false);
                result.put("message", "Outbound quantity must be greater than 0");
                return result;
            }

            // 如果提供了计量单位，则计算实际需要的容量（KG或L）
            BigDecimal requiredCapacity = BigDecimal.ZERO;
            BigDecimal requiredVolume = BigDecimal.ZERO;
            String unitType = null;
            
            if (StringUtils.isNotEmpty(unitOfMeasure)) {
                UnitConversionUtil.UnitParseResult parseResult = UnitConversionUtil.calculateTotalAmount(unitOfMeasure, quantity);
                if (!parseResult.isSuccess()) {
                    result.put("valid", false);
                    result.put("message", "Unit of measure parsing failed: " + parseResult.getMessage());
                    return result;
                }
                
                unitType = parseResult.getUnitType();
                if (UnitConversionUtil.UNIT_TYPE_WEIGHT.equals(unitType)) {
                    requiredCapacity = parseResult.getConvertedValue();
                } else if (UnitConversionUtil.UNIT_TYPE_VOLUME.equals(unitType)) {
                    requiredVolume = parseResult.getConvertedValue();
                }
            }

            // 查询可用库存（FIFO）
            List<Stock> availableStocks = stockMapper.selectAvailableStockFIFO(warehouseId, materialId, quantity);

            if (availableStocks == null || availableStocks.isEmpty()) {
                result.put("valid", false);
                result.put("message", "Insufficient inventory");
                result.put("available_quantity", BigDecimal.ZERO);
                result.put("available_capacity_kg", BigDecimal.ZERO);
                result.put("available_volume_l", BigDecimal.ZERO);
                return result;
            }

            // 计算总可用库存数量和容量
            BigDecimal totalAvailableQuantity = BigDecimal.ZERO;
            BigDecimal totalAvailableCapacity = BigDecimal.ZERO;
            BigDecimal totalAvailableVolume = BigDecimal.ZERO;
            
            for (Stock stock : availableStocks) {
                totalAvailableQuantity = totalAvailableQuantity.add(stock.getQuantity());
                if (stock.getCapacity() != null) {
                    totalAvailableCapacity = totalAvailableCapacity.add(stock.getCapacity());
                }
                if (stock.getWarehouseArea() != null) {
                    totalAvailableVolume = totalAvailableVolume.add(stock.getWarehouseArea());
                }
            }

            // 根据是否有计量单位决定校验方式
            boolean isValid;
            String message;
            
            if (StringUtils.isNotEmpty(unitOfMeasure) && unitType != null) {
                // 基于容量/容积校验
                if (UnitConversionUtil.UNIT_TYPE_WEIGHT.equals(unitType)) {
                    isValid = totalAvailableCapacity.compareTo(requiredCapacity) >= 0;
                    if (!isValid) {
                        message = "Inventory capacity(KG) is insufficient，Needed：" + requiredCapacity + " KG，Available：" + totalAvailableCapacity + " KG";
                        result.put("shortage_kg", requiredCapacity.subtract(totalAvailableCapacity));
                    } else {
                        message = "Inventory capacity(KG) is sufficient";
                    }
                    result.put("required_capacity_kg", requiredCapacity);
                    result.put("available_capacity_kg", totalAvailableCapacity);
                } else {
                    isValid = totalAvailableVolume.compareTo(requiredVolume) >= 0;
                    if (!isValid) {
                        message = "Inventory volume(L) is insufficient，Needed：" + requiredVolume + " L，Available：" + totalAvailableVolume + " L";
                        result.put("shortage_l", requiredVolume.subtract(totalAvailableVolume));
                    } else {
                        message = "Inventory volume(L) is sufficient";
                    }
                    result.put("required_volume_l", requiredVolume);
                    result.put("available_volume_l", totalAvailableVolume);
                }
                result.put("unit_type", unitType);
            } else {
                // 基于数量校验（兼容旧逻辑）
                isValid = totalAvailableQuantity.compareTo(quantity) >= 0;
                if (!isValid) {
                    message = "Insufficient inventory，Needed：" + quantity + "，Available：" + totalAvailableQuantity;
                    result.put("shortage", quantity.subtract(totalAvailableQuantity));
                } else {
                    message = "Inventory is sufficient";
                }
            }

            result.put("valid", isValid);
            result.put("message", message);
            result.put("available_quantity", totalAvailableQuantity);
            result.put("required_quantity", quantity);

        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "Batch verification failed：" + e.getMessage());
        }

        return result;
    }

    /**
     * 校验库存（支持计量单位转换和投入品品类）
     * 
     * 计算逻辑：
     * 1. 根据计量单位字典值解析出单位规格（如 Package/50kg = 50kg/包）
     * 2. 计算所需总容量 = 单位规格 * 出库数量（如 50kg * 50 = 2500kg）
     * 3. 根据投入品品类查询库存中该品类的总容量(KG)或容积(L)
     * 4. 比较所需容量与库存容量，判断是否满足出库需求
     * 
     * @param warehouseId 仓库ID
     * @param materialId 物料ID（可为空）
     * @param materialType 投入品类型
     * @param agriculturalInputType 投入品品类
     * @param quantity 出库数量
     * @param unitOfMeasure 计量单位（字典值，如 U103 = Package/50kg）
     * @return 校验结果
     */
    public Map<String, Object> validateStockWithUnitAndCategory(String warehouseId, String materialId, 
            String materialType, String agriculturalInputType, BigDecimal quantity, String unitOfMeasure) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 校验参数
            if (StringUtils.isEmpty(warehouseId)) {
                result.put("valid", false);
                result.put("message", "Warehouse ID cannot be empty");
                return result;
            }
            if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
                result.put("valid", false);
                result.put("message", "Outbound quantity must be greater than 0");
                return result;
            }

            // 如果提供了计量单位，则计算实际需要的容量（KG或L）
            BigDecimal requiredCapacity = BigDecimal.ZERO;
            BigDecimal requiredVolume = BigDecimal.ZERO;
            String unitType = null;
            
            if (StringUtils.isNotEmpty(unitOfMeasure)) {
                UnitConversionUtil.UnitParseResult parseResult = UnitConversionUtil.calculateTotalAmount(unitOfMeasure, quantity);
                if (!parseResult.isSuccess()) {
                    result.put("valid", false);
                    result.put("message", "Unit of measure parsing failed: " + parseResult.getMessage());
                    return result;
                }
                
                unitType = parseResult.getUnitType();
                if (UnitConversionUtil.UNIT_TYPE_WEIGHT.equals(unitType)) {
                    requiredCapacity = parseResult.getConvertedValue();
                } else if (UnitConversionUtil.UNIT_TYPE_VOLUME.equals(unitType)) {
                    requiredVolume = parseResult.getConvertedValue();
                }
            }

            // 查询可用库存 - 优先使用materialId，否则使用投入品类型和品类
            List<Stock> availableStocks;
            if (StringUtils.isNotEmpty(materialId)) {
                // 根据materialId查询库存
                availableStocks = stockMapper.selectAvailableStockFIFO(warehouseId, materialId, quantity);
            } else if (StringUtils.isNotEmpty(materialType) && StringUtils.isNotEmpty(agriculturalInputType)) {
                // 根据投入品类型和品类查询库存
                availableStocks = stockMapper.selectAvailableStockByTypeAndCategory(warehouseId, materialType, agriculturalInputType, quantity);
            } else {
                result.put("valid", false);
                result.put("message", "Material ID or input type and category are required");
                result.put("available_quantity", BigDecimal.ZERO);
                result.put("available_capacity_kg", BigDecimal.ZERO);
                result.put("available_volume_l", BigDecimal.ZERO);
                return result;
            }

            if (availableStocks == null || availableStocks.isEmpty()) {
                result.put("valid", false);
                result.put("message", "Insufficient inventory");
                result.put("available_quantity", BigDecimal.ZERO);
                result.put("available_capacity_kg", BigDecimal.ZERO);
                result.put("available_volume_l", BigDecimal.ZERO);
                result.put("max_available_by_unit", BigDecimal.ZERO);
                return result;
            }

            // 计算总可用库存数量和容量
            BigDecimal totalAvailableQuantity = BigDecimal.ZERO;
            BigDecimal totalAvailableCapacity = BigDecimal.ZERO;
            BigDecimal totalAvailableVolume = BigDecimal.ZERO;
            
            for (Stock stock : availableStocks) {
                totalAvailableQuantity = totalAvailableQuantity.add(stock.getQuantity());
                if (stock.getCapacity() != null) {
                    totalAvailableCapacity = totalAvailableCapacity.add(stock.getCapacity());
                }
                if (stock.getWarehouseArea() != null) {
                    totalAvailableVolume = totalAvailableVolume.add(stock.getWarehouseArea());
                }
            }

            // 根据是否有计量单位决定校验方式
            boolean isValid;
            String message;
            BigDecimal maxAvailableByUnit = BigDecimal.ZERO; // 按计量单位计算的最大可用数量
            BigDecimal unitValue = BigDecimal.ONE; // 单位规格值（如 Package/50kg 的 50）
            
            if (StringUtils.isNotEmpty(unitOfMeasure) && unitType != null) {
                // 获取单位规格值（如 Package/50kg 的 50）
                UnitConversionUtil.UnitParseResult unitParseResult = UnitConversionUtil.parseUnitFromDict(unitOfMeasure);
                if (unitParseResult.isSuccess()) {
                    unitValue = unitParseResult.getConvertedValue();
                }
                
                // 基于容量/容积校验
                if (UnitConversionUtil.UNIT_TYPE_WEIGHT.equals(unitType)) {
                    // 计算按计量单位的最大可用数量 = 库存容量(KG) / 单位规格(KG)
                    // 例如：库存2500KG，单位规格50KG/包 → 最大可用 = 2500/50 = 50包
                    if (unitValue.compareTo(BigDecimal.ZERO) > 0) {
                        maxAvailableByUnit = totalAvailableCapacity.divide(unitValue, 0, java.math.RoundingMode.FLOOR);
                    }
                    
                    isValid = totalAvailableCapacity.compareTo(requiredCapacity) >= 0;
                    if (!isValid) {
                        message = "Inventory capacity (KG) is insufficient. Required: " + requiredCapacity + " KG, Available: " + totalAvailableCapacity + " KG";
                        result.put("shortage_kg", requiredCapacity.subtract(totalAvailableCapacity));
                    } else {
                        message = "Inventory capacity is sufficient";
                    }
                    result.put("required_capacity_kg", requiredCapacity);
                    result.put("available_capacity_kg", totalAvailableCapacity);
                } else {
                    // 计算按计量单位的最大可用数量 = 库存容积(L) / 单位规格(L)
                    // 例如：库存500L，单位规格0.5L/瓶 → 最大可用 = 500/0.5 = 1000瓶
                    if (unitValue.compareTo(BigDecimal.ZERO) > 0) {
                        maxAvailableByUnit = totalAvailableVolume.divide(unitValue, 0, java.math.RoundingMode.FLOOR);
                    }
                    
                    isValid = totalAvailableVolume.compareTo(requiredVolume) >= 0;
                    if (!isValid) {
                        message = "Inventory volume (L) is insufficient. Required: " + requiredVolume + " L, Available: " + totalAvailableVolume + " L";
                        result.put("shortage_l", requiredVolume.subtract(totalAvailableVolume));
                    } else {
                        message = "Inventory volume is sufficient";
                    }
                    result.put("required_volume_l", requiredVolume);
                    result.put("available_volume_l", totalAvailableVolume);
                }
                result.put("unit_type", unitType);
                result.put("unit_value", unitValue); // 单位规格值
                result.put("max_available_by_unit", maxAvailableByUnit);
            } else {
                // 基于数量校验（兼容旧逻辑）
                maxAvailableByUnit = totalAvailableQuantity;
                isValid = totalAvailableQuantity.compareTo(quantity) >= 0;
                if (!isValid) {
                    message = "Insufficient inventory. Required: " + quantity + ", Available: " + totalAvailableQuantity;
                    result.put("shortage", quantity.subtract(totalAvailableQuantity));
                } else {
                    message = "Inventory is sufficient";
                }
                result.put("max_available_by_unit", maxAvailableByUnit);
            }

            result.put("valid", isValid);
            result.put("message", message);
            result.put("available_quantity", totalAvailableQuantity);
            result.put("required_quantity", quantity);

        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "Validation failed: " + e.getMessage());
        }

        return result;
    }

    /**
     * 通过批次号校验库存容量
     * 
     * 计算逻辑：
     * 1. 根据批次号查询库存记录，获取该批次的容量(capacity)或容积(warehouseArea)
     * 2. 根据计量单位字典值解析出单位规格（如 Package/50kg = 50kg/包）
     * 3. 计算所需总容量 = 单位规格 * 出库数量（如 50kg * 50 = 2500kg）
     * 4. 比较所需容量与库存容量，判断是否满足出库需求
     * 5. 计算最大可用数量 = 库存容量 / 单位规格
     * 
     * @param warehouseId 仓库ID
     * @param materialBatchId 批次号（唯一标识）
     * @param quantity 出库数量
     * @param unitOfMeasure 计量单位（字典值，如 U103 = Package/50kg）
     * @return 校验结果
     */
    public Map<String, Object> validateStockByBatchId(String warehouseId, String materialBatchId, 
            BigDecimal quantity, String unitOfMeasure) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 校验参数
            if (StringUtils.isEmpty(warehouseId)) {
                result.put("valid", false);
                result.put("message", "Warehouse ID cannot be empty");
                return result;
            }
            if (StringUtils.isEmpty(materialBatchId)) {
                result.put("valid", false);
                result.put("message", "Material batch ID cannot be empty");
                return result;
            }
            if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
                result.put("valid", false);
                result.put("message", "Outbound quantity must be greater than 0");
                return result;
            }

            // 根据批次号查询库存记录
            LambdaQueryWrapper<Stock> stockWrapper = new LambdaQueryWrapper<>();
            stockWrapper.eq(Stock::getWarehouseId, warehouseId);
            stockWrapper.eq(Stock::getMaterialBatchId, materialBatchId);
            stockWrapper.eq(Stock::getStatus, "0"); // 正常状态
            Stock stock = stockMapper.selectOne(stockWrapper);

            if (stock == null) {
                result.put("valid", false);
                result.put("message", "Stock record not found for batch: " + materialBatchId);
                result.put("available_quantity", BigDecimal.ZERO);
                result.put("available_capacity_kg", BigDecimal.ZERO);
                result.put("available_volume_l", BigDecimal.ZERO);
                result.put("max_available_by_unit", BigDecimal.ZERO);
                return result;
            }

            // 获取库存信息
            String materialName = stock.getMaterialName();
            BigDecimal availableQuantity = stock.getQuantity() != null ? stock.getQuantity() : BigDecimal.ZERO;
            BigDecimal availableCapacity = stock.getCapacity() != null ? stock.getCapacity() : BigDecimal.ZERO;
            BigDecimal availableVolume = stock.getWarehouseArea() != null ? stock.getWarehouseArea() : BigDecimal.ZERO;

            result.put("material_name", materialName);
            result.put("available_quantity", availableQuantity);

            // 如果提供了计量单位，则计算实际需要的容量（KG或L）
            BigDecimal requiredCapacity = BigDecimal.ZERO;
            BigDecimal requiredVolume = BigDecimal.ZERO;
            String unitType = null;
            BigDecimal unitValue = BigDecimal.ONE; // 单位规格值
            
            if (StringUtils.isNotEmpty(unitOfMeasure)) {
                UnitConversionUtil.UnitParseResult parseResult = UnitConversionUtil.calculateTotalAmount(unitOfMeasure, quantity);
                if (!parseResult.isSuccess()) {
                    result.put("valid", false);
                    result.put("message", "Unit of measure parsing failed: " + parseResult.getMessage());
                    return result;
                }
                
                unitType = parseResult.getUnitType();
                if (UnitConversionUtil.UNIT_TYPE_WEIGHT.equals(unitType)) {
                    requiredCapacity = parseResult.getConvertedValue();
                } else if (UnitConversionUtil.UNIT_TYPE_VOLUME.equals(unitType)) {
                    requiredVolume = parseResult.getConvertedValue();
                }

                // 获取单位规格值
                UnitConversionUtil.UnitParseResult unitParseResult = UnitConversionUtil.parseUnitFromDict(unitOfMeasure);
                if (unitParseResult.isSuccess()) {
                    unitValue = unitParseResult.getConvertedValue();
                }
            }

            // 根据是否有计量单位决定校验方式
            boolean isValid;
            String message;
            BigDecimal maxAvailableByUnit = BigDecimal.ZERO;
            
            if (StringUtils.isNotEmpty(unitOfMeasure) && unitType != null) {
                // 基于容量/容积校验
                if (UnitConversionUtil.UNIT_TYPE_WEIGHT.equals(unitType)) {
                    // 计算按计量单位的最大可用数量 = 库存容量(KG) / 单位规格(KG)
                    if (unitValue.compareTo(BigDecimal.ZERO) > 0) {
                        maxAvailableByUnit = availableCapacity.divide(unitValue, 0, java.math.RoundingMode.FLOOR);
                    }
                    
                    isValid = availableCapacity.compareTo(requiredCapacity) >= 0;
                    if (!isValid) {
                        message = "Inventory capacity (KG) is insufficient. Required: " + requiredCapacity + " KG, Available: " + availableCapacity + " KG";
                        result.put("shortage_kg", requiredCapacity.subtract(availableCapacity));
                    } else {
                        message = "Inventory capacity is sufficient";
                    }
                    result.put("required_capacity_kg", requiredCapacity);
                    result.put("available_capacity_kg", availableCapacity);
                } else {
                    // 计算按计量单位的最大可用数量 = 库存容积(L) / 单位规格(L)
                    if (unitValue.compareTo(BigDecimal.ZERO) > 0) {
                        maxAvailableByUnit = availableVolume.divide(unitValue, 0, java.math.RoundingMode.FLOOR);
                    }
                    
                    isValid = availableVolume.compareTo(requiredVolume) >= 0;
                    if (!isValid) {
                        message = "Inventory volume (L) is insufficient. Required: " + requiredVolume + " L, Available: " + availableVolume + " L";
                        result.put("shortage_l", requiredVolume.subtract(availableVolume));
                    } else {
                        message = "Inventory volume is sufficient";
                    }
                    result.put("required_volume_l", requiredVolume);
                    result.put("available_volume_l", availableVolume);
                }
                result.put("unit_type", unitType);
                result.put("unit_value", unitValue);
                result.put("max_available_by_unit", maxAvailableByUnit);
            } else {
                // 基于数量校验（兼容旧逻辑）
                maxAvailableByUnit = availableQuantity;
                isValid = availableQuantity.compareTo(quantity) >= 0;
                if (!isValid) {
                    message = "Insufficient inventory. Required: " + quantity + ", Available: " + availableQuantity;
                    result.put("shortage", quantity.subtract(availableQuantity));
                } else {
                    message = "Inventory is sufficient";
                }
                result.put("max_available_by_unit", maxAvailableByUnit);
            }

            result.put("valid", isValid);
            result.put("message", message);
            result.put("required_quantity", quantity);

        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "Validation failed: " + e.getMessage());
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
                result.put("message", "The warehouse ID cannot be empty");
                return result;
            }
            if (details == null || details.isEmpty()) {
                result.put("valid", false);
                result.put("message", "The outbound details cannot be empty");
                return result;
            }

            // 逐个校验每个明细
            for (Map<String, Object> detail : details) {
                String materialBatchId = detail.get("materialBatchId") != null ? detail.get("materialBatchId").toString() : null;
                BigDecimal quantity = detail.get("quantity") != null ? new BigDecimal(detail.get("quantity").toString()) : BigDecimal.ZERO;
                String unitOfMeasure = detail.get("unitOfMeasure") != null ? detail.get("unitOfMeasure").toString() : null;

                if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                // 调用通过批次号校验库存的方法
                Map<String, Object> validateResult = validateStockByBatchId(warehouseId, materialBatchId, quantity, unitOfMeasure);

                if (!(Boolean) validateResult.get("valid")) {
                    allValid = false;
                    Map<String, Object> insufficientItem = new HashMap<>();
                    insufficientItem.put("material_batch_id", materialBatchId);
                    insufficientItem.put("material_name", validateResult.get("material_name"));
                    insufficientItem.put("required_quantity", quantity);
                    insufficientItem.put("unit_of_measure", unitOfMeasure);
                    insufficientItem.put("available_quantity", validateResult.get("available_quantity"));
                    insufficientItem.put("message", validateResult.get("message"));
                    
                    // 添加容量/容积相关信息
                    if (validateResult.get("unit_type") != null) {
                        insufficientItem.put("unit_type", validateResult.get("unit_type"));
                        insufficientItem.put("max_available_by_unit", validateResult.get("max_available_by_unit"));
                        if (UnitConversionUtil.UNIT_TYPE_WEIGHT.equals(validateResult.get("unit_type"))) {
                            insufficientItem.put("required_capacity_kg", validateResult.get("required_capacity_kg"));
                            insufficientItem.put("available_capacity_kg", validateResult.get("available_capacity_kg"));
                            insufficientItem.put("shortage_kg", validateResult.get("shortage_kg"));
                        } else {
                            insufficientItem.put("required_volume_l", validateResult.get("required_volume_l"));
                            insufficientItem.put("available_volume_l", validateResult.get("available_volume_l"));
                            insufficientItem.put("shortage_l", validateResult.get("shortage_l"));
                        }
                    } else {
                        insufficientItem.put("shortage", validateResult.get("shortage"));
                        insufficientItem.put("max_available_by_unit", validateResult.get("max_available_by_unit"));
                    }
                    
                    insufficientItems.add(insufficientItem);
                }
            }

            // 构建返回结果
            result.put("valid", allValid);
            if (allValid) {
                result.put("message", "All Materials are in sufficient stock");
            } else {
                result.put("message", "Partial MaterialsInsufficient inventory");
                result.put("insufficient_items", insufficientItems);
            }

        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "Batch verification failed：" + e.getMessage());
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
                result.put("message", "Release order ID cannot be empty");
                return result;
            }
            if (StringUtils.isEmpty(warehouseId)) {
                result.put("valid", false);
                result.put("message", "Warehouse ID cannot be empty");
                return result;
            }

            // 查询分发单主表信息
            Map<String, Object> releaseMain = outboundOrderMapper.selectReleaseMainById(releaseId);
            if (releaseMain == null) {
                result.put("valid", false);
                result.put("message", "Release order does not exist");
                return result;
            }

            // 查询分发单明细（包含投入品的农资类型和品种）
            List<Map<String, Object>> releaseDetails = outboundOrderMapper.selectReleaseDetailsByReleaseId(releaseId);

            if (releaseDetails == null || releaseDetails.isEmpty()) {
                result.put("valid", false);
                result.put("message", "Release order details do not exist");
                return result;
            }

            // 校验每个投入品的库存
            List<Map<String, Object>> insufficientItems = new ArrayList<>();
            List<Map<String, Object>> enrichedDetails = new ArrayList<>();
            boolean allValid = true;

            for (Map<String, Object> detail : releaseDetails) {
                Long inputId = detail.get("input_id") != null ? Long.parseLong(detail.get("input_id").toString()) : null;
                String inputName = detail.get("input_name") != null ? detail.get("input_name").toString() : "";
                // 优先使用明细表中的input_type和input_category，如果没有则使用投入品表中的字段
                String inputType = detail.get("input_type") != null ? detail.get("input_type").toString() : 
                                 (detail.get("input_type_from_input") != null ? detail.get("input_type_from_input").toString() : "");
                String agriculturalInputType = detail.get("input_category") != null ? detail.get("input_category").toString() : 
                                            (detail.get("agricultural_input_type_from_input") != null ? detail.get("agricultural_input_type_from_input").toString() : "");
                String variety = detail.get("input_variety") != null ? detail.get("input_variety").toString() : "";
                BigDecimal required = detail.get("required") != null ? new BigDecimal(detail.get("required").toString()) : BigDecimal.ZERO;

                // 检查是否提供了投入品类型和投入品品类
                if (StringUtils.isEmpty(inputType) || StringUtils.isEmpty(agriculturalInputType) || required.compareTo(BigDecimal.ZERO) <= 0) {
                    enrichedDetails.add(detail);
                    continue;
                }

                // 根据投入品类型和投入品品类查询库存
                // 这里需要一个新的方法来查询符合条件的库存
                List<Stock> availableStocks = stockMapper.selectAvailableStockByTypeAndCategory(
                        warehouseId,
                        inputType,
                        agriculturalInputType,
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
                    insufficientItem.put("message", "Insufficient inventory，Needed：" + required + "，Available：" + totalAvailable);
                    insufficientItems.add(insufficientItem);
                }

                enrichedDetails.add(enrichedDetail);
            }

            // 构建返回结果 - 按照前端期望的格式
            result.put("main", releaseMain);
            result.put("details", enrichedDetails);
            result.put("valid", allValid);

            if (allValid) {
                result.put("message", "All inputs have sufficient stock");
            } else {
                result.put("message", "Partial inputs have insufficient stock");
                result.put("insufficient_items", insufficientItems);
            }

        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "Batch verification failed：" + e.getMessage());
        }

        return result;
    }
}