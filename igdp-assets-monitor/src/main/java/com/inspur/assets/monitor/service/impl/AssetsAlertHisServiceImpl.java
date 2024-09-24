package com.inspur.assets.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.monitor.domain.AssetsAlertCur;
import com.inspur.assets.monitor.domain.AssetsAlertHis;
import com.inspur.assets.monitor.mapper.AssetsAlertHisMapper;
import com.inspur.assets.monitor.service.IAssetsAlertHisService;
import com.inspur.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName AssetsAlertHisServiceImpl
 * @date 2024/7/8 11:25
 */
@Service
public class AssetsAlertHisServiceImpl extends ServiceImpl<AssetsAlertHisMapper, AssetsAlertHis> implements IAssetsAlertHisService {
    @Override
    public List<AssetsAlertHis> getList(AssetsAlertHis queryParam) {
        LambdaQueryWrapper<AssetsAlertHis> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(queryParam.getAssetsId())){
            queryWrapper.eq(AssetsAlertHis::getAssetsId,queryParam.getAssetsId());
        }
        if(StringUtils.isNotEmpty(queryParam.getAssetsCode())){
            queryWrapper.like(AssetsAlertHis::getAssetsCode,queryParam.getAssetsCode());
        }
        if(StringUtils.isNotEmpty(queryParam.getTarget())){
            queryWrapper.like(AssetsAlertHis::getTarget,queryParam.getTarget());
        }
        if(StringUtils.isNotEmpty(queryParam.getAssetsType())){
            queryWrapper.likeRight(AssetsAlertHis::getAssetsType,queryParam.getAssetsType());
        }
        if(StringUtils.isNotEmpty(queryParam.getGrade())){
            queryWrapper.eq(AssetsAlertHis::getGrade,queryParam.getGrade());
        }
        if(StringUtils.isNotEmpty(queryParam.getAlertKey())){
            queryWrapper.like(AssetsAlertHis::getAlertKey,queryParam.getAlertKey());
        }
        if (StringUtils.isNotEmpty(queryParam.getTitle())){
            queryWrapper.like(AssetsAlertHis::getTitle,queryParam.getTitle());
        }
        LocalDateTime beginTime = queryParam.getBeginTime();
        LocalDateTime endTime = queryParam.getEndTime();
        if(null != beginTime){
            queryWrapper.ge(AssetsAlertHis::getBeginTime, beginTime);
        }
        if(null != endTime){
            queryWrapper.le(AssetsAlertHis::getEndTime, endTime);
        }
        return list(queryWrapper);
    }

    @Override
    public void saveAlertHis(AssetsAlertHis alertHis) {

    }
}
