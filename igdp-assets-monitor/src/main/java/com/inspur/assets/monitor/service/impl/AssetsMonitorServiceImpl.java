package com.inspur.assets.monitor.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.domain.AssetsCorrelation;
import com.inspur.assets.domain.AssetsInfo;
import com.inspur.assets.mapper.AssetsMonitorMapper;
import com.inspur.assets.monitor.domain.AssetsAlertCur;
import com.inspur.assets.monitor.domain.payload.PrometheusQueryPayload;
import com.inspur.assets.monitor.enums.AssetsAlertStatus;
import com.inspur.assets.monitor.service.IAssetsAlertCurService;
import com.inspur.assets.monitor.service.IAssetsMonitorService;
import com.inspur.assets.monitor.service.IPrometheusService;
import com.inspur.assets.service.IAssetsCorrelationService;
import com.inspur.assets.service.IAssetsService;
import com.inspur.common.utils.EnumUtil;
import com.inspur.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wanghailong
 * @date 2024/7/30
 */
@Service
@Slf4j
public class AssetsMonitorServiceImpl implements IAssetsMonitorService {
    @Resource
    private IAssetsAlertCurService assetsAlertCurService;
    @Resource
    private IAssetsService assetsService;
    @Resource
    private IPrometheusService prometheusService;
    @Resource
    private IAssetsCorrelationService assetsCorrelationService;

    @Override
    public List<Map<String, Object>> getMonitoringDetails(List<AssetsInfo> list) {
        List<Map<String, Object>> arrayList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            List<AssetsAlertCur> curList = assetsAlertCurService.getList(new AssetsAlertCur());
            Map<String, List<AssetsAlertCur>> groupMap = new HashMap<>();
            if (null != curList && !curList.isEmpty()) {
                groupMap = curList.stream()
                        .filter(item -> StringUtils.isNotBlank(item.getAssetsCode()))
                        .collect(Collectors.groupingBy(AssetsAlertCur::getAssetsCode));
            }
            for (AssetsInfo assets : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("assetsCode", assets.getCode());
                map.put("assetsName", assets.getName());
                map.put("monitoring", assets.getMonitoring());
                map.put("typeName", assets.getTypeName());
                map.put("statusValue", assets.getStatusValue());
                map.put("ip", assets.getIp());
                int emergent = 0;
                int important = 0;
                int secondary = 0;
                int prompt = 0;
                int indeterminacy = 0;
                List<AssetsAlertCur> alertList = groupMap.get(assets.getCode());
                if (alertList != null && !alertList.isEmpty()) {
                    for (AssetsAlertCur alertCur : alertList) {
                        if (alertCur.getGrade().equals(AssetsAlertStatus.Critical.getCode())) {
                            emergent++;
                        }
                        if (alertCur.getGrade().equals(AssetsAlertStatus.Major.getCode())) {
                            important++;
                        }
                        if (alertCur.getGrade().equals(AssetsAlertStatus.Minor.getCode())) {
                            secondary++;
                        }
                        if (alertCur.getGrade().equals(AssetsAlertStatus.Warning.getCode())) {
                            prompt++;
                        }
                        if (alertCur.getGrade().equals(AssetsAlertStatus.Unknown.getCode())) {
                            indeterminacy++;
                        }
                    }
                }
                map.put("emergent", emergent);
                map.put("important", important);
                map.put("secondary", secondary);
                map.put("prompt", prompt);
                map.put("indeterminacy", indeterminacy);
                arrayList.add(map);
            }

        }

