package com.inspur.agriculture.input.service.inventory.impl;

import com.inspur.agriculture.input.domain.inventory.Inventory;
import com.inspur.agriculture.input.domain.inventory.StockOut;
import com.inspur.agriculture.input.domain.inventory.StockOutItem;
import com.inspur.agriculture.input.domain.inventory.Warehouse;
import com.inspur.agriculture.input.dto.inventory.StockOutDTO;
import com.inspur.agriculture.input.dto.inventory.StockOutQueryDTO;
import com.inspur.agriculture.input.mapper.inventory.InventoryMapper;
import com.inspur.agriculture.input.mapper.inventory.StockOutItemMapper;
import com.inspur.agriculture.input.mapper.inventory.StockOutMapper;
import com.inspur.agriculture.input.mapper.inventory.WarehouseMapper;
import com.inspur.agriculture.input.service.inventory.IStockOutService;
import com.inspur.agriculture.input.vo.inventory.StockOutVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DateUtils;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 出库单 Service实现
 *
 * @author inspur
 * @date 2025-11-26
 */
@Service
public class StockOutServiceImpl implements IStockOutService {

    @Autowired
    private StockOutMapper stockOutMapper;

    @Autowired
    private StockOutItemMapper stockOutItemMapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public List<StockOutVO> getStockOutList(StockOutQueryDTO queryDTO) {
        List<StockOutVO> list = stockOutMapper.selectStockOutList(queryDTO);
        for (StockOutVO vo : list) {
            vo.setItems(stockOutItemMapper.selectItemsByStockOutId(vo.getStockOutId()));
        }
        return list;
    }

    @Override
    public StockOutVO getStockOutById(String stockOutId) {
        StockOutVO vo = stockOutMapper.selectStockOutById(stockOutId);
        if (vo != null) {
            vo.setItems(stockOutItemMapper.selectItemsByStockOutId(stockOutId));
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createStockOut(StockOutDTO dto) {
        // 校验仓库是否存在
        Warehouse warehouse = warehouseMapper.selectById(dto.getWarehouseId());
        if (warehouse == null || "2".equals(warehouse.getDelFlag())) {
            throw new ServiceException("仓库不存在");
        }
        if (!"1".equals(warehouse.getStatus())) {
            throw new ServiceException("仓库已停用");
        }

        // 校验每个出库商品的库存是否充足
        for (StockOutDTO.StockOutItemDTO itemDTO : dto.getItems()) {
            Inventory inventory = inventoryMapper.selectOrCreateInventory(
                    itemDTO.getInputId(), itemDTO.getBatchNo(), dto.getWarehouseId());

            if (inventory == null) {
                throw new ServiceException(String.format("投入品ID %d 批次号 %s 在该仓库中无库存",
                        itemDTO.getInputId(), itemDTO.getBatchNo()));
            }

            if (inventory.getCurrentQuantity() < itemDTO.getQuantity()) {
                throw new ServiceException(String.format("投入品ID %d 批次号 %s 库存不足，当前库存: %d，需要出库: %d",
                        itemDTO.getInputId(), itemDTO.getBatchNo(),
                        inventory.getCurrentQuantity(), itemDTO.getQuantity()));
            }
        }

        // 生成出库单号
        String stockOutId = stockOutMapper.generateStockOutId();

        // 取第一个明细的批次号作为出库单批次号
        String batchNo = dto.getItems().get(0).getBatchNo();

        // 计算总数量
        int totalQuantity = dto.getItems().stream()
                .mapToInt(StockOutDTO.StockOutItemDTO::getQuantity)
                .sum();

        // DTO转Entity
        StockOut stockOut = new StockOut();
        BeanUtils.copyProperties(dto, stockOut);
        stockOut.setStockOutId(stockOutId);
        stockOut.setBatchNo(batchNo);
        stockOut.setStatus("0"); // 未出库
        stockOut.setTotalQuantity(totalQuantity);

        // 设置审计字段
        stockOut.setCreateTime(DateUtils.getNowDate());
        try {
            stockOut.setCreatePeople(SecurityUtils.getUsername());
        } catch (Exception e) {
            stockOut.setCreatePeople("system");
        }
        stockOut.setDelFlag("0");

        // 插入出库单
        stockOutMapper.insert(stockOut);

        // 插入出库明细
        List<StockOutItem> items = new ArrayList<>();
        for (int i = 0; i < dto.getItems().size(); i++) {
            StockOutDTO.StockOutItemDTO itemDTO = dto.getItems().get(i);
            StockOutItem item = new StockOutItem();
            item.setStockOutItemId(stockOutItemMapper.generateStockOutItemId(stockOutId, i + 1));
            item.setStockOutId(stockOutId);
            item.setInputId(itemDTO.getInputId());
            item.setWarehouseId(dto.getWarehouseId());
            item.setBatchNo(itemDTO.getBatchNo());
            item.setQuantity(itemDTO.getQuantity());
            item.setRemarks(itemDTO.getRemarks());
            item.setCreateTime(DateUtils.getNowDate());
            try {
                item.setCreatePeople(SecurityUtils.getUsername());
            } catch (Exception e) {
                item.setCreatePeople("system");
            }
            item.setDelFlag("0");
            items.add(item);
        }
        stockOutItemMapper.batchInsert(items);

        return stockOutId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int confirmStockOut(String stockOutId) {
        // 查询出库单
        StockOut stockOut = stockOutMapper.selectById(stockOutId);
        if (stockOut == null || "2".equals(stockOut.getDelFlag())) {
            throw new ServiceException("出库单不存在");
        }
        if (!"0".equals(stockOut.getStatus())) {
            throw new ServiceException("出库单状态不正确");
        }

        // 查询出库明细
        List<StockOutVO.StockOutItemVO> items = stockOutItemMapper.selectItemsByStockOutId(stockOutId);

        // 计算总出库数量
        int totalQuantity = items.stream().mapToInt(StockOutVO.StockOutItemVO::getQuantity).sum();

        // 扣减库存
        for (StockOutVO.StockOutItemVO item : items) {
            Inventory inventory = inventoryMapper.selectOrCreateInventory(
                    item.getInputId(), item.getBatchNo(), item.getWarehouseId());

            if (inventory == null) {
                throw new ServiceException(String.format("批次 %s 库存不存在", item.getBatchNo()));
            }

            if (inventory.getCurrentQuantity() < item.getQuantity()) {
                throw new ServiceException(String.format("批次 %s 库存不足，当前库存: %d，需要出库: %d",
                        item.getBatchNo(), inventory.getCurrentQuantity(), item.getQuantity()));
            }

            // 扣减库存
            inventoryMapper.updateQuantity(inventory.getInventoryId(), -item.getQuantity());
        }

        // 更新仓库已用容量（减少）
        BigDecimal capacityChange = BigDecimal.valueOf(-totalQuantity);
        warehouseMapper.updateUsedCapacity(stockOut.getWarehouseId(), capacityChange);

        // 更新出库单状态
        stockOut.setStatus("1");
        stockOut.setOutTime(DateUtils.getNowDate());
        stockOut.setUpdateTime(DateUtils.getNowDate());
        try {
            stockOut.setUpdatePeople(SecurityUtils.getUsername());
        } catch (Exception e) {
            stockOut.setUpdatePeople("system");
        }

        return stockOutMapper.updateById(stockOut);
    }
}
