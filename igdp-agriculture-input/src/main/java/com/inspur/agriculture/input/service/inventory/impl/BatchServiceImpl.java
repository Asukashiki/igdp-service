package com.inspur.agriculture.input.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.agriculture.input.domain.inventory.InboundOrderDetail;
import com.inspur.agriculture.input.domain.inventory.OutboundBatchSplit;
import com.inspur.agriculture.input.domain.inventory.Stock;
import com.inspur.agriculture.input.domain.inventory.StockLog;
import com.inspur.agriculture.input.mapper.inventory.InboundOrderDetailMapper;
import com.inspur.agriculture.input.mapper.inventory.OutboundBatchSplitMapper;
import com.inspur.agriculture.input.mapper.inventory.StockLogMapper;
import com.inspur.agriculture.input.mapper.inventory.StockMapper;
import com.inspur.agriculture.input.service.inventory.IBatchService;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 批次管理服务实现类
 *
 * @author igdp
 */
@Service
public class BatchServiceImpl implements IBatchService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockLogMapper stockLogMapper;

    @Autowired
    private InboundOrderDetailMapper inboundOrderDetailMapper;

    @Autowired
    private OutboundBatchSplitMapper outboundBatchSplitMapper;

    @Override
    public String generateBatchId(String materialId, String warehouseId, Integer inboundType,
                                   BigDecimal quantity, Date inboundTime) {
        // 批次号生成规则：BATCH-{物料ID}-{日期时间戳}-{序号}
        if (StringUtils.isEmpty(materialId)) {
            throw new ServiceException("物料ID不能为空");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(inboundTime != null ? inboundTime : new Date());

        // 生成随机序号（6位）
        Random random = new Random();
        int sequence = random.nextInt(999999);
        String sequenceStr = String.format("%06d", sequence);

        // 组装批次号
        return "BATCH-" + materialId + "-" + timestamp + "-" + sequenceStr;
    }

    @Override
    public String generateQrCode(String materialId, String materialBatchId, String warehouseId,
                                  Date expiryDate, BigDecimal quantity) {
        // 二维码内容包含：物料信息、批次号、仓库ID、有效期等
        if (StringUtils.isEmpty(materialId)) {
            throw new ServiceException("物料ID不能为空");
        }
        if (StringUtils.isEmpty(materialBatchId)) {
            throw new ServiceException("批次ID不能为空");
        }
        if (StringUtils.isEmpty(warehouseId)) {
            throw new ServiceException("仓库ID不能为空");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String expiryDateStr = expiryDate != null ? sdf.format(expiryDate) : "N/A";

        // 使用JSON格式组装二维码内容
        StringBuilder qrCode = new StringBuilder();
        qrCode.append("{");
        qrCode.append("\"materialId\":\"").append(materialId).append("\",");
        qrCode.append("\"batchId\":\"").append(materialBatchId).append("\",");
        qrCode.append("\"warehouseId\":\"").append(warehouseId).append("\",");
        qrCode.append("\"expiryDate\":\"").append(expiryDateStr).append("\",");
        qrCode.append("\"quantity\":").append(quantity != null ? quantity : 0);
        qrCode.append("}");

        return qrCode.toString();
    }

    @Override
    public Map<String, Object> parseQrCode(String qrCode) {
        if (StringUtils.isEmpty(qrCode)) {
            throw new ServiceException("二维码不能为空");
        }

        Map<String, Object> result = new HashMap<>();

        try {
            // 简单的JSON解析（实际项目中应使用JSON库如Jackson或Fastjson）
            qrCode = qrCode.trim();
            if (qrCode.startsWith("{") && qrCode.endsWith("}")) {
                qrCode = qrCode.substring(1, qrCode.length() - 1);
                String[] pairs = qrCode.split(",");
                for (String pair : pairs) {
                    String[] keyValue = pair.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].replaceAll("\"", "").trim();
                        String value = keyValue[1].replaceAll("\"", "").trim();
                        result.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException("二维码格式错误");
        }

        return result;
    }

    @Override
    public boolean validateBatch(String materialBatchId, String warehouseId, String materialId) {
        if (StringUtils.isEmpty(materialBatchId)) {
            throw new ServiceException("批次ID不能为空");
        }
        if (StringUtils.isEmpty(warehouseId)) {
            throw new ServiceException("仓库ID不能为空");
        }
        if (StringUtils.isEmpty(materialId)) {
            throw new ServiceException("物料ID不能为空");
        }

        // 查询库存
        Stock stock = stockMapper.selectByBatch(warehouseId, materialId, materialBatchId);
        if (stock == null) {
            return false;
        }

        // 检查是否过期
        if (stock.getExpiryDate() != null && stock.getExpiryDate().before(new Date())) {
            return false;
        }

        // 检查库存是否大于0
        return stock.getQuantity().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public Map<String, Object> traceBatch(String materialBatchId) {
        if (StringUtils.isEmpty(materialBatchId)) {
            throw new ServiceException("批次ID不能为空");
        }

        Map<String, Object> result = new HashMap<>();

        // 1. 查询入库明细（批次来源）
        InboundOrderDetail inboundDetail = inboundOrderDetailMapper.selectByBatchId(materialBatchId);
        if (inboundDetail != null) {
            Map<String, Object> inboundInfo = new HashMap<>();
            inboundInfo.put("inbound_order_id", inboundDetail.getInboundOrderId());
            inboundInfo.put("material_id", inboundDetail.getMaterialId());
            inboundInfo.put("material_type", inboundDetail.getMaterialType());
            inboundInfo.put("quantity", inboundDetail.getQuantity());
            inboundInfo.put("inbound_time", inboundDetail.getInboundTime());
            inboundInfo.put("expiry_date", inboundDetail.getExpiryDate());
            inboundInfo.put("qr_code", inboundDetail.getQrCode());
            result.put("inbound_info", inboundInfo);
        }

        // 2. 查询当前库存
        if (inboundDetail != null) {
            LambdaQueryWrapper<Stock> stockWrapper = new LambdaQueryWrapper<>();
            stockWrapper.eq(Stock::getMaterialBatchId, materialBatchId);
            stockWrapper.eq(Stock::getMaterialId, inboundDetail.getMaterialId());
            Stock stock = stockMapper.selectOne(stockWrapper);
            if (stock != null) {
                Map<String, Object> stockInfo = new HashMap<>();
                stockInfo.put("warehouse_id", stock.getWarehouseId());
                stockInfo.put("current_quantity", stock.getQuantity());
                stockInfo.put("inbound_quantity", stock.getInboundQuantity());
                stockInfo.put("outbound_quantity", stock.getOutboundQuantity());
                stockInfo.put("status", stock.getStatus());
                result.put("current_stock", stockInfo);
            }
        }

        // 3. 查询出库批次拆分记录
        List<OutboundBatchSplit> splits = outboundBatchSplitMapper.selectByInboundBatchId(materialBatchId);
        List<Map<String, Object>> splitInfoList = new ArrayList<>();
        for (OutboundBatchSplit split : splits) {
            Map<String, Object> splitInfo = new HashMap<>();
            splitInfo.put("outbound_detail_id", split.getOutboundDetailId());
            splitInfo.put("split_quantity", split.getSplitQuantity());
            splitInfo.put("remaining_quantity", split.getRemainingQuantity());
            splitInfo.put("created_at", split.getCreatedAt());
            splitInfoList.add(splitInfo);
        }
        result.put("outbound_splits", splitInfoList);

        // 4. 查询库存变动日志
        LambdaQueryWrapper<StockLog> logWrapper = new LambdaQueryWrapper<>();
        logWrapper.eq(StockLog::getMaterialBatchId, materialBatchId);
        logWrapper.orderByDesc(StockLog::getCreatedAt);
        List<StockLog> logs = stockLogMapper.selectList(logWrapper);
        List<Map<String, Object>> logInfoList = new ArrayList<>();
        for (StockLog log : logs) {
            Map<String, Object> logInfo = new HashMap<>();
            logInfo.put("operation_type", log.getOperationType());
            logInfo.put("change_quantity", log.getChangeQuantity());
            logInfo.put("before_quantity", log.getBeforeQuantity());
            logInfo.put("after_quantity", log.getAfterQuantity());
            logInfo.put("reference_order_id", log.getReferenceOrderId());
            logInfo.put("operator", log.getOperator());
            logInfo.put("created_at", log.getCreatedAt());
            logInfoList.add(logInfo);
        }
        result.put("stock_logs", logInfoList);

        // 5. 统计信息
        Map<String, Object> summary = new HashMap<>();
        if (inboundDetail != null) {
            summary.put("material_batch_id", materialBatchId);
            summary.put("material_id", inboundDetail.getMaterialId());
            summary.put("total_inbound", inboundDetail.getQuantity());
        }
        BigDecimal totalOutbound = BigDecimal.ZERO;
        for (OutboundBatchSplit split : splits) {
            totalOutbound = totalOutbound.add(split.getSplitQuantity());
        }
        summary.put("total_outbound", totalOutbound);
        summary.put("log_count", logs.size());
        result.put("summary", summary);

        return result;
    }
}
