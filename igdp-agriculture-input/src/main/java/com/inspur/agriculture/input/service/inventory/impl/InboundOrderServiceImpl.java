package com.inspur.agriculture.input.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.agriculture.input.domain.inventory.InboundOrder;
import com.inspur.agriculture.input.domain.inventory.InboundOrderDetail;
import com.inspur.agriculture.input.domain.inventory.Stock;
import com.inspur.agriculture.input.domain.inventory.StockLog;
import com.inspur.agriculture.input.domain.inventory.Warehouse;
import com.inspur.agriculture.input.mapper.inventory.*;
import com.inspur.agriculture.input.service.inventory.IBatchService;
import com.inspur.agriculture.input.service.inventory.IInboundOrderService;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 入库单服务实现类
 *
 * @author igdp
 */
@Service
public class InboundOrderServiceImpl implements IInboundOrderService {

    @Autowired
    private InboundOrderMapper inboundOrderMapper;

    @Autowired
    private InboundOrderDetailMapper inboundOrderDetailMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockLogMapper stockLogMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private IBatchService batchService;

    @Autowired
    private OutboundOrderMapper outboundOrderMapper;

    @Override
    public List<Map<String, Object>> selectInboundOrderList(Map<String, Object> params) {
        return inboundOrderMapper.selectInboundOrderList(params);
    }

