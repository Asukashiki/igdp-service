package com.inspur.system.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.system.domain.SysRoleWorkbenchItem;
import com.inspur.system.domain.SysUserRole;
import com.inspur.system.domain.SysWorkbenchItem;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysWorkbenchItemMapper
 * @date 2024/6/10 16:52
 */
public interface SysWorkbenchItemMapper extends MPJBaseMapper<SysWorkbenchItem> {
    /**
     * 根据userId加载工作台内容
     *
     * @param userId 用户id
     * @return 集合
     */
    default List<SysWorkbenchItem> selectByUserId(String userId) {
        MPJLambdaWrapper<SysWorkbenchItem> wrapper = new MPJLambdaWrapper<>();
        wrapper.distinct().select(SysWorkbenchItem::getId, SysWorkbenchItem::getCode, SysWorkbenchItem::getName);
        wrapper.leftJoin(SysRoleWorkbenchItem.class, SysRoleWorkbenchItem::getItemId, SysWorkbenchItem::getId);
        wrapper.leftJoin(SysUserRole.class, SysUserRole::getRoleId, SysRoleWorkbenchItem::getRoleId);
        wrapper.leftJoin(SysRole.class, SysRole::getRoleId, SysUserRole::getRoleId);
        wrapper.eq(SysUserRole::getUserId, userId);
        wrapper.eq(SysRole::getStatus, Constants.STATUS_VALID);
        wrapper.orderByAsc(SysWorkbenchItem::getId);
        wrapper.groupBy(SysWorkbenchItem::getId, SysWorkbenchItem::getCode, SysWorkbenchItem::getName);
        return selectJoinList(SysWorkbenchItem.class, wrapper);
    }
}
