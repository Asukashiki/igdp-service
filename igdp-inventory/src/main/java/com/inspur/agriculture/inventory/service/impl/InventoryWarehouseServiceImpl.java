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
            throw new ServiceException("仓库类型不能为空");
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
                throw new ServiceException("未知的仓库类型: " + warehouseType);
        }
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
        if (isBlank(warehouse.getType())) {
            throw new ServiceException("仓库类型不能为空");
        }
        if (isBlank(warehouse.getStoreType())) {
            throw new ServiceException("存储类型不能为空");
        }
        if (isBlank(warehouse.getOrgName())) {
            throw new ServiceException("所属机构不能为空");
        }
        if (isBlank(warehouse.getAdminLevel())) {
            throw new ServiceException("行政层级不能为空");
        }
        if (warehouse.getCapacity() == null || warehouse.getCapacity().doubleValue() < 0D) {
            throw new ServiceException("仓库容量不能小于0");
        }

        // 如果是联盟或合作社仓库，上级仓库必选
        if (("LM".equals(warehouse.getType()) || "HZS".equals(warehouse.getType()))
                && (warehouse.getParentId() == null || warehouse.getParentId() <= 0)) {
            throw new ServiceException("联盟/合作社仓库必须选择上级仓库");
        }

        // 检查编码唯一性
        int count = baseMapper.checkWarehouseCodeExists(warehouse.getWarehouseCode(),
                isUpdate ? warehouse.getId() : null);
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
