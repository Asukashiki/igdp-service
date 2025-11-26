package com.inspur.agriculture.input.service.inventory.impl;

import com.inspur.agriculture.input.domain.inventory.Inventory;
import com.inspur.agriculture.input.domain.inventory.StockIn;
import com.inspur.agriculture.input.domain.inventory.StockInItem;
import com.inspur.agriculture.input.dto.inventory.StockInDTO;
import com.inspur.agriculture.input.dto.inventory.StockInQueryDTO;
import com.inspur.agriculture.input.mapper.inventory.InventoryMapper;
import com.inspur.agriculture.input.mapper.inventory.StockInItemMapper;
import com.inspur.agriculture.input.mapper.inventory.StockInMapper;
import com.inspur.agriculture.input.service.inventory.IStockInService;
import com.inspur.agriculture.input.vo.inventory.StockInVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DateUtils;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 入库单 Service实现
 *
 * @author inspur
 * @date 2025-11-26
 */
@Service
public class StockInServiceImpl implements IStockInService {

    @Autowired
    private StockInMapper stockInMapper;

    @Autowired
    private StockInItemMapper stockInItemMapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public List<StockInVO> getStockInList(StockInQueryDTO queryDTO) {
        List<StockInVO> list = stockInMapper.selectStockInList(queryDTO);
        // 查询明细
        for (StockInVO vo : list) {
            vo.setItems(stockInItemMapper.selectItemsByStockInId(vo.getStockInId()));
        }
        return list;
    }

    @Override
    public StockInVO getStockInById(String stockInId) {
        StockInVO vo = stockInMapper.selectStockInById(stockInId);
        if (vo != null) {
            vo.setItems(stockInItemMapper.selectItemsByStockInId(stockInId));
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createStockIn(StockInDTO dto) {
        // 生成入库单号和批次号
        String stockInId = stockInMapper.generateStockInId();
        String batchNo = stockInMapper.generateBatchNo();

        // 计算总数量
        int totalQuantity = dto.getItems().stream()
                .mapToInt(StockInDTO.StockInItemDTO::getQuantity)
                .sum();

        // DTO转Entity
        StockIn stockIn = new StockIn();
        BeanUtils.copyProperties(dto, stockIn);
        stockIn.setStockInId(stockInId);
        stockIn.setBatchNo(batchNo);
        stockIn.setStatus("0"); // 未入库
        stockIn.setTotalQuantity(totalQuantity);
        stockIn.setQrCode("/qrcodes/" + stockInId + ".png"); // 简化生成二维码路径

        // 设置审计字段
        stockIn.setCreateTime(DateUtils.getNowDate());
        try {
            stockIn.setCreatePeople(SecurityUtils.getUsername());
        } catch (Exception e) {
            stockIn.setCreatePeople("system");
        }
        stockIn.setDelFlag("0");

        // 插入入库单
        stockInMapper.insert(stockIn);

        // 插入入库明细
        List<StockInItem> items = new ArrayList<>();
        for (int i = 0; i < dto.getItems().size(); i++) {
            StockInDTO.StockInItemDTO itemDTO = dto.getItems().get(i);
            StockInItem item = new StockInItem();
            item.setStockInItemId(stockInItemMapper.generateStockInItemId(stockInId, i + 1));
            item.setStockInId(stockInId);
            item.setInputId(itemDTO.getInputId());
            item.setWarehouseId(dto.getWarehouseId());
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
        stockInItemMapper.batchInsert(items);

        return stockInId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int confirmStockIn(String stockInId) {
        // 查询入库单
        StockIn stockIn = stockInMapper.selectById(stockInId);
        if (stockIn == null || "2".equals(stockIn.getDelFlag())) {
            throw new ServiceException("入库单不存在");
        }
        if (!"0".equals(stockIn.getStatus())) {
            throw new ServiceException("入库单状态不正确");
        }

        // 查询入库明细
        List<StockInVO.StockInItemVO> items = stockInItemMapper.selectItemsByStockInId(stockInId);

        // 更新库存
        for (StockInVO.StockInItemVO item : items) {
            Inventory inventory = inventoryMapper.selectOrCreateInventory(
                    item.getInputId(), stockIn.getBatchNo(), item.getWarehouseId());

            if (inventory == null) {
                // 创建新库存记录
                inventory = new Inventory();
                inventory.setInventoryId(inventoryMapper.generateInventoryId());
                inventory.setInputId(item.getInputId());
                inventory.setBatchNo(stockIn.getBatchNo());
                inventory.setWarehouseId(item.getWarehouseId());
                inventory.setCurrentQuantity(item.getQuantity());
                inventory.setInDate(DateUtils.getNowDate());
                inventory.setExpiredDate(stockIn.getExpiredTime());
                inventory.setStockStatus("0");
                inventory.setCreateTime(DateUtils.getNowDate());
                try {
                    inventory.setCreatePeople(SecurityUtils.getUsername());
                } catch (Exception e) {
                    inventory.setCreatePeople("system");
                }
                inventory.setDelFlag("0");
                inventoryMapper.insert(inventory);
            } else {
                // 更新已有库存
                inventoryMapper.updateQuantity(inventory.getInventoryId(), item.getQuantity());
            }
        }

        // 更新入库单状态
        stockIn.setStatus("1");
        stockIn.setConfirmTime(DateUtils.getNowDate());
        stockIn.setUpdateTime(DateUtils.getNowDate());
        try {
            stockIn.setUpdatePeople(SecurityUtils.getUsername());
        } catch (Exception e) {
            stockIn.setUpdatePeople("system");
        }

        return stockInMapper.updateById(stockIn);
    }
}