        return arrayList;
    }


    @Override
    public List<Map<String, Object>> getApplication() {
        List<Map<String, Object>> applicationList = new ArrayList<>();

        LambdaQueryWrapper<AssetsInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(AssetsInfo::getClassification);
        queryWrapper.ne(AssetsInfo::getClassification, "[]");
        queryWrapper.eq(AssetsInfo::getTypeCode, "0201");
        queryWrapper.eq(AssetsInfo::getDelFlag, "0");
        queryWrapper.groupBy(AssetsInfo::getClassification);
        List<AssetsInfo> classList = assetsService.list(queryWrapper);
        if (classList != null && !classList.isEmpty()) {
            //todo 查询告警列表
            for (AssetsInfo assetsInfo : classList) {
                Map<String, Object> map = new HashMap<>();
                int emergent = 0;
                int indeterminacy = 0;
                int count = 0;
                map.put("name", assetsInfo.getClassification());
                List<AssetsInfo> assetsInfoList = assetsService.getList(assetsInfo);
                for (int i = 0; i < assetsInfoList.size(); i++) {
                    AssetsCorrelation assetsCorrelation = new AssetsCorrelation();
                    assetsCorrelation.setParentCode(assetsInfoList.get(i).getCode());
                    assetsCorrelation.setCorrelationTypeCode("0102");
                    List<AssetsCorrelation> assetsCorrelationList = assetsCorrelationService.getCorrelation(assetsCorrelation);
                    if (assetsCorrelationList != null && !assetsCorrelationList.isEmpty()){
                        List<String> correlationCode = new ArrayList<>();
                        assetsCorrelationList.forEach( correlation ->{
                            correlationCode.add(correlation.getCorrelationCode());
                        });
                        assetsInfoList.get(i).setAssetsCorrelation(correlationCode);
                    }
                    AssetsAlertCur param = new AssetsAlertCur();
                    param.setAssetsCode(assetsInfoList.get(i).getCode());
                    List<AssetsAlertCur> curList = assetsAlertCurService.getList(param);
                    if (!curList.isEmpty()) {
                        assetsInfoList.get(i).setRoomCode("1");
                        int num = 0;
                        for (AssetsAlertCur cur : curList) {
                            if (cur.getGrade().equals(AssetsAlertStatus.Critical.getCode())) {
                                num++;
                            }
                        }
                        ;
                        if (num != 0) {
                            assetsInfoList.get(i).setRoomCode("4");
                            emergent++;
                        }
                    } else {
                        assetsInfoList.get(i).setRoomCode("0");
                    }
                }
                map.put("emergent", emergent);
                map.put("indeterminacy", indeterminacy);
                map.put("count", 0);
                map.put("list", assetsInfoList);
                applicationList.add(map);
            }
        }
        return applicationList;
    }

    @Override
    public Map<String, Object> getCur(String code) {
        AssetsInfo queryParam = new AssetsInfo();
        queryParam.setCode(code);
        List<AssetsInfo> list = assetsService.getList(queryParam);
        Map<String, Object> map = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            list.forEach(assetsInfo -> {
                map.put("name", assetsInfo.getName());
                map.put("dept", assetsInfo.getDeptName());
                map.put("vendorName", assetsInfo.getOpsVendorName());
                map.put("personnelName", assetsInfo.getOpsPersonnelName());
                AssetsAlertCur param = new AssetsAlertCur();
                param.setAssetsCode(assetsInfo.getCode());
                List<AssetsAlertCur> curList = assetsAlertCurService.getList(param);
                if (curList != null && !curList.isEmpty()) {
                    curList.forEach(cur -> {
                        map.put("abnormal", true);
                        map.put("title", cur.getTitle());
                    });

                } else {
                    map.put("abnormal", false);
                }
            });
        }
        return map;
    }

    @Override
    public List<AssetsAlertCur> getMonitoringCur(String code, String keyword) {
        AssetsAlertCur param = new AssetsAlertCur();
        param.setAssetsType(code);
        return assetsAlertCurService.getList(param);
    }

    @Override
    public JSONObject getAvgByAll(String label, List<String> identList, LocalDateTime dateTime) {
        if (null == dateTime) {
            dateTime = LocalDateTime.now();
        }
        //当前平均使用率
        BigDecimal currentAvg = prometheusService.getAllAvg(label, identList, dateTime);
        //获取昨天同时间的平均使用率
        dateTime = dateTime.minusDays(1);
        BigDecimal yesterdayAvg = prometheusService.getAllAvg(label, identList, dateTime);
        //计算环比
        BigDecimal QoQ = BigDecimal.ZERO;
        if (yesterdayAvg.compareTo(BigDecimal.ZERO) > 0) {
            QoQ = currentAvg.subtract(yesterdayAvg).divide(yesterdayAvg, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        }
        JSONObject resultObject = new JSONObject();
        resultObject.set("currentAvg", currentAvg);
        resultObject.set("yesterdayAvg", yesterdayAvg);
        resultObject.set("QoQ", QoQ);
        return resultObject;
    }


    /**
     * 获取指定时间区间的最大峰值平均利用率以及最大峰值
     *
     * @param label     指标
     * @param identList 实列列表
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @return 结果：最大峰值以及环比/平均最大峰值以及环比/最大峰值的ident
     */
    @Override
    public JSONObject getMaxAvgByAll(String label, List<String> identList, Integer step, LocalDateTime startTime, LocalDateTime endTime) {
        PrometheusQueryPayload payload = new PrometheusQueryPayload();
        if (startTime == null) {
            startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        if (null == step) {
            step = 60;
        }
        JSONObject currentResult = prometheusService.getMaxMinAvg(label, identList, startTime, endTime, step);
        LocalDateTime yesterdayStartTime = startTime.minusDays(1);
        LocalDateTime yesterdayEndTime = endTime.minusDays(1);
        JSONObject yesterdayResult = prometheusService.getMaxMinAvg(label, identList, yesterdayStartTime, yesterdayEndTime, step);
        BigDecimal currentAvg = currentResult.getBigDecimal("avg");
        BigDecimal yesterdayAvg = yesterdayResult.getBigDecimal("avg");
        BigDecimal currentMax = currentResult.getBigDecimal("max");
        BigDecimal yesterdayMax = yesterdayResult.getBigDecimal("max");
        BigDecimal avgQoQ = BigDecimal.ZERO;
        BigDecimal maxQoQ = BigDecimal.ZERO;
        if (yesterdayAvg.compareTo(BigDecimal.ZERO) > 0) {
            avgQoQ = currentAvg.subtract(yesterdayAvg).divide(yesterdayAvg, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        }
        if (yesterdayMax.compareTo(BigDecimal.ZERO) > 0) {
            maxQoQ = currentMax.subtract(yesterdayMax).divide(yesterdayMax, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        }
        JSONObject result = new JSONObject();
        result.set("avg", currentAvg);
        result.set("max", currentMax);
        result.set("avgQoQ", avgQoQ);
        result.set("maxQoQ", maxQoQ);
        result.set("maxIdent", currentResult.getStr("maxIdent"));
        return result;
    }


    @Override
    public List<JSONObject> getRangeByIdent(String label,List<String> identList, Integer step,LocalDateTime startTime, LocalDateTime endTime) {
        return prometheusService.getRangeByIdent(label,identList,startTime,endTime,step);
    }

}
