package com.inspur.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.system.domain.SysRoleWorkbenchItem;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ISysRoleWorkbenchItemService
 * @date 2024/6/11 9:55
 */
public interface ISysRoleWorkbenchItemService extends IService<SysRoleWorkbenchItem> {
    /**
     * 保存角色分配item信息
     *
     * @param role 角色信息
     * @return 结果
     */
    boolean saveSysRoleWorkbenchItem(SysRole role);
    /**
     * 根据角色id获取关联的itemId列表
     * @param roleId 角色id
     * @return itemId列表
     * */
    List<Long> getItemIdsByRoleId(String roleId);
}
