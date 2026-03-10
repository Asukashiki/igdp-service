package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.mapper.InventoryWarehouseMapper;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 仓库服务实现
 */
@Service
public class InventoryWarehouseServiceImpl extends ServiceImpl<InventoryWarehouseMapper, InventoryWarehouse> implements IInventoryWarehouseService {

    @Override
    public List<InventoryWarehouse> selectWarehouseList(InventoryWarehouse warehouse) {
        return baseMapper.selectWarehouseList(warehouse);
    }

    @Override
    public InventoryWarehouse selectWarehouseById(Long id) {
        return baseMapper.selectWarehouseById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createWarehouse(InventoryWarehouse warehouse) {
        validateWarehouse(warehouse, false);
        warehouse.setCreateTime(LocalDateTime.now());
        warehouse.setUpdateTime(LocalDateTime.now());
        warehouse.setCreateBy(getCurrentUsername());
        warehouse.setUpdateBy(getCurrentUsername());
        return this.save(warehouse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWarehouse(InventoryWarehouse warehouse) {
        if (warehouse == null || warehouse.getId() == null) {
            throw new ServiceException("仓库ID不能为空");
        }
        InventoryWarehouse exists = this.getById(warehouse.getId());
        if (exists == null) {
            throw new ServiceException("仓库不存在");
        }
        validateWarehouse(warehouse, true);
        warehouse.setUpdateTime(LocalDateTime.now());
        warehouse.setUpdateBy(getCurrentUsername());
        return this.updateById(warehouse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWarehouse(Long id) {
        if (id == null) {
            throw new ServiceException("仓库ID不能为空");
        }
        InventoryWarehouse exists = this.getById(id);
        if (exists == null) {
            throw new ServiceException("仓库不存在");
        }
        return this.removeById(id);
    }

    private void validateWarehouse(InventoryWarehouse warehouse, boolean isUpdate) {
        if (warehouse == null) {
            throw new ServiceException("仓库信息不能为空");
        }
        if (isBlank(warehouse.getWarehouseCode())) {
            throw new ServiceException("仓库编码不能为空");
        }
        if (isBlank(warehouse.getWarehouseName())) {
            throw new ServiceException("仓库名称不能为空");
        }
        if (warehouse.getCapacity() == null || warehouse.getCapacity().doubleValue() < 0D) {
            throw new ServiceException("仓库容量不能小于0");
        }

        LambdaQueryWrapper<InventoryWarehouse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryWarehouse::getWarehouseCode, warehouse.getWarehouseCode());
        if (isUpdate && warehouse.getId() != null) {
            wrapper.ne(InventoryWarehouse::getId, warehouse.getId());
        }
        long count = this.count(wrapper);
        if (count > 0) {
            throw new ServiceException("仓库编码已存在");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String getCurrentUsername() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception ex) {
            return "system";
        }
    }
}
