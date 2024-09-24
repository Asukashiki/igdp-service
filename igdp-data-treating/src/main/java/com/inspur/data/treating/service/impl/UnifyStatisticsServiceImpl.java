package com.inspur.data.treating.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inspur.assets.domain.AssetsInfo;
import com.inspur.assets.mapper.AssetsInfoMapper;
import com.inspur.assets.monitor.mapper.AssetsAlertCurMapper;
import com.inspur.common.constant.Constants;
import com.inspur.common.enums.PeriodType;
import com.inspur.common.utils.StringUtils;
import com.inspur.data.treating.domain.UnifyStatisticsItem;
import com.inspur.common.core.domain.entity.UnifyStatisticsItemValue;
import com.inspur.data.treating.mapper.AssetsApplicationDataMapper;
import com.inspur.data.treating.mapper.UnifyStatisticsItemMapper;
import com.inspur.data.treating.service.IUnifyStatisticsItemValueService;
import com.inspur.data.treating.service.IUnifyStatisticsService;
import com.inspur.workorder.mapper.TodoItemMapper;
import com.inspur.workorder.mapper.WorkOrderKnowledgeBaseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName UnifyStatisticsServiceImpl
 * @date 2024/7/17 16:56
 */
@Service("unifyStatisticsService")
public class UnifyStatisticsServiceImpl implements IUnifyStatisticsService {
    @Resource
    private AssetsInfoMapper assetsInfoMapper;
    @Resource
    private UnifyStatisticsItemMapper unifyStatisticsItemMapper;
    @Resource
    private IUnifyStatisticsItemValueService unifyStatisticsItemValueService;
    @Resource
    private AssetsAlertCurMapper assetsAlertCurMapper;
    @Resource
    private TodoItemMapper todoItemMapper;
    @Resource
    private AssetsApplicationDataMapper applicationDataMapper;
    @Resource
    private WorkOrderKnowledgeBaseMapper workOrderKnowledgeBaseMapper;

    @Override
    public void statisticsTaskDay() {

        UnifyStatisticsItem queryItem = new UnifyStatisticsItem();
        queryItem.setFrequency(PeriodType.DAY.getCode());
        queryItem.setStatus(Constants.STATUS_VALID);
        List<UnifyStatisticsItem> itemList = unifyStatisticsItemMapper.getList(queryItem);
        handleTaskByItemList(itemList);
    }

    @Override
    public void statisticsTaskMinute() {
        UnifyStatisticsItem queryItem = new UnifyStatisticsItem();
        queryItem.setFrequency(PeriodType.MINUTE.getCode());
        queryItem.setStatus(Constants.STATUS_VALID);
        List<UnifyStatisticsItem> itemList = unifyStatisticsItemMapper.getList(queryItem);
        handleTaskByItemList(itemList);
    }

    @Override
    public void statisticsTaskHour() {
        UnifyStatisticsItem queryItem = new UnifyStatisticsItem();
        queryItem.setFrequency(PeriodType.HOUR.getCode());
        queryItem.setStatus(Constants.STATUS_VALID);
        List<UnifyStatisticsItem> itemList = unifyStatisticsItemMapper.getList(queryItem);
        handleTaskByItemList(itemList);
    }

    @Override
    public void statisticsTaskWeek() {

    }

    @Override
    public void statisticsTaskMonth() {
        UnifyStatisticsItem queryItem = new UnifyStatisticsItem();
        queryItem.setFrequency(PeriodType.MONTH.getCode());
        queryItem.setStatus(Constants.STATUS_VALID);
        List<UnifyStatisticsItem> itemList = unifyStatisticsItemMapper.getList(queryItem);
        handleTaskByItemList(itemList);
    }

    @Override
    public void statisticsTaskYear() {
        UnifyStatisticsItem queryItem = new UnifyStatisticsItem();
        queryItem.setFrequency(PeriodType.YEAR.getCode());
        queryItem.setStatus(Constants.STATUS_VALID);
        List<UnifyStatisticsItem> itemList = unifyStatisticsItemMapper.getList(queryItem);
        handleTaskByItemList(itemList);
    }

    private void handleTaskByItemList(List<UnifyStatisticsItem> itemList) {
        if (null != itemList && !itemList.isEmpty()) {
            for (UnifyStatisticsItem item : itemList) {
                String source = item.getDataSource();
                switch (source) {
                    case "assets":
                        handleAssets(item);
                        break;
                    case "assets_alert":
                        handleAssetsAlert(item);
                        break;
                    case "work_order":
                        handleWorkOrder(item);
                        break;
                    case "application":
                        handleApplication(item);
                        break;
                    case "work_order_knowledge":
                        handleKnowledge(item);
                        break;
                    case "inspection":
                        handleInspection(item);
                        break;
                    default:
                        break;

                }
            }
        }
    }

    /**
     * 统计资产
     */
    private void handleAssets(UnifyStatisticsItem item) {
        if (StringUtils.isNotEmpty(item.getSelectSql())) {
            List<UnifyStatisticsItemValue> voList = assetsInfoMapper.selectStatisticsItemValueListBySql(item.getSelectSql());
            saveValueByList(voList, item);
        }
    }


