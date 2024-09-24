package com.inspur.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.system.domain.SysFastItem;
import com.inspur.system.domain.SysUserFastItem;
import com.inspur.system.mapper.SysUserFastItemMapper;
import com.inspur.system.service.ISysUserFastItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysUserFastItemServiceImpl
 * @date 2024/6/14 17:24
 */
@Service
public class SysUserFastItemServiceImpl extends ServiceImpl<SysUserFastItemMapper, SysUserFastItem> implements ISysUserFastItemService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addItem(String userId, String[] itemIds) {
        //先删除旧的，再保存新的
        remove(new LambdaQueryWrapper<SysUserFastItem>()
                .eq(SysUserFastItem::getUserId, userId));
        //保存新的
        if (itemIds != null && itemIds.length > 0) {
            int num = 0;
            List<SysUserFastItem> itemList = new ArrayList<SysUserFastItem>();
            for (String itemId : itemIds) {
                SysUserFastItem item = new SysUserFastItem();
                item.setUserId(userId);
                item.setItemId(itemId);
                item.setCreateTime(LocalDateTime.now());    
                num++;
                item.setSortNumber(num);
                itemList.add(item);
            }
            saveBatch(itemList);
        }
    }

    @Override
    public void deleteItemByItemId(String userId, String itemId) {

        remove(new LambdaQueryWrapper<SysUserFastItem>()
                .eq(SysUserFastItem::getUserId, userId)
                .eq(SysUserFastItem::getItemId, itemId));
    }


}
