package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryWarehouseOwner;
import com.inspur.agriculture.inventory.domain.InventoryWarehousePermission;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseOwnerService;
import com.inspur.agriculture.inventory.service.IInventoryWarehousePermissionService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/inventory/warehouse-permission")
public class WarehousePermissionController extends BaseController {

    @Autowired
    private IInventoryWarehouseOwnerService ownerService;

    @Autowired
    private IInventoryWarehousePermissionService permissionService;

    @GetMapping("/owner/list")
    public TableDataInfo listOwners(InventoryWarehouseOwner owner) {
        startPage();
        LambdaQueryWrapper<InventoryWarehouseOwner> wrapper = new LambdaQueryWrapper<>();
        if (owner.getWarehouseId() != null) {
            wrapper.eq(InventoryWarehouseOwner::getWarehouseId, owner.getWarehouseId());
        }
        if (owner.getOwnerUserId() != null) {
            wrapper.eq(InventoryWarehouseOwner::getOwnerUserId, owner.getOwnerUserId());
        }
        if (owner.getStatus() != null && !owner.getStatus().trim().isEmpty()) {
            wrapper.eq(InventoryWarehouseOwner::getStatus, owner.getStatus());
        }
        if (owner.getOwnerRole() != null && !owner.getOwnerRole().trim().isEmpty()) {
            wrapper.eq(InventoryWarehouseOwner::getOwnerRole, owner.getOwnerRole());
        }
        wrapper.orderByDesc(InventoryWarehouseOwner::getCreateTime, InventoryWarehouseOwner::getId);
        List<InventoryWarehouseOwner> list = ownerService.list(wrapper);
        return getDataTable(list);
    }

    @PostMapping("/owner")
    public AjaxResult addOwner(@RequestBody InventoryWarehouseOwner owner) {
        validateOwner(owner);
        fillOwnerAudit(owner, true);
        boolean result = ownerService.save(owner);
        return toAjax(result);
    }

    @PutMapping("/owner")
    public AjaxResult editOwner(@RequestBody InventoryWarehouseOwner owner) {
        if (owner.getId() == null) {
            throw new ServiceException("Ownership record ID cannot be empty.");
        }
        validateOwner(owner);
        fillOwnerAudit(owner, false);
        boolean result = ownerService.updateById(owner);
        return toAjax(result);
    }

    @DeleteMapping("/owner/{ids}")
    public AjaxResult removeOwner(@PathVariable List<Long> ids) {
        boolean result = ownerService.removeByIds(ids);
        return toAjax(result);
    }

    @GetMapping("/permission/list")
    public TableDataInfo listPermissions(InventoryWarehousePermission permission) {
        startPage();
        LambdaQueryWrapper<InventoryWarehousePermission> wrapper = new LambdaQueryWrapper<>();
        if (permission.getWarehouseId() != null) {
            wrapper.eq(InventoryWarehousePermission::getWarehouseId, permission.getWarehouseId());
        }
        if (permission.getDeptId() != null) {
            wrapper.eq(InventoryWarehousePermission::getDeptId, permission.getDeptId());
        }
        if (permission.getStatus() != null && !permission.getStatus().trim().isEmpty()) {
            wrapper.eq(InventoryWarehousePermission::getStatus, permission.getStatus());
        }
        wrapper.orderByDesc(InventoryWarehousePermission::getCreateTime, InventoryWarehousePermission::getId);
        List<InventoryWarehousePermission> list = permissionService.list(wrapper);
        return getDataTable(list);
    }

    @PostMapping("/permission")
    public AjaxResult addPermission(@RequestBody InventoryWarehousePermission permission) {
        validatePermission(permission);
        fillPermissionAudit(permission, true);
        boolean result = permissionService.save(permission);
        return toAjax(result);
    }

    @PutMapping("/permission")
    public AjaxResult editPermission(@RequestBody InventoryWarehousePermission permission) {
        if (permission.getId() == null) {
            throw new ServiceException("Usage record ID cannot be empty.");
        }
        validatePermission(permission);
        fillPermissionAudit(permission, false);
        boolean result = permissionService.updateById(permission);
        return toAjax(result);
    }

    @DeleteMapping("/permission/{ids}")
    public AjaxResult removePermission(@PathVariable List<Long> ids) {
        boolean result = permissionService.removeByIds(ids);
        return toAjax(result);
    }

    private void validateOwner(InventoryWarehouseOwner owner) {
        if (owner == null) {
            throw new ServiceException("Warehouse ownership cannot be empty.");
        }
        if (owner.getWarehouseId() == null) {
            throw new ServiceException("Warehouse ID cannot be empty.");
        }
        if (owner.getOwnerUserId() == null) {
            throw new ServiceException("Owner user ID cannot be empty.");
        }
        if (owner.getOwnerRole() == null || owner.getOwnerRole().trim().isEmpty()) {
            owner.setOwnerRole("PRIMARY");
        }
        if (owner.getStatus() == null || owner.getStatus().trim().isEmpty()) {
            owner.setStatus("1");
        }
        if (owner.getIsPrimary() == null || owner.getIsPrimary().trim().isEmpty()) {
            owner.setIsPrimary("0");
        }
    }

    private void validatePermission(InventoryWarehousePermission permission) {
        if (permission == null) {
            throw new ServiceException("Warehouse permission cannot be empty.");
        }
        if (permission.getWarehouseId() == null) {
            throw new ServiceException("Warehouse ID cannot be empty.");
        }
        if (permission.getDeptId() == null) {
            throw new ServiceException("Department ID cannot be empty.");
        }
    }

    private void fillOwnerAudit(InventoryWarehouseOwner owner, boolean isCreate) {
        String username = getCurrentUsername();
        if (isCreate) {
            owner.setCreateBy(username);
            owner.setCreateTime(LocalDateTime.now());
        }
        owner.setUpdateBy(username);
        owner.setUpdateTime(LocalDateTime.now());
    }

    private void fillPermissionAudit(InventoryWarehousePermission permission, boolean isCreate) {
        String username = getCurrentUsername();
        if (isCreate) {
            permission.setCreateBy(username);
            permission.setCreateTime(LocalDateTime.now());
            if (permission.getAssignTime() == null) {
                permission.setAssignTime(LocalDateTime.now());
            }
            if (permission.getAssignerName() == null || permission.getAssignerName().trim().isEmpty()) {
                permission.setAssignerName(username);
            }
        }
        permission.setUpdateBy(username);
        permission.setUpdateTime(LocalDateTime.now());
    }

    private String getCurrentUsername() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception ex) {
            return "system";
        }
    }
}
