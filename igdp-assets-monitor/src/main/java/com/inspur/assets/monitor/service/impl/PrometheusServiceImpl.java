package com.inspur.assets.monitor.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.inspur.assets.monitor.domain.payload.PrometheusQueryPayload;
import com.inspur.assets.monitor.service.IPrometheusService;
import com.inspur.common.utils.StringUtils;
import com.inspur.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName PrometheusServiceImpl
 * @date 2024/9/19 15:58
 */
@Service("prometheusService")
@Slf4j
public class PrometheusServiceImpl implements IPrometheusService {

    @Resource
    private ISysConfigService sysConfigService;

    public static final String STATUS_FLAG = "status";
    public static final String STATUS_SUCCESS = "success";
    public static final String DATA_FLAG = "data";
    public static final String RESULT_FLAG = "result";
    public static final String VALUE_FLAG = "value";
    public static final String VALUES_FLAG = "values";

    /**
     * 获取普罗的服务地址
     */
    private String getPrometheusServer() {
        String prometheusServer = sysConfigService.selectConfigByKey("prometheus.server");
        if (StringUtils.isEmpty(prometheusServer)) {
            throw new RuntimeException("Prometheus服务地址没有配置：prometheus.server");
        }
        return prometheusServer;
    }

    /**
     * 即时查询
     */
    @Override
    public JSONObject query(PrometheusQueryPayload payload) {
        String api = "/api/v1/query";
        if (StrUtil.isEmpty(payload.getQuery())) {
            throw new RuntimeException("查询指标不能为空");
        }
        HashMap<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("query", payload.getQuery());
        paramMap.put("time", payload.getTime());
        return handleRequest(api, paramMap);
    }

    /**
     * 区间查询
     */
    @Override
    public JSONObject queryRange(PrometheusQueryPayload payload) {
        String api = "/api/v1/query_range";
        if (StrUtil.isEmpty(payload.getQuery())) {
            throw new RuntimeException("查询指标不能为空");
        }
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("query", payload.getQuery());
        paramMap.put("start", payload.getStart());
        paramMap.put("end", payload.getEnd());
        paramMap.put("step", payload.getStep());
        return handleRequest(api, paramMap);
    }

    private JSONObject handleRequest(String api, HashMap<String, Object> paramMap) {
        String requestUrl = getPrometheusServer() + api;
        String response = HttpUtil.get(requestUrl, paramMap);
        log.info("Prometheus查询请求地址：{}", requestUrl);
        log.info("Prometheus查询请求结果：{}", response);
        JSONObject responseObject = JSONUtil.parseObj(response);
        if (!responseObject.getStr(STATUS_FLAG).equals(STATUS_SUCCESS)) {
            throw new RuntimeException("Prometheus即时查询结果异常");
        }
        return responseObject;
    }


    /**
     * 取所有设备平均值
     * 获取所有设备的指定时间的数据，然后取平均值
     *
     * @param label 指标内容：cpu使用率：cpu_usage_active；内存使用率：mem_used_percent
     * @return 计算结果
     */
    @Override
    public BigDecimal getAllAvg(String label, List<String> identList, LocalDateTime dateTime) {
        StringBuilder query = new StringBuilder(label);
        String indentStr = handleIdent(identList);
        if (StrUtil.isNotEmpty(indentStr)) {
            query.append(indentStr);
        }
        PrometheusQueryPayload payload = new PrometheusQueryPayload();
        payload.setQuery(query.toString());
        payload.setTime(dateTime.toEpochSecond(ZoneOffset.of("+8")));
        JSONObject resultObject = query(payload);
        JSONObject data = resultObject.getJSONObject(DATA_FLAG);
        List<JSONObject> resultList = data.getJSONArray(RESULT_FLAG).toList(JSONObject.class);
        BigDecimal total = new BigDecimal(0);
        BigDecimal avg = new BigDecimal(0);
        if (null != resultList && !resultList.isEmpty()) {
            for (JSONObject result : resultList) {
                List<Object> values = result.getJSONArray(VALUE_FLAG);
                total = total.add(new BigDecimal(values.get(1).toString()));
            }
            avg = total.divide(new BigDecimal(resultList.size()), 2, RoundingMode.HALF_UP);
        }
        return avg;
    }


