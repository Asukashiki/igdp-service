package com.inspur.assets.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.monitor.domain.WebAutoTestInput;
import com.inspur.assets.monitor.domain.WebInspectionSubmenu;
import com.inspur.assets.monitor.mapper.WebInspectionSubmenuMapper;
import com.inspur.assets.monitor.service.IWebInspectionSubmenuService;
import com.inspur.common.core.domain.AjaxResult;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Resource;
@Service
public class WebInspectionSubmenuServiceImpl extends ServiceImpl<WebInspectionSubmenuMapper, WebInspectionSubmenu> implements IWebInspectionSubmenuService {
    @Resource
    WebInspectionSubmenuMapper webInspectionSubmenuMapper;

    @Override
    public List<WebInspectionSubmenu> selectWebInspectionSubmenu(WebAutoTestInput param){
        LambdaQueryWrapper<WebInspectionSubmenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WebInspectionSubmenu::getChildMenuId,param.getExistParentMenuNumber());
        return webInspectionSubmenuMapper.selectList(queryWrapper);
    }

    @Override
    public AjaxResult insertWebInspectionSubmenu(WebInspectionSubmenu param){
        webInspectionSubmenuMapper.insert(param);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateWebInspectionSubmenu(WebInspectionSubmenu param){
        LambdaUpdateWrapper<WebInspectionSubmenu>updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(WebInspectionSubmenu::getChildMenuId,param.getChildMenuId());
        updateWrapper.eq(WebInspectionSubmenu::getButtonName,param.getButtonName());
        webInspectionSubmenuMapper.update(param,updateWrapper);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult deleteWebInspectionSubmenu(WebInspectionSubmenu param){
        LambdaQueryWrapper<WebInspectionSubmenu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(WebInspectionSubmenu::getChildMenuId,param.getChildMenuId());
        queryWrapper.eq(WebInspectionSubmenu::getButtonName,param.getButtonName());
        webInspectionSubmenuMapper.delete(queryWrapper);
        return AjaxResult.success();
    }

    @Override
    public void deleteOneElementAllOrderWebInspectionSubmenu(WebAutoTestInput param){
        LambdaQueryWrapper<WebInspectionSubmenu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(WebInspectionSubmenu::getChildMenuId,param.getExistParentMenuNumber());
        webInspectionSubmenuMapper.delete(queryWrapper);
    }

}
