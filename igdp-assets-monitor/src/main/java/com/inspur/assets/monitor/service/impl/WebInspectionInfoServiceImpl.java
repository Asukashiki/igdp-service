package com.inspur.assets.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.monitor.domain.WebAutoTestInput;
import com.inspur.assets.monitor.domain.WebInspectionInfo;
import com.inspur.assets.monitor.mapper.WebAutoTestInputMapper;
import com.inspur.assets.monitor.mapper.WebInspectionInfoMapper;
import com.inspur.assets.monitor.mapper.WebInspectionSubmenuMapper;
import com.inspur.assets.monitor.service.IWebAutoTestInputService;
import com.inspur.assets.monitor.service.IWebInspectionInfoService;
import com.inspur.assets.monitor.service.IWebInspectionSubmenuService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @author wangxinyang
 * @version 1.0
 * @ClassName WebInspectionInfoServiceImpl
 * @date 2024/7/30 10:43
 */
@Service
public class WebInspectionInfoServiceImpl extends ServiceImpl<WebInspectionInfoMapper, WebInspectionInfo> implements IWebInspectionInfoService {
    @Resource
    WebInspectionInfoMapper webInspectionInfoMapper;

    @Resource
    IWebAutoTestInputService webAutoTestInputService;

    @Resource
    WebAutoTestInputMapper webAutoTestInputMapper;
    @Override
    public List<WebInspectionInfo> selectWebInspectionInfoList(){
        QueryWrapper<WebInspectionInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("task_id","system_name","system_address","driver_select","system_id","test_frequency","system_id","captcha_input","password_input","username_input","password","username","captcha_image_location");
        List<WebInspectionInfo> webInspectionInfos=webInspectionInfoMapper.selectList(queryWrapper);
        try {
            for (WebInspectionInfo inspectionInfo : webInspectionInfos) {
                List<WebAutoTestInput> elementList = webAutoTestInputService.queryTargetTestInput(inspectionInfo.getTaskId());
                WebAutoTestInput[] elementArray = new WebAutoTestInput[elementList.size()];
                elementArray = elementList.toArray(elementArray);
                inspectionInfo.setElementList(elementArray);
                System.out.println("query input elementList is" + elementArray);
            }
        }catch(Exception e){
            System.out.println("the error is "+e);
        }
        return webInspectionInfos;
    }
    @Override
    public List<WebInspectionInfo> queryWebInspectionInfoList(WebInspectionInfo param){
        LambdaQueryWrapper<WebInspectionInfo> queryWrapper=new LambdaQueryWrapper<>();
        System.out.println("get task id is "+param.getTaskId());
        if(param.getSystemName()!=null){
            queryWrapper.like(WebInspectionInfo::getSystemName,param.getSystemName());
        }
        if(param.getDriverSelect()!=null){
            queryWrapper.eq(WebInspectionInfo::getDriverSelect,param.getDriverSelect());
        }
        if(param.getSystemAddress()!=null){
            queryWrapper.like(WebInspectionInfo::getSystemAddress,param.getSystemAddress());
        }
        if(param.getTaskId()!=0){
            queryWrapper.eq(WebInspectionInfo::getTaskId,param.getTaskId());
        }
        queryWrapper.select(
                WebInspectionInfo::getTaskId,
                WebInspectionInfo::getSystemName,
                WebInspectionInfo::getSystemAddress,
                WebInspectionInfo::getDriverSelect,
                WebInspectionInfo::getTestFrequency,
                WebInspectionInfo::getCaptchaInput,
                WebInspectionInfo::getCaptchaImageLocation,
                WebInspectionInfo::getUsername,
                WebInspectionInfo::getUsernameInput,
                WebInspectionInfo::getPassword,
                WebInspectionInfo::getPasswordInput,
                WebInspectionInfo::getSystemID
        );
        List<WebInspectionInfo> queryWebInspectionInfos=webInspectionInfoMapper.selectList(queryWrapper);
        for(WebInspectionInfo inspectionInfo:queryWebInspectionInfos){
            List<WebAutoTestInput> elementList=webAutoTestInputService.queryTargetTestInput(inspectionInfo.getTaskId());
            WebAutoTestInput[] elementArray = new WebAutoTestInput[elementList.size()];
            elementArray = elementList.toArray(elementArray);
            inspectionInfo.setElementList(elementArray);
        };
        return queryWebInspectionInfos;
    }
    @Override
    public AjaxResult insertWebInspectionInfo(WebInspectionInfo param){
        System.out.println("Inserting WebInspectionInfo with systemName: " + param.getSystemName() + " and taskID: " + param.getTaskId());
        webInspectionInfoMapper.insert(param);
        int generatedTaskId = param.getTaskId();
        System.out.println("Received data: " + param.getElementList());
        if (param.getElementList() != null) {
            System.out.println("Element List Length: " + param.getElementList().length);
        }
        try {
            if (param.getElementList() != null && param.getElementList().length > 0 ) {
                for (WebAutoTestInput element : param.getElementList()) {
                    element.setTaskNumber(generatedTaskId);
                }
                webAutoTestInputService.insertWebAutoTestInput(param.getElementList());
            }
        }catch (Exception e){
            return AjaxResult.error("插入失败");
        }
        return AjaxResult.success("插入成功");
    }
    @Override
    public AjaxResult updateWebInspectionInfo(WebInspectionInfo param){
        try {
            webInspectionInfoMapper.updateById(param);

            if(webAutoTestInputService.deleteOneTaskAllWebAutoTestInput(param.getTaskId())){
                System.out.println("the new elementList is "+param.getElementList());
                for (WebAutoTestInput element : param.getElementList()) {
                    element.setId(null);
                    element.setTaskNumber(param.getTaskId());
                }
                webAutoTestInputService.insertWebAutoTestInput(param.getElementList());
            }
        }catch (Exception e){
            return AjaxResult.error("插入失败,因为: "+e);
        }
        return AjaxResult.success();
    }
    @Override
    public AjaxResult deleteWebInspectionInfoById(int param){
        webAutoTestInputService.deleteOneTaskAllWebAutoTestInput(param);

        webInspectionInfoMapper.deleteById(param);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateTestFrequency(int taskID,String param){
        LambdaUpdateWrapper<WebInspectionInfo> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(WebInspectionInfo::getTaskId,taskID).set(WebInspectionInfo::getTestFrequency,param);
        webInspectionInfoMapper.update(updateWrapper);
        return AjaxResult.success();
    }

    public AjaxResult updateStatus(int taskID,String param){
        LambdaUpdateWrapper<WebInspectionInfo> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(WebInspectionInfo::getTaskId,taskID).set(WebInspectionInfo::getStatus,param);
        webInspectionInfoMapper.update(updateWrapper);
        return AjaxResult.success();
    }
}
