package com.inspur.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.inspur.system.domain.SysRoleMenu;

import java.util.Arrays;
import java.util.List;

/**
 * 角色与菜单关联表 数据层
 *
 * @author liyunlong
 */
public interface SysRoleMenuMapper extends MPJBaseMapper<SysRoleMenu> {
    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    default long countByMenuId(String menuId) {
        return selectCount(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getMenuId, menuId));
    }

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    default int deleteRoleMenuByRoleId(String roleId) {
        return delete(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, roleId));
    }

    /**
     * 批量删除角色菜单关联信息
     *
     * @param roleIds 需要删除的roleId集合
     * @return 结果
     */
    default int deleteByRoleIds(String[] roleIds) {
        return delete(new LambdaQueryWrapper<SysRoleMenu>()
                .in(SysRoleMenu::getRoleId, Arrays.asList(roleIds)));
    }

    /**
     * 批量新增角色菜单信息
     *
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    int batchRoleMenu(List<SysRoleMenu> roleMenuList);
}
