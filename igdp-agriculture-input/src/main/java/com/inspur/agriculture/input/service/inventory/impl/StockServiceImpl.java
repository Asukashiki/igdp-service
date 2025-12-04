package com.inspur.agriculture.input.service.inventory.impl;

import com.inspur.agriculture.input.domain.inventory.Stock;
import com.inspur.agriculture.input.domain.inventory.StockLog;
import com.inspur.agriculture.input.mapper.inventory.StockLogMapper;
import com.inspur.agriculture.input.mapper.inventory.StockMapper;
import com.inspur.agriculture.input.service.inventory.IStockService;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 库存服务实现类
 *
 * @author igdp
 */
@Service
public class StockServiceImpl implements IStockService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockLogMapper stockLogMapper;

    @Override
    public List<Map<String, Object>> selectStockList(Map<String, Object> params) {
        return stockMapper.selectStockList(params);
    }

    @Override
    public Map<String, Object> selectStockById(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("库存ID不能为空");
        }
        Map<String, Object> result = stockMapper.selectStockById(id);
        if (result == null) {
            throw new ServiceException("库存不存在");
        }
        return result;
    }

    @Override
    public List<Stock> selectByWarehouseAndMaterial(String warehouseId, String materialId) {
        if (StringUtils.isEmpty(warehouseId)) {
            throw new ServiceException("仓库ID不能为空");
        }
        if (StringUtils.isEmpty(materialId)) {
            throw new ServiceException("物料ID不能为空");
        }
        return stockMapper.selectByWarehouseAndMaterial(warehouseId, materialId);
    }

    @Override
    public Stock selectByBatch(String warehouseId, String materialId, String materialBatchId) {
        if (StringUtils.isEmpty(warehouseId)) {
            throw new ServiceException("仓库ID不能为空");
        }
        if (StringUtils.isEmpty(materialId)) {
            throw new ServiceException("物料ID不能为空");
        }
        if (StringUtils.isEmpty(materialBatchId)) {
            throw new ServiceException("批次ID不能为空");
        }
        return stockMapper.selectByBatch(warehouseId, materialId, materialBatchId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStockInbound(String warehouseId, String materialId, String materialBatchId,
                                      BigDecimal quantity, Date expiryDate, String qrCode, String operator) {
        if (StringUtils.isEmpty(warehouseId)) {
            throw new ServiceException("仓库ID不能为空");
        }
        if (StringUtils.isEmpty(materialId)) {
            throw new ServiceException("物料ID不能为空");
        }
        if (StringUtils.isEmpty(materialBatchId)) {
            throw new ServiceException("批次ID不能为空");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("数量必须大于0");
        }

        // 查询是否已存在该批次的库存
        Stock existStock = stockMapper.selectByBatch(warehouseId, materialId, materialBatchId);

        BigDecimal beforeQuantity = BigDecimal.ZERO;
        BigDecimal afterQuantity = quantity;

        if (existStock != null) {
            // 更新现有库存
            beforeQuantity = existStock.getQuantity();
            afterQuantity = beforeQuantity.add(quantity);

            existStock.setQuantity(afterQuantity);
            existStock.setInboundQuantity(existStock.getInboundQuantity().add(quantity));
            existStock.setUpdatedAt(new Date());
            stockMapper.updateById(existStock);
        } else {
            // 创建新库存
            Stock newStock = new Stock();
            newStock.setId(UUID.randomUUID().toString().replace("-", ""));
            newStock.setWarehouseId(warehouseId);
            newStock.setMaterialId(materialId);
            newStock.setMaterialBatchId(materialBatchId);
            newStock.setQuantity(quantity);
            newStock.setInboundQuantity(quantity);
            newStock.setOutboundQuantity(BigDecimal.ZERO);
            newStock.setExpiryDate(expiryDate);
            newStock.setQrCode(qrCode);
            newStock.setStatus("active");
            newStock.setCreatedAt(new Date());
            newStock.setUpdatedAt(new Date());
            stockMapper.insert(newStock);
        }

        // 记录库存变动日志
        StockLog stockLog = new StockLog();
        stockLog.setId(UUID.randomUUID().toString().replace("-", ""));
        stockLog.setWarehouseId(warehouseId);
        stockLog.setMaterialId(materialId);
        stockLog.setMaterialBatchId(materialBatchId);
        stockLog.setOperationType("inbound");
        stockLog.setChangeQuantity(quantity);
        stockLog.setBeforeQuantity(beforeQuantity);
        stockLog.setAfterQuantity(afterQuantity);
        stockLog.setOperator(operator);
        stockLog.setCreatedAt(new Date());
        stockLogMapper.insert(stockLog);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStockOutbound(String warehouseId, String materialId, String materialBatchId,
                                       BigDecimal quantity, String operator) {
        if (StringUtils.isEmpty(warehouseId)) {
            throw new ServiceException("仓库ID不能为空");
        }
        if (StringUtils.isEmpty(materialId)) {
            throw new ServiceException("物料ID不能为空");
        }
        if (StringUtils.isEmpty(materialBatchId)) {
            throw new ServiceException("批次ID不能为空");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("数量必须大于0");
        }

        // 查询库存
        Stock stock = stockMapper.selectByBatch(warehouseId, materialId, materialBatchId);
        if (stock == null) {
            throw new ServiceException("库存不存在");
        }

        // 检查库存是否充足
        if (stock.getQuantity().compareTo(quantity) < 0) {
            throw new ServiceException("库存不足，当前库存：" + stock.getQuantity() + "，需要：" + quantity);
        }

        // 检查是否过期
        if (stock.getExpiryDate() != null && stock.getExpiryDate().before(new Date())) {
            throw new ServiceException("批次已过期，不能出库");
        }

        BigDecimal beforeQuantity = stock.getQuantity();
        BigDecimal afterQuantity = beforeQuantity.subtract(quantity);

        // 更新库存
        stock.setQuantity(afterQuantity);
        stock.setOutboundQuantity(stock.getOutboundQuantity().add(quantity));
        stock.setUpdatedAt(new Date());
        stockMapper.updateById(stock);

        // 记录库存变动日志
        StockLog stockLog = new StockLog();
        stockLog.setId(UUID.randomUUID().toString().replace("-", ""));
        stockLog.setWarehouseId(warehouseId);
        stockLog.setMaterialId(materialId);
        stockLog.setMaterialBatchId(materialBatchId);
        stockLog.setOperationType("outbound");
        stockLog.setChangeQuantity(quantity.negate()); // 出库为负数
        stockLog.setBeforeQuantity(beforeQuantity);
        stockLog.setAfterQuantity(afterQuantity);
        stockLog.setOperator(operator);
        stockLog.setCreatedAt(new Date());
        stockLogMapper.insert(stockLog);

        return true;
    }

    @Override
    public Map<String, Object> sumWarehouseStock(String warehouseId) {
        if (StringUtils.isEmpty(warehouseId)) {
            throw new ServiceException("仓库ID不能为空");
        }
        return stockMapper.sumWarehouseStock(warehouseId);
    }

    @Override
    public Map<String, Object> sumMaterialStock(String materialId) {
        if (StringUtils.isEmpty(materialId)) {
            throw new ServiceException("物料ID不能为空");
        }
        return stockMapper.sumMaterialStock(materialId);
    }

    @Override
    public List<Map<String, Object>> selectLowStock(String warehouseId, BigDecimal minQuantity) {
        if (minQuantity == null) {
            minQuantity = BigDecimal.ZERO;
        }
        return stockMapper.selectLowStock(warehouseId, minQuantity);
    }

    @Override
    public List<Map<String, Object>> selectExpiringSoon(Integer days, String warehouseId) {
        if (days == null || days <= 0) {
            days = 30; // 默认30天
        }
        return stockMapper.selectExpiringSoon(days, warehouseId);
    }

    @Override
    public List<Map<String, Object>> selectExpired(String warehouseId) {
        return stockMapper.selectExpired(warehouseId);
    }

    @Override
    public List<Stock> selectAvailableStockFIFO(String warehouseId, String materialId, BigDecimal quantity) {
        if (StringUtils.isEmpty(warehouseId)) {
            throw new ServiceException("仓库ID不能为空");
        }
        if (StringUtils.isEmpty(materialId)) {
            throw new ServiceException("物料ID不能为空");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("数量必须大于0");
        }
        return stockMapper.selectAvailableStockFIFO(warehouseId, materialId, quantity);
    }

    @Override
    public List<Map<String, Object>> selectStockLogList(Map<String, Object> params) {
        return stockLogMapper.selectStockLogList(params);
    }

    @Override
    public boolean checkStockSufficient(String warehouseId, String materialId, BigDecimal quantity) {
        if (StringUtils.isEmpty(warehouseId)) {
            throw new ServiceException("仓库ID不能为空");
        }
        if (StringUtils.isEmpty(materialId)) {
            throw new ServiceException("物料ID不能为空");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("数量必须大于0");
        }

        // 查询该物料在该仓库的所有可用库存
        List<Stock> stocks = stockMapper.selectByWarehouseAndMaterial(warehouseId, materialId);
        if (stocks == null || stocks.isEmpty()) {
            return false;
        }

        // 计算总库存
        BigDecimal totalStock = BigDecimal.ZERO;
        Date now = new Date();
        for (Stock stock : stocks) {
            // 排除已过期的库存
            if (stock.getExpiryDate() != null && stock.getExpiryDate().before(now)) {
                continue;
            }
            totalStock = totalStock.add(stock.getQuantity());
        }

        return totalStock.compareTo(quantity) >= 0;
    }
}
