package com.inspur.assets.monitor.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.domain.AssetsCorrelation;
import com.inspur.assets.domain.dto.AssetsStatisticsDto;
import com.inspur.assets.monitor.domain.AssetsAlertCur;
import com.inspur.assets.monitor.mapper.AssetsAlertCurMapper;
import com.inspur.assets.monitor.service.IAssetsAlertCurService;
import com.inspur.assets.service.IAssetsCorrelationService;
import com.inspur.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName AssetsAlertCurServiceImpl
 * @date 2024/7/8 11:21
 */
@Service
public class AssetsAlertCurServiceImpl extends ServiceImpl<AssetsAlertCurMapper, AssetsAlertCur> implements IAssetsAlertCurService {

    @Resource
    private IAssetsCorrelationService assetsCorrelationService;

    @Resource
    AssetsAlertCurMapper assetsAlertCurMapper;

    @Override
    public List<AssetsAlertCur> getList(AssetsAlertCur param) {
        LambdaQueryWrapper<AssetsAlertCur> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(param.getAssetsId())) {
            queryWrapper.eq(AssetsAlertCur::getAssetsId, param.getAssetsId());
        }
        if (StringUtils.isNotEmpty(param.getAssetsCode())) {
            queryWrapper.like(AssetsAlertCur::getAssetsCode, param.getAssetsCode());
        }
        if (StringUtils.isNotEmpty(param.getTarget())) {
            queryWrapper.like(AssetsAlertCur::getTarget, param.getTarget());
        }
        if (StringUtils.isNotEmpty(param.getAssetsType())) {
            queryWrapper.likeRight(AssetsAlertCur::getAssetsType, param.getAssetsType());
        }
        if (StringUtils.isNotEmpty(param.getAlertKey())) {
            queryWrapper.like(AssetsAlertCur::getAlertKey, param.getAlertKey());
        }
        if (StringUtils.isNotEmpty(param.getGrade())) {
            queryWrapper.eq(AssetsAlertCur::getGrade, param.getGrade());
        }
        if (StringUtils.isNotEmpty(param.getTitle())) {
            queryWrapper.like(AssetsAlertCur::getTitle, param.getTitle());
        }
        LocalDateTime beginTime = param.getBeginTime();
        LocalDateTime endTime = param.getEndTime();
        if (null != beginTime) {
            queryWrapper.ge(AssetsAlertCur::getBeginTime, beginTime);
        }
        if (null != endTime) {
            queryWrapper.le(AssetsAlertCur::getEndTime, endTime);
        }
        return list(queryWrapper);
    }

    @Override
    public void saveAssetsAlert(AssetsAlertCur assetsAlertCur) {

        assetsAlertCur.setCreateTime(LocalDateTime.now());
        save(assetsAlertCur);
        //todo 异步更新资产信息到告警信息中
    }


    /**
     * 更新告警信息解除
     * 根据target、告警类型、资产类型进行告警解除，同时同步到历史告警中
     *
     * @param assetsAlertCur 告警信息
     */
    @Override
    public void reliveAlert(AssetsAlertCur assetsAlertCur) {

    }

    @Override
    public List<AssetsAlertCur> getWarnList(AssetsAlertCur param) {
        LambdaQueryWrapper<AssetsAlertCur> queryWrapper = new LambdaQueryWrapper<>();
        AssetsCorrelation assetsCorrelation = new AssetsCorrelation();
        assetsCorrelation.setParentCode(param.getAssetsCode());
        List<AssetsCorrelation> assetsCorrelationList = assetsCorrelationService.getCorrelation(assetsCorrelation);
        List<String> codeList= new ArrayList<>();
        assetsCorrelationList.forEach( correlation -> {
            codeList.add(correlation.getCorrelationCode());
        });
        queryWrapper.in(AssetsAlertCur::getAssetsCode,codeList);
        return this.baseMapper.getWarnList(param.getAssetsCode());
    }

    @Override
    public List<AssetsAlertCur> getListHistory(AssetsAlertCur param) {
        LambdaQueryWrapper<AssetsAlertCur> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(param.getSource()),AssetsAlertCur::getSource,param.getSource());
        queryWrapper.eq(StringUtils.isNotEmpty(param.getAssetsCode()),AssetsAlertCur::getAssetsCode,param.getAssetsCode());
        queryWrapper.eq(StringUtils.isNotEmpty(param.getGrade()),AssetsAlertCur::getGrade,param.getGrade());
        LocalDateTime beginTime = param.getBeginTime();
        LocalDateTime endTime = param.getEndTime();
        if (null != beginTime) {
            queryWrapper.ge(AssetsAlertCur::getStartTime, beginTime);
        }
        if (null != endTime) {
            queryWrapper.le(AssetsAlertCur::getStartTime, endTime);
        }
        queryWrapper.orderByDesc(AssetsAlertCur::getStartTime);
        return list(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> getListDiagram(AssetsAlertCur param) {
        QueryWrapper<AssetsAlertCur> wrapper = new QueryWrapper<>();
        wrapper.select("count(1) as count,LEFT(start_time,10) as start_time,source");
        wrapper.eq(StringUtils.isNotEmpty(param.getSource()),"source",param.getSource());
        wrapper.eq(StringUtils.isNotEmpty(param.getAssetsCode()),"assets_type",param.getAssetsType());
        wrapper.isNotNull("assets_type");
        wrapper.isNotNull("source");
        wrapper.eq(StringUtils.isNotEmpty(param.getGrade()),"grade",param.getGrade());
        LocalDateTime beginTime = param.getBeginTime();
        LocalDateTime endTime = param.getEndTime();
        if (null != beginTime) {
            wrapper.ge("LEFT(start_time,10)", beginTime);
        }
        if (null != endTime) {
            wrapper.le("LEFT(start_time,10)", endTime);
        }
        wrapper.groupBy("LEFT(start_time,10),source");
        wrapper.orderByAsc("LEFT(start_time,10)");
        return listMaps(wrapper);
    }

    @Override
    public List<Map<String, Object>> getListChart(AssetsAlertCur param) {
        QueryWrapper<AssetsAlertCur> wrapper = new QueryWrapper<>();
        LocalDateTime beginTime = param.getBeginTime();
        LocalDateTime endTime = param.getEndTime();
        StringBuffer sql = new StringBuffer();
        while (!beginTime.isAfter(endTime)){
            LocalDateTime dateTime = beginTime.plusDays(1);
             sql.append(" COUNT(case when LEFT(start_time,10) = " + "'").append(dateTime).append("'").append(" then 1 end)").append(dateTime).append(" ,");
        }
        sql.append("count(1) as count,source,assets_type");
        wrapper.select(sql.toString());
        wrapper.eq(StringUtils.isNotEmpty(param.getSource()),"source",param.getSource());
        wrapper.eq(StringUtils.isNotEmpty(param.getAssetsCode()),"assets_type",param.getAssetsType());
        wrapper.isNotNull("assets_type");
        wrapper.isNotNull("source");
        wrapper.eq(StringUtils.isNotEmpty(param.getGrade()),"grade",param.getGrade());

        if (null != beginTime) {
            wrapper.ge("LEFT(start_time,10)", beginTime);
        }
        if (null != endTime) {
            wrapper.le("LEFT(start_time,10)", endTime);
        }
        wrapper.groupBy("source,assets_type");
        List<Map<String, Object>> wrapperMap = listMaps(wrapper);

        return wrapperMap;
    }

    @Override
    public List<Map<String, Object>> getListBarchart(AssetsAlertCur param) {
        QueryWrapper<AssetsAlertCur> wrapper = new QueryWrapper<>();
        wrapper.select("count(1) as count,LEFT(start_time,10) as start_time,assets_type");
        wrapper.eq(StringUtils.isNotEmpty(param.getSource()),"source",param.getSource());
        wrapper.eq(StringUtils.isNotEmpty(param.getAssetsCode()),"assets_type",param.getAssetsType());
        wrapper.isNotNull("assets_type");
        wrapper.isNotNull("source");
        wrapper.eq(StringUtils.isNotEmpty(param.getGrade()),"grade",param.getGrade());
        LocalDateTime beginTime = param.getBeginTime();
        LocalDateTime endTime = param.getEndTime();
        if (null != beginTime) {
            wrapper.ge("LEFT(start_time,10)", beginTime);
        }
        if (null != endTime) {
            wrapper.le("LEFT(start_time,10)", endTime);
        }
        wrapper.groupBy("LEFT(start_time,10),assets_type");
        wrapper.orderByAsc("LEFT(start_time,10)");
        return listMaps(wrapper);
    }

    @Override
    public JSONObject statisticsWithStatus() {
        List<AssetsStatisticsDto> statisticsDtoList = assetsAlertCurMapper.getStatisticsDtoListGroupByLocalization();
        if (null != statisticsDtoList && !statisticsDtoList.isEmpty()) {
            List<String> statusValueList = statisticsDtoList.stream().map(AssetsStatisticsDto::getStatusValue).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            Map<String, List<Long>> yData = new HashMap<>(statusValueList.size());
            Map<String, List<AssetsStatisticsDto>> groupByTypeName = statisticsDtoList.stream().collect(Collectors.groupingBy(AssetsStatisticsDto::getTypeName));
            List<String> typeNameList = new ArrayList<>(groupByTypeName.keySet());
            for (String typeName : typeNameList) {
                List<AssetsStatisticsDto> listByTypeName = groupByTypeName.get(typeName);
                Map<String, List<AssetsStatisticsDto>> groupByStatusValue = listByTypeName.stream().collect(Collectors.groupingBy(AssetsStatisticsDto::getStatusValue));
                for (String statusValue : statusValueList) {
                    List<Long> totalList = yData.get(statusValue);
                    if (null == totalList) {
                        totalList = new ArrayList<>();
                    }
                    List<AssetsStatisticsDto> dtoList = groupByStatusValue.get(statusValue);
                    long total = 0;
                    if (null != dtoList && !dtoList.isEmpty()) {
                        total = dtoList.stream().mapToLong(AssetsStatisticsDto::getTotal).sum();
                    }
                    totalList.add(total);
                    yData.put(statusValue, totalList);
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("xData", typeNameList);
            jsonObject.set("yData", yData);
            return jsonObject;
        }
        return null;
    }

    private AssetsAlertCur getAssetsAlertCurByTarget(AssetsAlertCur queryParam) {
        LambdaQueryWrapper<AssetsAlertCur> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AssetsAlertCur::getTarget, queryParam.getTarget());
        queryWrapper.eq(AssetsAlertCur::getAlertType, queryParam.getAlertType());
        queryWrapper.eq(AssetsAlertCur::getContent, queryParam.getContent());
        queryWrapper.eq(AssetsAlertCur::getAlertKey, queryParam.getAlertKey());
        return getOne(queryWrapper);
    }
}
