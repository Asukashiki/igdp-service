package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.domain.InventoryWarehouseOwner;
import com.inspur.agriculture.inventory.domain.InventoryWarehousePermission;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseOwnerService;
import com.inspur.agriculture.inventory.service.IInventoryWarehousePermissionService;
import com.inspur.common.utils.LoginHelper;
import com.inspur.agriculture.inventory.mapper.InventoryWarehouseMapper;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 仓库服务实现
 */
@Service
public class InventoryWarehouseServiceImpl extends ServiceImpl<InventoryWarehouseMapper, InventoryWarehouse> implements IInventoryWarehouseService {

    @Autowired
    private IInventoryWarehouseOwnerService ownerService;

    @Autowired
    private IInventoryWarehousePermissionService permissionService;

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
        boolean saved = this.save(warehouse);
        if (saved) {
            createDefaultPermissions(warehouse);
        }
        return saved;
    }

    private void createDefaultPermissions(InventoryWarehouse warehouse) {
        if (warehouse == null || warehouse.getId() == null) {
            return;
        }
        String userId = SecurityUtils.getUserId();
        String deptId = SecurityUtils.getDeptId();
        String deptName = SecurityUtils.getDeptName();
        String ownerName = LoginHelper.getNickname();
        if (ownerName == null || ownerName.trim().isEmpty()) {
            ownerName = SecurityUtils.getUsername();
        }

        if (userId != null && !userId.trim().isEmpty()) {
            InventoryWarehouseOwner owner = new InventoryWarehouseOwner();
            owner.setWarehouseId(warehouse.getId());
            owner.setOwnerUserId(userId);
            owner.setOwnerUserName(ownerName);
            if (deptId != null && !deptId.trim().isEmpty()) {
                owner.setOwnerOrgId(deptId);
                owner.setOwnerOrgName(deptName);
            }
            owner.setOwnerRole("PRIMARY");
            owner.setIsPrimary("1");
            ownerService.save(owner);
        }

        if (deptId != null && !deptId.trim().isEmpty()) {
            InventoryWarehousePermission permission = new InventoryWarehousePermission();
            permission.setWarehouseId(warehouse.getId());
            permission.setDeptId(deptId);
            permission.setDeptName(deptName);
            permissionService.save(permission);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWarehouse(InventoryWarehouse warehouse) {
        if (warehouse == null || warehouse.getId() == null) {
            throw new ServiceException("Warehouse ID cannot be empty.");
        }
        InventoryWarehouse exists = this.getById(warehouse.getId());
        if (exists == null) {
            throw new ServiceException("Warehouse not found.");
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
            throw new ServiceException("Warehouse ID cannot be empty.");
        }
        InventoryWarehouse exists = this.getById(id);
        if (exists == null) {
            throw new ServiceException("Warehouse not found.");
        }
        return this.removeById(id);
    }

    @Override
    public InventoryWarehouse selectWarehouseByCode(String warehouseCode) {
        if (isBlank(warehouseCode)) {
            return null;
        }
        LambdaQueryWrapper<InventoryWarehouse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryWarehouse::getWarehouseCode, warehouseCode);
        return this.getOne(wrapper);
    }


    @Override
    public String generateWarehouseCode(String warehouseType) {
        if (isBlank(warehouseType)) {
            throw new ServiceException("Warehouse type cannot be empty.");
        }

        // 1. 获取仓库类型前缀
        String prefix = getWarehouseTypePrefix(warehouseType);

        // 2. 获取当前日期
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 3. 查询该前缀+日期的最大序列号
        String pattern = prefix + "_" + dateStr + "_%";
        Integer maxSeq = baseMapper.selectMaxSequence(pattern);
        if (maxSeq == null) {
            maxSeq = 0;
        }

        // 4. 生成新序列号
        int newSeq = maxSeq + 1;
        String seqStr = String.format("%03d", newSeq);

        // 5. 组合生成编码
        return prefix + "_" + dateStr + "_" + seqStr;
    }

    private String getWarehouseTypePrefix(String warehouseType) {
        switch (warehouseType) {
            case "ZY":
                return "ZY";  // 中央仓库
            case "LM":
                return "LM";  // 联盟仓库
            case "HZS":
                return "HZS"; // 合作社仓库
            case "QY":
                return "QY";  // 企业仓库
            default:
                throw new ServiceException("Unknown warehouse type: " + warehouseType);
        }
    }

    private void validateWarehouse(InventoryWarehouse warehouse, boolean isUpdate) {
        if (warehouse == null) {
            throw new ServiceException("Warehouse information cannot be empty.");
        }
        if (isBlank(warehouse.getWarehouseCode())) {
            throw new ServiceException("Warehouse code cannot be empty.");
        }
        if (isBlank(warehouse.getWarehouseName())) {
            throw new ServiceException("Warehouse name cannot be empty.");
        }
        if (isBlank(warehouse.getType())) {
            throw new ServiceException("Warehouse type cannot be empty.");
        }
        if (isBlank(warehouse.getStoreType())) {
            throw new ServiceException("Storage type cannot be empty.");
        }
        if (isBlank(warehouse.getOrgName())) {
            throw new ServiceException("Owning organization cannot be empty.");
        }
        if (isBlank(warehouse.getAdminLevel())) {
            throw new ServiceException("Administrative level cannot be empty.");
        }
        if (warehouse.getCapacity() == null || warehouse.getCapacity().doubleValue() < 0D) {
            throw new ServiceException("Warehouse capacity cannot be less than 0.");
        }

        // 如果是联盟或合作社仓库，上级仓库必选
        if (("LM".equals(warehouse.getType()) || "HZS".equals(warehouse.getType()))
                && (warehouse.getParentId() == null || warehouse.getParentId() <= 0)) {
            throw new ServiceException("Union/Cooperative warehouses must select a parent warehouse.");
        }

        // 检查编码唯一性
        int count = baseMapper.checkWarehouseCodeExists(warehouse.getWarehouseCode(),
                isUpdate ? warehouse.getId() : null);
        if (count > 0) {
            throw new ServiceException("Warehouse code already exists.");
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
