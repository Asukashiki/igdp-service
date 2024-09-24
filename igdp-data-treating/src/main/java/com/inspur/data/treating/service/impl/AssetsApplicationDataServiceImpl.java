package com.inspur.data.treating.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.constant.HttpStatus;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.PeriodType;
import com.inspur.common.utils.EnumUtil;
import com.inspur.common.utils.StringUtils;
import com.inspur.data.treating.domain.AssetsApplicationData;
import com.inspur.data.treating.domain.payload.ApplicationDataPayload;
import com.inspur.data.treating.enums.DataType;
import com.inspur.data.treating.mapper.AssetsApplicationDataMapper;
import com.inspur.data.treating.service.IAssetsApplicationDataService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ApplicationDataServiceImpl
 * @date 2024/7/17 14:57
 */
@Service
public class AssetsApplicationDataServiceImpl extends ServiceImpl<AssetsApplicationDataMapper, AssetsApplicationData> implements IAssetsApplicationDataService {
    @Override
    public AjaxResult saveApplicationData(ApplicationDataPayload payload) {

        AjaxResult checkResult = checkPayload(payload);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        //处理日期
        handleDateForPayload(payload);
        //判断是否已有数据，根据target+period+dataType+date唯一索引判断
        AssetsApplicationData data = getOne(new LambdaQueryWrapper<AssetsApplicationData>()
                .eq(AssetsApplicationData::getTarget, payload.getTarget())
                .eq(AssetsApplicationData::getPeriod, payload.getPeriod())
                .eq(AssetsApplicationData::getDataType, payload.getDataType())
                .eq(AssetsApplicationData::getDate, payload.getDate()));
        if (data != null) {
            data.setValue(new BigDecimal(payload.getValue()));
            data.setUpdateTime(LocalDateTime.now());
            updateById(data);
        } else {
            data = new AssetsApplicationData();
            data.setTarget(payload.getTarget());
            data.setPeriod(payload.getPeriod());
            data.setDataType(payload.getDataType());
            data.setDate(payload.getDate());
            data.setValue(new BigDecimal(payload.getValue()));
            data.setCreateTime(LocalDateTime.now());
            DataType dataType = EnumUtil.getByCode(payload.getDataType(), DataType.class);
            if (null != dataType) {
                data.setDataTypeName(dataType.getDescription());
            }
            save(data);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult checkPayload(ApplicationDataPayload payload) {
        //校验数据合规性
        try {
            if (payload.getPeriod().equals(PeriodType.YEAR.getCode())) {
                int year = Integer.parseInt(payload.getDate());
                if (year > LocalDate.now().getYear()) {
                    return AjaxResult.error(HttpStatus.BAD_REQUEST, "传入的年份不能超过当前年份");
                }
            } else if (payload.getPeriod().equals(PeriodType.MONTH.getCode())) {
                String[] dates = payload.getDate().split("-");
                if (Integer.parseInt(dates[0]) > LocalDate.now().getYear()) {
                    return AjaxResult.error(HttpStatus.BAD_REQUEST, "传入的年份不能超过当前年份");
                }
                if (Integer.parseInt(dates[1]) > LocalDate.now().getMonthValue()) {
                    return AjaxResult.error(HttpStatus.BAD_REQUEST, "传入的月份不能超过当前年份");
                }
            } else if (payload.getPeriod().equals(PeriodType.DAY.getCode()) || payload.getPeriod().equals(PeriodType.HOUR.getCode())) {
                LocalDate date = LocalDate.parse(payload.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                if (date.isAfter(LocalDate.now())) {
                    return AjaxResult.error(HttpStatus.BAD_REQUEST, "传入的日期不能晚于当前日期");
                }
                if (payload.getPeriod().equals(PeriodType.HOUR.getCode())) {
                    if (StringUtils.isEmpty(payload.getTime())) {
                        return AjaxResult.error(HttpStatus.BAD_REQUEST, "小时统计数据对应的time参数不能为空");
                    }
                    //判断日期格式是否正确
                    int hour = Integer.parseInt(payload.getTime());
                    if (hour < LocalTime.MIN.getHour() || hour > LocalTime.MAX.getHour()) {
                        return AjaxResult.error(HttpStatus.BAD_REQUEST, "传入小时参数不合理，必须传入0-23之间的整点");
                    }
                }
            } else {
                return AjaxResult.error(HttpStatus.BAD_REQUEST, "period传入数据格式异常");
            }
        } catch (Exception e) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "date参数传入的数据格式异常");
        }
        return AjaxResult.success();
    }

    @Override
    public List<AssetsApplicationData> getList(AssetsApplicationData payload) {
        LambdaQueryWrapper<AssetsApplicationData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AssetsApplicationData::getDataType, "visit_num");
        queryWrapper.eq(StringUtils.isNotEmpty(payload.getTarget()), AssetsApplicationData::getTarget, payload.getTarget());
        queryWrapper.ge(StringUtils.isNotEmpty(payload.getDate()), AssetsApplicationData::getDate, payload.getDate());
        return list(queryWrapper);
    }

    @Override
    public List<AssetsApplicationData> getApplicationList(AssetsApplicationData payload) {
        LambdaQueryWrapper<AssetsApplicationData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(payload.getTarget()), AssetsApplicationData::getTarget, payload.getTarget());
        queryWrapper.ge(StringUtils.isNotEmpty(payload.getDate()), AssetsApplicationData::getDate, payload.getDate());
        queryWrapper.eq(AssetsApplicationData::getPeriod, "day");
        return list(queryWrapper);
    }

    @Override
    public Map<String, Object> getMonitoringDetails(AssetsApplicationData payload) {
        LambdaQueryWrapper<AssetsApplicationData> queryWrapper = new LambdaQueryWrapper<>();
        Map<String, Object> map = new HashMap<>();
        queryWrapper.eq(AssetsApplicationData::getDataType, "visit_num");
        queryWrapper.eq(StringUtils.isNotEmpty(payload.getTarget()), AssetsApplicationData::getTarget, payload.getTarget());
        queryWrapper.ge(StringUtils.isNotEmpty(payload.getDate()), AssetsApplicationData::getDate, LocalDate.now());
        queryWrapper.orderByDesc(AssetsApplicationData::getValue).last("LIMIT 1");
        List<AssetsApplicationData> assetsApplicationData = list(queryWrapper);
        map.put("visitnum", assetsApplicationData.get(0).getValue());
        Random rand = new Random();
        map.put("cpu", rand.nextInt(100));
        map.put("memory", rand.nextInt(100));
        map.put("harddisk", rand.nextInt(100));
        return map;
    }


    /**
     * 根据传入payload处理日期
     */
    private void handleDateForPayload(ApplicationDataPayload payload) {
        if (payload.getPeriod().equals(PeriodType.HOUR.getCode())) {

            LocalDateTime date = LocalDateTime.of(LocalDate.parse(payload.getDate(), DateTimeFormatter.ISO_LOCAL_DATE),
                    LocalTime.of(Integer.parseInt(payload.getTime()), 0, 0));
            payload.setDate(LocalDateTimeUtil.formatNormal(date));
        }
    }
}