    /**
     * 获取指定指标的指定时间区间内的最大峰值
     * startTime 为空，则查当天
     *
     * @param label     指标 cpu使用率：cpu_usage_active；内存使用率：mem_used_percent
     * @param identList 服务器实例列表
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param step      取数间隔，单位为秒，默认60秒
     * @return 统计结果：区间内最大值、平均最大值
     */
    @Override
    public JSONObject getMaxMinAvg(String label, List<String> identList, LocalDateTime startTime, LocalDateTime endTime, Integer step) {
        if (startTime == null) {
            startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        if (step == null) {
            step = 60;
        }
        StringBuilder query = new StringBuilder(label);
        String identStr = handleIdent(identList);
        if (StrUtil.isNotEmpty(identStr)) {
            query.append(identStr);
        }
        PrometheusQueryPayload queryPayload = new PrometheusQueryPayload();
        queryPayload.setQuery(query.toString());
        queryPayload.setStart(startTime.toEpochSecond(ZoneOffset.of("+8")));
        queryPayload.setEnd(endTime.toEpochSecond(ZoneOffset.of("+8")));
        queryPayload.setStep(step);
        JSONObject resultObject = queryRange(queryPayload);
        JSONObject data = resultObject.getJSONObject(DATA_FLAG);
        List<JSONObject> resultList = data.getJSONArray(RESULT_FLAG).toList(JSONObject.class);
        BigDecimal max = new BigDecimal(0);
        BigDecimal total = new BigDecimal(0);
        BigDecimal avg = new BigDecimal(0);
        String maxIdent = "";
        if (null != resultList && !resultList.isEmpty()) {
            //将每个ident的value中的最大值取出
            for (JSONObject result : resultList) {
                List<List> values = result.getJSONArray(VALUES_FLAG).toList(List.class);
                List<BigDecimal> valueList = values.stream().map(object -> new BigDecimal(object.get(1).toString())).collect(Collectors.toList());
                BigDecimal maxValue = valueList.stream().max(BigDecimal::compareTo).get();
                if (maxValue.compareTo(max) > 0) {
                    max = maxValue.setScale(2, RoundingMode.HALF_UP);
                    maxIdent = result.getJSONObject("metric").getStr("ident");
                }
                total = total.add(maxValue);
            }
            avg = total.divide(new BigDecimal(resultList.size()), 2, RoundingMode.HALF_UP);
        }
        JSONObject result = new JSONObject();
        result.set("max", max);
        result.set("avg", avg);
        result.set("maxIdent", maxIdent);

        return result;
    }

    @Override
    public List<JSONObject> getRangeByIdent(String label, List<String> identList, LocalDateTime startTime, LocalDateTime endTime, Integer step) {
        if (startTime == null) {
            startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        if (step == null) {
            step = 60;
        }
        StringBuilder query = new StringBuilder(label);
        String identStr = handleIdent(identList);
        if (StrUtil.isNotEmpty(identStr)) {
            query.append(identStr);
        }
        PrometheusQueryPayload queryPayload = new PrometheusQueryPayload();
        queryPayload.setQuery(query.toString());
        queryPayload.setStart(startTime.toEpochSecond(ZoneOffset.of("+8")));
        queryPayload.setEnd(endTime.toEpochSecond(ZoneOffset.of("+8")));
        queryPayload.setStep(step);
        JSONObject resultObject = queryRange(queryPayload);
        JSONObject data = resultObject.getJSONObject(DATA_FLAG);
        List<JSONObject> resultList = data.getJSONArray(RESULT_FLAG).toList(JSONObject.class);
        if (null != resultList && !resultList.isEmpty()) {
            //将每个ident的value中的最大值取出
            for (JSONObject result : resultList) {
                result.set("ident", result.getJSONObject("metric").getStr("ident"));
            }
        }
        return resultList;
    }


    /**
     * 处理实例列表
     */
    private String handleIdent(List<String> identList) {
        if (null != identList && !identList.isEmpty()) {
            StringBuilder result = new StringBuilder();
            StringBuilder identStr = new StringBuilder();
            for (String ident : identList) {
                if (identStr.length() > 0) {
                    identStr.append("|").append(ident);
                } else {
                    identStr.append(ident);
                }
            }
            result.append("{").append("ident=~").append("'").append(identStr).append("'").append("}");
            return result.toString();
        } else {
            return "";
        }
    }
}