    /**
     * 资产告警数据
     */
    private void handleAssetsAlert(UnifyStatisticsItem item) {
        if (StringUtils.isNotEmpty(item.getSelectSql())) {
            List<UnifyStatisticsItemValue> voList = assetsAlertCurMapper.selectStatisticsItemValueListBySql(item.getSelectSql());
            saveValueByList(voList, item);
        }
    }


    /**
     * 工单数据
     */
    private void handleWorkOrder(UnifyStatisticsItem item) {
        if (StringUtils.isNotEmpty(item.getSelectSql())) {
            List<UnifyStatisticsItemValue> voList = todoItemMapper.selectStatisticsItemValueListBySql(item.getSelectSql());
            saveValueByList(voList, item);
        }
    }

    /**
     * 应用系统数据相关
     */
    private void handleApplication(UnifyStatisticsItem item) {
        if (StringUtils.isNotEmpty(item.getSelectSql())) {
            List<UnifyStatisticsItemValue> voList = applicationDataMapper.selectStatisticsItemValueListBySql(item.getSelectSql());
            saveValueByList(voList, item);
        }
    }

    /**
     * 工单知识库
     */
    private void handleKnowledge(UnifyStatisticsItem item) {
        if (StringUtils.isNotEmpty(item.getSelectSql())) {
            List<UnifyStatisticsItemValue> voList = workOrderKnowledgeBaseMapper.selectStatisticsItemValueListBySql(item.getSelectSql());
            saveValueByList(voList, item);
        }
    }

    /**
     * 巡检相关
     */
    private void handleInspection(UnifyStatisticsItem item) {

    }

    private void saveValueByList(List<UnifyStatisticsItemValue> valueList,UnifyStatisticsItem item){
        if (null != valueList && !valueList.isEmpty()) {
            List<UnifyStatisticsItemValue> itemValueList = new ArrayList<>(valueList.size() + 1);
            for (UnifyStatisticsItemValue itemValue : valueList) {
                handleItemValue(itemValue,item);
                itemValueList.add(itemValue);
            }
            unifyStatisticsItemValueService.saveValueList(itemValueList);
        }
    }

    /**
     * 保存统计数据
     */
    private void saveValueByMapList(List<Map<String, Object>> voList, UnifyStatisticsItem item) {
        if (null != voList && !voList.isEmpty()) {
            List<UnifyStatisticsItemValue> itemValueList = new ArrayList<>(voList.size() + 1);
            BigDecimal total = BigDecimal.ZERO;
            for (Map<String, Object> vo : voList) {
                UnifyStatisticsItemValue itemValue = createItemValue(item);
                BigDecimal value = new BigDecimal(vo.get("value").toString());
                total = total.add(value);
                itemValue.setValue(value);
                itemValue.setTarget(vo.get("target").toString());
                itemValue.setTargetName(vo.get("target_name").toString());
                itemValue.setStatisticsCode(vo.get("statistics_code").toString());
                itemValue.setStatisticsName(vo.get("statistics_name").toString());
                if (null != vo.get("date")) {
                    itemValue.setDate(vo.get("date").toString());
                }
                itemValueList.add(itemValue);
            }
            unifyStatisticsItemValueService.saveValueList(itemValueList);
        }
    }

    private void handleItemValue(UnifyStatisticsItemValue itemValue,UnifyStatisticsItem item){
        itemValue.setItemId(item.getId());
        itemValue.setItemCode(item.getCode());
        if (StringUtils.isEmpty(itemValue.getDate())) {
            itemValue.setDate(getDateByPeriod(item.getPeriod()));
        }
        itemValue.setType(item.getType());
        itemValue.setCategory(item.getCategory());
        itemValue.setDescription(item.getDescription());
    }

    private UnifyStatisticsItemValue createItemValue(UnifyStatisticsItem item) {
        UnifyStatisticsItemValue itemValue = new UnifyStatisticsItemValue();
        itemValue.setItemId(item.getId());
        itemValue.setItemCode(item.getCode());
        if (StringUtils.isEmpty(itemValue.getDate())) {
            itemValue.setDate(getDateByPeriod(item.getPeriod()));
        }
        itemValue.setType(item.getType());
        itemValue.setCategory(item.getCategory());
        itemValue.setDescription(item.getDescription());
        return itemValue;
    }


    private String getDateByPeriod(String period) {
        LocalDateTime now = LocalDateTime.now();
        switch (period) {
            case "year":
                return String.valueOf(now.getYear());
            case "month":
                return now.getYear() + "-" + now.getMonthValue();
            case "day":
                return LocalDate.now().toString();
            case "hour":
                return LocalDateTime.of(LocalDate.now(), LocalTime.of(now.getHour(), 0, 0)).toString();
            case "minute":
                return LocalDateTime.of(LocalDate.now(), LocalTime.of(now.getHour(), now.getMinute(), 0)).toString();
            default:
                return now.toString();
        }
    }

}
