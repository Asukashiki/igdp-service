package com.inspur.system.mapper;

import cn.hutool.core.util.StrUtil;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.entity.SysMenu;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.system.domain.SysRoleMenu;
import com.inspur.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.Arrays;
import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author liyunlong
 */
public interface SysMenuMapper extends MPJBaseMapper<SysMenu> {

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu   查询条件
     * @param userId 用户id
     * @return 菜单列表
     */
    default List<SysMenu> selectMenuListByUserId(SysMenu menu, String userId) {
        MPJLambdaWrapper<SysMenu> joinQueryWrapper = new MPJLambdaWrapper<SysMenu>()
                .distinct()
                .selectAll(SysMenu.class)
                .leftJoin(SysRoleMenu.class, SysRoleMenu::getMenuId, SysMenu::getMenuId)
                .leftJoin(SysUserRole.class, SysUserRole::getRoleId, SysRoleMenu::getRoleId)
                .eq(SysUserRole::getUserId, userId)
                .eq(StrUtil.isNotEmpty(menu.getStatus()), SysMenu::getStatus, menu.getStatus())
                .eq(StrUtil.isNotEmpty(menu.getVisible()), SysMenu::getVisible, menu.getVisible())
                .like(StrUtil.isNotEmpty(menu.getMenuName()), SysMenu::getMenuName, menu.getMenuName())
                .orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);
        return selectJoinList(SysMenu.class, joinQueryWrapper);
    }

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    default List<SysMenu> selectMenuByRoleId(String roleId) {
        MPJLambdaWrapper<SysMenu> joinQueryWrapper = new MPJLambdaWrapper<SysMenu>()
                .selectAll(SysMenu.class)
                .leftJoin(SysRoleMenu.class, SysRoleMenu::getMenuId, SysMenu::getMenuId)
                .eq(SysRoleMenu::getRoleId, roleId)
                .eq(SysMenu::getStatus, Constants.STATUS_VALID)
                .orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);
        return selectJoinList(SysMenu.class, joinQueryWrapper);
    }



    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    default List<SysMenu> selectMenuTreeByUserId(String userId) {
        MPJLambdaWrapper<SysMenu> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.selectAll(SysMenu.class).distinct();
        if (StrUtil.isNotEmpty(userId)) {
            queryWrapper.leftJoin(SysRoleMenu.class, SysRoleMenu::getMenuId, SysMenu::getMenuId)
                    .leftJoin(SysUserRole.class, SysUserRole::getRoleId, SysRoleMenu::getRoleId)
                    .leftJoin(SysRole.class, SysRole::getRoleId, SysUserRole::getRoleId)
                    .eq(SysUserRole::getUserId, userId)
                    .eq(SysRole::getStatus, Constants.STATUS_VALID);
        }
        queryWrapper.in(SysMenu::getMenuType, Arrays.asList("M", "C"))
                .eq(SysMenu::getStatus, Constants.STATUS_VALID)
                .orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);
        return selectJoinList(SysMenu.class, queryWrapper);
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
     List<String> selectMenuListByRoleId(@Param("roleId") String roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

}