    @Override
    public Map<String, Object> selectInboundOrderById(String inboundOrderId) {
        if (StringUtils.isEmpty(inboundOrderId)) {
            throw new ServiceException("入库单ID不能为空");
        }
        Map<String, Object> result = inboundOrderMapper.selectInboundOrderById(inboundOrderId);
        if (result == null) {
            throw new ServiceException("入库单不存在");
        }
        // 查询明细
        List<Map<String, Object>> details = inboundOrderDetailMapper.selectDetailsByOrderId(inboundOrderId);
        result.put("details", details);
        
        // 确保表单备注和审核意见正确返回
        // 表单备注使用 formRemark 字段
        // 审核意见使用 remark 字段，并在前端通过 audit_remark 显示
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createInboundOrder(InboundOrder inboundOrder, List<Map<String, Object>> details) {
        // 校验参数
        if (inboundOrder == null) {
            throw new ServiceException("入库单信息不能为空");
        }
        if (details == null || details.isEmpty()) {
            throw new ServiceException("入库明细不能为空");
        }

        // 校验仓库是否存在
        if (StringUtils.isEmpty(inboundOrder.getWarehouseId())) {
            throw new ServiceException("仓库ID不能为空");
        }
        Warehouse warehouse = warehouseMapper.selectById(Long.valueOf(inboundOrder.getWarehouseId()));
        if (warehouse == null) {
            throw new ServiceException("仓库不存在，请先配置仓库");
        }
        if (!"1".equals(warehouse.getStatus())) {
            throw new ServiceException("仓库已停用，无法入库");
        }

        // 生成入库单ID和批次号
        String inboundOrderId = "INB-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String inboundBatchId = "BATCH-INB-" + System.currentTimeMillis();

        // 设置入库单信息
        inboundOrder.setId(UUID.randomUUID().toString().replace("-", ""));
        inboundOrder.setInboundOrderId(inboundOrderId);
        inboundOrder.setInboundBatchId(inboundBatchId);
        inboundOrder.setInboundStatus("pending");
        inboundOrder.setCreatedAt(new Date());
        inboundOrder.setUpdatedAt(new Date());

        // 设置入库员为当前登录人
        String currentUser = SecurityUtils.getUsername();
        if (StringUtils.isNotEmpty(currentUser)) {
            inboundOrder.setInboundUser(currentUser);
        }

        // 计算总数量
        BigDecimal totalQuantity = BigDecimal.ZERO;
        for (Map<String, Object> detail : details) {
            BigDecimal quantity = new BigDecimal(detail.get("quantity").toString());
            totalQuantity = totalQuantity.add(quantity);
        }
        inboundOrder.setTotalQuantity(totalQuantity);

        // 插入入库单
        inboundOrderMapper.insert(inboundOrder);

        // 插入入库明细并自动生成批次号
        List<InboundOrderDetail> detailList = new ArrayList<>();
        int sequenceNum = 1;
        for (Map<String, Object> detail : details) {
            InboundOrderDetail detailEntity = new InboundOrderDetail();
            detailEntity.setId(UUID.randomUUID().toString().replace("-", ""));
            detailEntity.setDetailId("DET-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
            detailEntity.setInboundOrderId(inboundOrderId);
            detailEntity.setMaterialId(detail.get("materialId").toString());

            // 自动生成入库批次号：BATCH-入库单ID-序号
            String autoBatchNo = "BATCH-" + inboundOrderId + "-" + String.format("%03d", sequenceNum++);
            detailEntity.setBatchNo(autoBatchNo);

            // 设置生产批次号（手动填写）
            if (detail.get("productionBatchNo") != null) {
                detailEntity.setProductionBatchNo(detail.get("productionBatchNo").toString());
            }

            detailEntity.setMaterialName(detail.get("materialName").toString());
            detailEntity.setMaterialType(detail.get("materialType").toString());
            detailEntity.setQuantity(new BigDecimal(detail.get("quantity").toString()));
            detailEntity.setSpecModel(detail.get("specModel") != null ? detail.get("specModel").toString() : null);
            detailEntity.setUnitOfMeasure(detail.get("unitOfMeasure") != null ? detail.get("unitOfMeasure").toString() : null);
            detailEntity.setExpiryDate((Date) detail.get("expiryDate"));
            detailEntity.setAgriculturalInputType(detail.get("agriculturalInputType") != null ? detail.get("agriculturalInputType").toString() : null);
            detailEntity.setVariety(detail.get("variety") != null ? detail.get("variety").toString() : null);
            detailEntity.setOperator(inboundOrder.getOperator());
            detailEntity.setCreatedAt(new Date());
            detailEntity.setUpdatedAt(new Date());
            detailList.add(detailEntity);
        }
        inboundOrderDetailMapper.batchInsert(detailList);

        // 注：库存同步逻辑已移至审批通过时执行，此处不再更新库存

        return inboundOrderId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditInboundOrder(String inboundOrderId, String auditStatus, String auditUser, Date auditTime, String remark) {
        // 校验参数
        if (StringUtils.isEmpty(inboundOrderId)) {
            throw new ServiceException("入库单ID不能为空");
        }
        if (StringUtils.isEmpty(auditStatus)) {
            throw new ServiceException("审核状态不能为空");
        }
        if (!Arrays.asList("approved", "rejected").contains(auditStatus)) {
            throw new ServiceException("审核状态无效");
        }

        // 查询入库单
        LambdaQueryWrapper<InboundOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InboundOrder::getInboundOrderId, inboundOrderId);
        InboundOrder inboundOrder = inboundOrderMapper.selectOne(wrapper);
        if (inboundOrder == null) {
            throw new ServiceException("入库单不存在");
        }

        // 校验状态
        if (!"pending".equals(inboundOrder.getInboundStatus())) {
            throw new ServiceException("只有待审核状态的入库单才能审核");
        }

        // 更新入库单
        // 设置审核人为当前登录人
        String currentUser = SecurityUtils.getUsername();
        inboundOrder.setAuditUser(StringUtils.isNotEmpty(currentUser) ? currentUser : auditUser);
        inboundOrder.setAuditTime(auditTime != null ? auditTime : new Date());
        // 只更新审核意见，不影响表单备注
        inboundOrder.setRemark(remark);
        inboundOrder.setUpdatedAt(new Date());

        // 根据审核结果更新状态
        if ("approved".equals(auditStatus)) {
            inboundOrder.setInboundStatus("approved");
        } else {
            inboundOrder.setInboundStatus("rejected");
        }

        return inboundOrderMapper.updateById(inboundOrder) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> confirmInbound(String inboundOrderId, Date inboundTime, String operator) {
        // 校验参数
        if (StringUtils.isEmpty(inboundOrderId)) {
            throw new ServiceException("The ID of the inbound order cannot be empty");
        }

        // 查询入库单
        LambdaQueryWrapper<InboundOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InboundOrder::getInboundOrderId, inboundOrderId);
        InboundOrder inboundOrder = inboundOrderMapper.selectOne(wrapper);
        if (inboundOrder == null) {
            throw new ServiceException("The warehouse receipt does not exist");
        }

        // 校验状态（必须是已审核状态）
        if (!"approved".equals(inboundOrder.getInboundStatus())) {
            throw new ServiceException("Only the warehouse entry form that has been reviewed can be executed for entry");
        }

        // 校验仓库
        Warehouse warehouse = warehouseMapper.selectById(Long.valueOf(inboundOrder.getWarehouseId()));
        if (warehouse == null) {
            throw new ServiceException("The warehouse doesn't exist.");
        }
        if (!"1".equals(warehouse.getStatus())) {
            throw new ServiceException("The warehouse is out of service and no goods can be stored");
        }

        // 校验仓库容量
        BigDecimal usedCapacity = warehouse.getUsedCapacity() != null ? warehouse.getUsedCapacity() : BigDecimal.ZERO;
        BigDecimal totalQuantity = inboundOrder.getTotalQuantity();
        BigDecimal availableCapacity = warehouse.getCapacity().subtract(usedCapacity);
        if (availableCapacity.compareTo(totalQuantity) < 0) {
            throw new ServiceException("The warehouse capacity is insufficient. Available capacity：" + availableCapacity + "，Required capacity：" + totalQuantity);
        }

        // 查询入库明细
        LambdaQueryWrapper<InboundOrderDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(InboundOrderDetail::getInboundOrderId, inboundOrderId);
        List<InboundOrderDetail> details = inboundOrderDetailMapper.selectList(detailWrapper);

        List<Map<String, Object>> updatedStock = new ArrayList<>();

        // 确认入库时同步更新库存（并发安全）
        for (InboundOrderDetail detail : details) {
            try {
                // 使用悲观锁查询库存，防止并发修改异常
                Stock existStock = stockMapper.selectByBatchForUpdate(
                        inboundOrder.getWarehouseId(),
                        detail.getMaterialId(),
                        detail.getBatchNo()
                );

                BigDecimal beforeQuantity = BigDecimal.ZERO;
                BigDecimal afterQuantity = detail.getQuantity();

                if (existStock != null) {
                    // 更新现有库存
                    beforeQuantity = existStock.getQuantity();
                    afterQuantity = beforeQuantity.add(detail.getQuantity());

                    existStock.setQuantity(afterQuantity);
                    existStock.setInboundQuantity(
                            (existStock.getInboundQuantity() != null ? existStock.getInboundQuantity() : BigDecimal.ZERO)
                                    .add(detail.getQuantity())
                    );
                    existStock.setExpiryDate(detail.getExpiryDate());
                    // 更新投入品类型和投入品品类
                    existStock.setMaterialType(detail.getMaterialType());
                    existStock.setAgriculturalInputType(detail.getAgriculturalInputType());
                    existStock.setUpdatedAt(new Date());
                    stockMapper.updateById(existStock);
                } else {
                    // 创建新库存记录
                    Stock newStock = new Stock();
                    newStock.setId(UUID.randomUUID().toString().replace("-", ""));
                    newStock.setWarehouseId(inboundOrder.getWarehouseId());
                    newStock.setWarehouseName(warehouse.getWarehouseName());
                    newStock.setMaterialId(detail.getMaterialId());
                    newStock.setMaterialBatchId(detail.getBatchNo());
                    newStock.setMaterialName(detail.getMaterialName());
                    // 保存投入品类型和投入品品类
                    newStock.setMaterialType(detail.getMaterialType());
                    newStock.setAgriculturalInputType(detail.getAgriculturalInputType());
                    newStock.setQuantity(detail.getQuantity());
                    newStock.setInboundQuantity(detail.getQuantity());
                    newStock.setOutboundQuantity(BigDecimal.ZERO);
                    newStock.setExpiryDate(detail.getExpiryDate());
                    newStock.setStatus("0");
                    newStock.setCreatedAt(new Date());
                    newStock.setUpdatedAt(new Date());
                    stockMapper.insert(newStock);
                }

                // 记录库存变动日志
                StockLog stockLog = new StockLog();
                stockLog.setId(UUID.randomUUID().toString().replace("-", ""));
                stockLog.setWarehouseId(inboundOrder.getWarehouseId());
                stockLog.setMaterialId(detail.getMaterialId());
                stockLog.setMaterialBatchId(detail.getBatchNo());
                stockLog.setOperationType("inbound");
                stockLog.setChangeQuantity(detail.getQuantity());
                stockLog.setBeforeQuantity(beforeQuantity);
                stockLog.setAfterQuantity(afterQuantity);
                stockLog.setReferenceOrderId(inboundOrderId);
                stockLog.setOperator(operator);
                stockLog.setCreatedAt(new Date());
                stockLogMapper.insert(stockLog);

            } catch (Exception e) {
                throw new ServiceException("Inventory synchronization failed - Input ID: " + detail.getMaterialId()
                        + ", Batch: " + detail.getBatchNo() + ", Error: " + e.getMessage());
            }
        }

        // 处理每个明细（生成二维码并更新库存中的二维码）
        for (InboundOrderDetail detail : details) {
            // 使用用户选择的批次号（从投入品目录中选择）
            String batchNo = detail.getBatchNo();
            if (StringUtils.isEmpty(batchNo)) {
                throw new ServiceException("The batch number of the input product cannot be empty");
            }

            // 生成二维码
            String qrCode = batchService.generateQrCode(
                    detail.getMaterialId(),
                    batchNo,
                    inboundOrder.getWarehouseId(),
                    detail.getExpiryDate(),
                    detail.getQuantity()
            );

            // 更新明细
            detail.setQrCode(qrCode);
            detail.setInboundTime(inboundTime != null ? inboundTime : new Date());
            detail.setUpdatedAt(new Date());
            inboundOrderDetailMapper.updateById(detail);

            // 更新库存中的二维码（库存记录在确认入库时已创建）
            Stock existStock = stockMapper.selectByBatch(
                    inboundOrder.getWarehouseId(),
                    detail.getMaterialId(),
                    batchNo
            );

            if (existStock != null) {
                existStock.setQrCode(qrCode);
                existStock.setUpdatedAt(new Date());
                stockMapper.updateById(existStock);

                // 添加到返回结果
                Map<String, Object> stockInfo = new HashMap<>();
                stockInfo.put("material_id", detail.getMaterialId());
                stockInfo.put("warehouse_id", inboundOrder.getWarehouseId());
                stockInfo.put("batch_id", batchNo);
                stockInfo.put("new_quantity", existStock.getQuantity());
                updatedStock.add(stockInfo);
            }
        }

            outboundOrderMapper.updateReceiveUnionStatus(
                    inboundOrder.getRelatedOrderNo(),
                    "HasBeenWarehoused"
            );
            outboundOrderMapper.updateReceiveWoredaStatus(
                    inboundOrder.getRelatedOrderNo(),
                    "HasBeenWarehoused"
            );

        // 更新仓库已用容量
        try {
            warehouseMapper.updateUsedCapacity(Long.valueOf(inboundOrder.getWarehouseId()), totalQuantity);
        } catch (Exception e) {
            throw new ServiceException("Failed to update the warehouse capacity: " + e.getMessage());
        }

        // 更新入库单状态
        inboundOrder.setInboundStatus("completed");
        inboundOrder.setInboundTime(inboundTime != null ? inboundTime : new Date());
        inboundOrder.setInboundUser(operator != null ? operator : inboundOrder.getOperator());
        inboundOrder.setUpdatedAt(new Date());
        inboundOrderMapper.updateById(inboundOrder);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("inbound_order_id", inboundOrderId);
        result.put("updated_stock", updatedStock);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelInboundOrder(String inboundOrderId, String operator) {
        if (StringUtils.isEmpty(inboundOrderId)) {
            throw new ServiceException("The ID of the inbound order cannot be empty");
        }

        LambdaQueryWrapper<InboundOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InboundOrder::getInboundOrderId, inboundOrderId);
        InboundOrder inboundOrder = inboundOrderMapper.selectOne(wrapper);

        if (inboundOrder == null) {
            throw new ServiceException("The warehouse receipt does not exist");
        }

        if ("completed".equals(inboundOrder.getInboundStatus())) {
            throw new ServiceException("Completed warehouse entry forms cannot be cancelled");
        }

        inboundOrder.setInboundStatus("cancelled");
        inboundOrder.setUpdatedAt(new Date());
        return inboundOrderMapper.updateById(inboundOrder) > 0;
    }

    @Override
    public int countInboundOrders(Map<String, Object> params) {
        return inboundOrderMapper.countInboundOrders(params);
    }

    @Override
    public int countPendingOrders(String warehouseId) {
        return inboundOrderMapper.countPendingOrders(warehouseId);
    }

    @Override
    public List<Map<String, Object>> countByStatus(String warehouseId) {
        return inboundOrderMapper.countByStatus(warehouseId);
    }

    @Override
    public List<Map<String, Object>> countByType(String startDate, String endDate) {
        return inboundOrderMapper.countByType(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> selectReleaseOrderList() {
        return inboundOrderMapper.selectReleaseOrderList();
    }
    
    /**
     * 根据分发单ID获取分发投入品明细并匹配库存
     * 匹配规则：根据投入品类型和品类匹配库存中的投入品
     *
     * @param releaseId   分发单ID
     * @param warehouseId 仓库ID
     * @return 分发投入品明细及匹配的库存信息
     */
    @Override
    public Map<String, Object> matchReleaseStock(String releaseId, String warehouseId) {
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
            
            // 查询分发单明细（包含投入品的类型和品类）
            List<Map<String, Object>> releaseDetails = inboundOrderMapper.selectReleaseDetailsByReleaseId(releaseId);
            
            if (releaseDetails == null || releaseDetails.isEmpty()) {
                result.put("valid", false);
                result.put("message", "分发单明细不存在");
                return result;
            }
            
            // 匹配每个投入品类型的库存
            List<Map<String, Object>> matchedDetails = new ArrayList<>();
            
            for (Map<String, Object> detail : releaseDetails) {
                // 获取投入品类型和品类
                String inputType = (String) detail.get("input_type");
                String agriculturalInputType = (String) detail.get("input_category");
                BigDecimal required = (BigDecimal) detail.get("required");
                
                // 如果required为空，使用quantity字段
                if (required == null) {
                    required = (BigDecimal) detail.get("quantity");
                }
                
                // 检查是否提供了投入品类型和投入品品类
                if (StringUtils.isEmpty(inputType) || StringUtils.isEmpty(agriculturalInputType) || required == null || required.compareTo(BigDecimal.ZERO) <= 0) {
                    // 添加未匹配的明细
                    Map<String, Object> unmatchedDetail = new HashMap<>(detail);
                    unmatchedDetail.put("matched", false);
                    unmatchedDetail.put("message", "缺少必要的匹配信息");
                    matchedDetails.add(unmatchedDetail);
                    continue;
                }
                
                // 根据投入品类型和投入品品类查询库存
                List<Stock> availableStocks = stockMapper.selectAvailableStockByTypeAndCategory(
                        warehouseId,
                        inputType,
                        agriculturalInputType,
                        required
                );
                
                // 将匹配结果添加到明细中
                Map<String, Object> matchedDetail = new HashMap<>(detail);
                matchedDetail.put("matched", !availableStocks.isEmpty());
                matchedDetail.put("availableStocks", availableStocks);
                
                if (!availableStocks.isEmpty()) {
                    matchedDetail.put("message", "找到匹配的库存");
                } else {
                    matchedDetail.put("message", "未找到匹配的库存");
                }
                
                matchedDetails.add(matchedDetail);
            }
            
            result.put("valid", true);
            result.put("details", matchedDetails);
            result.put("message", "匹配完成");
            
        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "匹配过程中发生错误: " + e.getMessage());
        }
        
        return result;
    }
}
