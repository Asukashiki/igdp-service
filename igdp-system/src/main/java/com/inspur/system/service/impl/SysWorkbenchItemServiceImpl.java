package com.inspur.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.LoginHelper;
import com.inspur.system.domain.SysRoleWorkbenchItem;
import com.inspur.system.domain.SysWorkbenchItem;
import com.inspur.system.mapper.SysRoleWorkbenchItemMapper;
import com.inspur.system.mapper.SysWorkbenchItemMapper;
import com.inspur.system.service.ISysWorkbenchItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysWorkbenchItemServiceImpl
 * @date 2024/6/10 16:53
 */
@Service
public class SysWorkbenchItemServiceImpl extends ServiceImpl<SysWorkbenchItemMapper, SysWorkbenchItem> implements ISysWorkbenchItemService {
    @Resource
    private SysRoleWorkbenchItemMapper roleWorkbenchItemMapper;

    @Override
    public List<SysWorkbenchItem> selectItemList(SysWorkbenchItem item) {
        LambdaQueryWrapper<SysWorkbenchItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(item.getName()), SysWorkbenchItem::getName, item.getName());
        queryWrapper.like(StrUtil.isNotEmpty(item.getDescription()), SysWorkbenchItem::getDescription, item.getDescription());
        queryWrapper.eq(null != item.getId(), SysWorkbenchItem::getId, item.getId());
        return list(queryWrapper);
    }

    @Override
    public AjaxResult addItem(SysWorkbenchItem item) {
        //校验code是否冲突
        if (existCode(null, item.getCode())) {
            return AjaxResult.error("编码已经存在");
        }
        boolean result = save(item);
        if (result) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error("保存失败");
        }
    }

    @Override
    public AjaxResult updateItem(SysWorkbenchItem item) {
        if (existCode(item.getId(), item.getCode())) {
            return AjaxResult.error("编码已经存在");
        }
        boolean result = updateById(item);
        if (result) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error("更新失败");
        }
    }

    @Override
    public AjaxResult deleteItemById(String id) {
        //判断是否已经分配了
        LambdaQueryWrapper<SysRoleWorkbenchItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysRoleWorkbenchItem::getRoleId);
        wrapper.eq(SysRoleWorkbenchItem::getItemId, id);
        List<String> roleWorkbenchItems = roleWorkbenchItemMapper.selectObjs(wrapper);
        if (null != roleWorkbenchItems && !roleWorkbenchItems.isEmpty()) {
            return AjaxResult.error("已经分配，不允许删除");
        }
        boolean result = removeById(Integer.parseInt(id));
        if (result) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error("删除失败");
        }
    }

    @Override
    public List<SysWorkbenchItem> getItemListByUserId(String userId) {
        if (LoginHelper.isSuperAdmin()) {
            return list(new LambdaQueryWrapper<SysWorkbenchItem>().select(SysWorkbenchItem::getId, SysWorkbenchItem::getName, SysWorkbenchItem::getCode).orderByAsc(SysWorkbenchItem::getId));
        }
        return this.baseMapper.selectByUserId(userId);
    }

    /**
     * 判断code是否存在
     * true 存在
     * false 不存在
     */
    private boolean existCode(Long id, String code) {
        LambdaQueryWrapper<SysWorkbenchItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysWorkbenchItem::getCode, code);
        SysWorkbenchItem sysWorkbenchItem = super.getOne(wrapper);
        if (null != sysWorkbenchItem) {
            if (null == id) {
                return true;
            }
            return sysWorkbenchItem.getId().equals(id);
        } else {
            return false;
        }
    }
}
