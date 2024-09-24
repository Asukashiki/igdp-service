package com.inspur.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.utils.LoginHelper;
import com.inspur.system.domain.SysRoleWorkbenchItem;
import com.inspur.system.domain.SysWorkbenchItem;
import com.inspur.system.mapper.SysRoleWorkbenchItemMapper;
import com.inspur.system.service.ISysRoleWorkbenchItemService;
import com.inspur.system.service.ISysWorkbenchItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysRoleWorkbenchItemServiceImpl
 * @date 2024/6/11 9:55
 */
@Service
public class SysRoleWorkbenchItemServiceImpl extends ServiceImpl<SysRoleWorkbenchItemMapper, SysRoleWorkbenchItem> implements ISysRoleWorkbenchItemService {
    @Resource
    private ISysWorkbenchItemService workbenchItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSysRoleWorkbenchItem(SysRole role) {
        remove(new LambdaQueryWrapper<SysRoleWorkbenchItem>()
                .eq(SysRoleWorkbenchItem::getRoleId, role.getRoleId()));
        if (null != role.getWorkbenchItemIds()) {
            List<SysRoleWorkbenchItem> itemList = new ArrayList<>(role.getWorkbenchItemIds().length);
            for (Long itemId : role.getWorkbenchItemIds()) {
                SysRoleWorkbenchItem item = new SysRoleWorkbenchItem();
                item.setItemId(itemId);
                item.setRoleId(role.getRoleId());
                item.setCreateBy(LoginHelper.getUsername());
                item.setCreateTime(LocalDateTime.now());
                item.setUserId(LoginHelper.getUserId());
                itemList.add(item);
            }
            return saveBatch(itemList);
        }
        return true;
    }

    @Override
    public List<Long> getItemIdsByRoleId(String roleId) {
        LambdaQueryWrapper<SysRoleWorkbenchItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysRoleWorkbenchItem::getItemId);
        queryWrapper.eq(SysRoleWorkbenchItem::getRoleId, roleId);
        return listObjs(queryWrapper);
    }

}
