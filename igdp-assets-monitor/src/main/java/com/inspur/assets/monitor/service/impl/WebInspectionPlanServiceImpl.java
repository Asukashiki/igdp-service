package com.inspur.assets.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.monitor.domain.WebInspectionInfo;
import com.inspur.assets.monitor.domain.WebInspectionPlan;
import com.inspur.assets.monitor.mapper.WebInspectionPlanMapper;
import com.inspur.assets.monitor.service.IWebInspectionInfoService;
import com.inspur.assets.monitor.service.IWebInspectionPlanService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WebInspectionPlanServiceImpl extends ServiceImpl<WebInspectionPlanMapper,WebInspectionPlan> implements IWebInspectionPlanService {
    @Resource
    WebInspectionPlanMapper webInspectionPlanMapper;

    @Resource
    IWebInspectionInfoService webInspectionInfoService;
    @Override
    public List<WebInspectionPlan> SelectWebInspectionPlan(){
        QueryWrapper<WebInspectionPlan> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("jobid","taskid","test_frequency","status","concurrent","misfire_policy","system_name","group_name");

        return webInspectionPlanMapper.selectList(queryWrapper);
    }

    @Override
    public AjaxResult insertWebInspectionPlan(WebInspectionPlan param){
        LambdaQueryWrapper<WebInspectionPlan> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(WebInspectionPlan::getJobid,param.getJobid());
        long exists = webInspectionPlanMapper.selectCount(queryWrapper);
        if (exists > 0) {
            return AjaxResult.error("存在相同任务");
        }
        webInspectionPlanMapper.insert(param);
        webInspectionInfoService.updateTestFrequency(param.getTaskid(),param.getTestFrequency());
        webInspectionInfoService.updateStatus(param.getTaskid(),param.getStatus());

        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateWebInspectionPlan(WebInspectionPlan param){
        webInspectionPlanMapper.updateById(param);
        webInspectionInfoService.updateTestFrequency(param.getTaskid(),param.getTestFrequency());
        webInspectionInfoService.updateStatus(param.getTaskid(),param.getStatus());
        return AjaxResult.success();
    }

    @Override
    public AjaxResult deleteWebInspectionPlan(int param){
        webInspectionPlanMapper.deleteById(param);
        webInspectionInfoService.updateTestFrequency(param,"");
        webInspectionInfoService.updateStatus(param,"");
        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateStatus(int jobid,String param){
        LambdaUpdateWrapper<WebInspectionPlan> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(WebInspectionPlan::getJobid,jobid).set(WebInspectionPlan::getStatus,param);
        webInspectionPlanMapper.update(updateWrapper);
        return AjaxResult.success();
    }

    @Override
    public List<WebInspectionPlan> QueryWebInspectionPlan(WebInspectionPlan param){
        LambdaQueryWrapper<WebInspectionPlan> queryWrapper=new LambdaQueryWrapper<>();
        if(param.getStatus()!=null){
            queryWrapper.eq(WebInspectionPlan::getStatus,param.getStatus());
        }
        if (param.getSystemName()!=null){
            queryWrapper.like(WebInspectionPlan::getSystemName,param.getSystemName());
        }
        if (param.getTaskid()!=0){
            queryWrapper.eq(WebInspectionPlan::getTaskid,param.getTaskid());
        }
        queryWrapper.select(WebInspectionPlan::getJobid,
                WebInspectionPlan::getTaskid,
                WebInspectionPlan::getTestFrequency,
                WebInspectionPlan::getStatus,
                WebInspectionPlan::getConcurrent,
                WebInspectionPlan::getMisfirePolicy,
                WebInspectionPlan::getSystemName,
                WebInspectionPlan::getGroupName);
        return webInspectionPlanMapper.selectList(queryWrapper);

    }
}
