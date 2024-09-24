package com.inspur.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.LoginHelper;
import com.inspur.system.domain.SysFastItem;
import com.inspur.system.domain.SysUserFastItem;
import com.inspur.system.domain.SysUserRole;
import com.inspur.system.mapper.SysFastItemMapper;
import com.inspur.system.mapper.SysUserFastItemMapper;
import com.inspur.system.service.ISysFastItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.inspur.common.utils.LoginHelper.getUserId;
import static com.inspur.common.utils.LoginHelper.getUsername;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysFastItemServiceImpl
 * @date 2024/6/14 17:25
 */
@Service
public class SysFastItemServiceImpl extends ServiceImpl<SysFastItemMapper, SysFastItem> implements ISysFastItemService {
    @Resource
    private SysUserFastItemMapper userFastItemMapper;

    @Override
    public List<SysFastItem> getList(SysFastItem queryParam) {
        LambdaQueryWrapper<SysFastItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(queryParam.getName()), SysFastItem::getName, queryParam.getName());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getStatus()), SysFastItem::getStatus, queryParam.getStatus());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getType()), SysFastItem::getType, queryParam.getType());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getAppId()), SysFastItem::getAppId, queryParam.getAppId());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getUserId()), SysFastItem::getUserId, queryParam.getUserId());
        queryWrapper.orderByAsc(SysFastItem::getSortNumber);
        return list(queryWrapper);
    }

    @Override
    public AjaxResult addItem(SysFastItem item) {

        AjaxResult result = checkItem(item);
        if (result.isError()) {
            return result;
        }
        item.setCreateBy(LoginHelper.getUsername());
        item.setUserId(LoginHelper.getUserId());
        item.setCreateTime(LocalDateTime.now());
        save(item);
        return result;
    }

    @Override
    public AjaxResult updateItem(SysFastItem item) {
        AjaxResult result = checkItem(item);
        if (result.isError()) {
            return result;
        }
        if (StrUtil.isEmpty(item.getId())) {
            return AjaxResult.error("id不能为空");
        }
        item.setUpdateBy(LoginHelper.getUsername());
        item.setUpdateTime(LocalDateTime.now());
        updateById(item);
        return result;
    }

    @Override
    public AjaxResult deleteItem(String id) {
        boolean result = removeById(id);
        if (result) {
            //先删除，再删除分配到user的信息
            userFastItemMapper.delete(new LambdaQueryWrapper<SysUserFastItem>()
                    .eq(SysUserFastItem::getItemId, id));
            return AjaxResult.success();
        } else {
            return AjaxResult.error("没有相关信息");
        }
    }

    @Override
    public List<SysFastItem> getListByUserId(String userId) {
        return this.baseMapper.selectListByUserId(userId);
    }

    @Override
    public List<SysFastItem> getOtherList(List<SysFastItem> itemList) {
        LambdaQueryWrapper<SysFastItem> queryWrapper = new LambdaQueryWrapper<>();
        List<String> num = new ArrayList<>();
        itemList.forEach( sysFastItem -> {
            num.add(sysFastItem.getId());
        });
        if(!num.isEmpty()){
            queryWrapper.notIn(SysFastItem::getId,num);
        }
        return list(queryWrapper);
    }

    private AjaxResult checkItem(SysFastItem item) {
        if (StrUtil.isEmpty(item.getName())) {
            return AjaxResult.error("名称不能为空");
        }
        if (StrUtil.isEmpty(item.getAppId())) {
            return AjaxResult.error("所属应用不能空");
        }
        if (StrUtil.isEmpty(item.getLink()) && StrUtil.isEmpty(item.getRoutePath())) {
            return AjaxResult.error("连接或者路由不可都为空");
        }

        return AjaxResult.success();
    }

    @Override
    public AjaxResult addFastItem(SysFastItem sysFastItem) {
        long count = this.count(new LambdaQueryWrapper<SysFastItem>()
                .eq(SysFastItem::getRoutePath,sysFastItem.getRoutePath()));
        if (count>0){
            return AjaxResult.error("添加失败，已存在该路由地址信息") ;

        }
        sysFastItem.setStatus("0");
        sysFastItem.setCreateBy(getUsername());
        sysFastItem.setUserId(getUserId());
        sysFastItem.setCreateTime(LocalDateTime.now());
        return AjaxResult.success(save(sysFastItem));
    }

    @Override
    public AjaxResult removeItem(String[] id) {
        for(String item:id){
            removeById(item);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateFastItem(SysFastItem sysFastItem) {
        long count = this.count(new LambdaQueryWrapper<SysFastItem>()
                .eq(SysFastItem::getRoutePath,sysFastItem.getRoutePath())
                .ne(SysFastItem::getId,sysFastItem.getId()));
        if (count>0){
            return AjaxResult.error("修改失败，已存在该路由地址信息") ;

        }
        sysFastItem.setUpdateBy(getUsername());
        sysFastItem.setUpdateTime(LocalDateTime.now());
        return AjaxResult.success(updateById(sysFastItem));
    }

    @Override
    public List<SysFastItem> getFastItemList(String keyword) {
        LambdaQueryWrapper<SysFastItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(keyword),SysFastItem::getName,keyword);
        return list(queryWrapper);
    }

    @Override
    public List<SysFastItem> getFastItemById(String id) {
        LambdaQueryWrapper<SysFastItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(id),SysFastItem::getId,id);
        return list(queryWrapper);
    }
}
