package com.inspur.data.treating.task;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.inspur.common.config.SsoConfig;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.model.SsoInfo;
import com.inspur.common.core.redis.RedisCache;
import com.inspur.common.enums.PeriodType;
import com.inspur.data.treating.domain.payload.ApplicationDataPayload;
import com.inspur.data.treating.enums.DataType;
import com.inspur.data.treating.service.IAssetsApplicationDataService;
import com.inspur.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成都退役相关的应用系统数据同步任务
 *
 * @author liyunlong
 * @version 1.0
 * @date 2024/9/5 14:18
 */
@Service("cdtyApplicationDataTask")
@Slf4j
public class CdtyApplicationDataTask {


    @Resource
    private IAssetsApplicationDataService assetsApplicationDataService;
    @Resource
    private ISysConfigService configService;
    @Resource
    private SsoConfig ssoConfig;
    @Resource
    private RedisCache redisCache;

    private static final String DEFAULT_GRANT_TYPE = "chengDuTuiYi";

    private static final Integer SUCCESS_CODE = 0;
    private static final String RESPONSE_CODE = "status";
    private static final String RESPONSE_DATA = "data";
    private static final String RESPONSE_MSG = "msg";


    /**
     * 获取成都退役应用系统数据接口地址
     */
    public String getServer() {
        String server = configService.selectConfigByKey("cdty.application.data.server");
        if (StrUtil.isEmpty(server)) {
            throw new RuntimeException("成都退役应用系统数据服务地址为空");
        }
        return server;
    }

    /**
     * 获取客户端accessToken
     */
    public String getAccessToken() {
        String accessToken = null;
        String key = "cdty:client:access_token";
        boolean hasKey = redisCache.hasKey(key);
        if (hasKey) {
            accessToken = redisCache.getCacheObject(key);
        } else {
            SsoInfo ssoInfo = ssoConfig.getSsoInfo(DEFAULT_GRANT_TYPE);
            if (null == ssoInfo) {
                log.error("获取成都退役的ssoInfo失败");
            } else {
                String api = configService.selectConfigByKey("cdty.oauth.api.token");
                api = StrUtil.isNotEmpty(api) ? StrUtil.trim(api) : "/gw-ma/oauth/token?grant_type=client_credentials";
                String tokenUrl = getServer() + api;
                String authorization = "Basic " + org.apache.commons.codec.binary.Base64.encodeBase64String((ssoInfo.getClientId() + ":" + ssoInfo.getClientSecret()).getBytes(StandardCharsets.UTF_8));
                Map<String, String> headers = new HashMap<>(1);
                headers.put("Content-Type", "application/json;charset=UTF-8");
                headers.put("Authorization", authorization);
                String result = HttpRequest.post(tokenUrl).addHeaders(headers).execute().body();
                log.info("成都退役获取client的AccessToken结果：{}", result);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject resultObject = JSONUtil.parseObj(result);
                    accessToken = "Bearer " + resultObject.getStr("access_token");
                    Long expiresIn = resultObject.getLong("expires_in");
                    redisCache.setCacheObject(key, accessToken);
                    //提前十分钟过期，获取新的token
                    redisCache.expire(key, expiresIn - 600);
                }
            }
        }
        if (StringUtils.isEmpty(accessToken)) {
            throw new RuntimeException("成都退役获取系统数据服务accessToken为空");
        }
        log.info("获取应用数据的accessToken:{}", accessToken);
        return accessToken;
    }


    /**
     * 同步当前在线人数
     * 每天一条数据，每小时更新一次到assets_application_data中
     */
    public void syncOnlineCount() {
        Integer count = getOnlineCountFromServer();
        if (count >= 0) {
            ApplicationDataPayload dataPayload = new ApplicationDataPayload();
            dataPayload.setTarget(DataType.NUMBER_ONLINE_USERS.getCode());
            dataPayload.setDate(LocalDate.now().toString());
            dataPayload.setValue(String.valueOf(count));
            dataPayload.setPeriod(PeriodType.DAY.getCode());
            dataPayload.setDataType(DataType.NUMBER_ONLINE_USERS.getCode());
            assetsApplicationDataService.saveApplicationData(dataPayload);
        }
    }

    public Integer getOnlineCountFromServer() {
        String api = configService.selectConfigByKey("cdty.application.api.onlineCount");
        api = StrUtil.isNotEmpty(api) ? StrUtil.trim(api) : "/api/umc/sysinfo/getOnlineCount";
        String server = getServer();
        String url = server + api;
        String token = getAccessToken();
        Map<String, String> headers = initHeaders(token);
        log.info("成都退役获取应用系统请求地址：{},请求头：{}", url, headers);
        String result = HttpRequest.get(url).addHeaders(headers).execute().body();
        log.info("成都退役获取应用系统在线人数结果：{}", result);
        JSONObject resultObject = JSONUtil.parseObj(result);
        int status = resultObject.getInt(RESPONSE_CODE);
        if (status == SUCCESS_CODE) {
            return resultObject.getInt(RESPONSE_DATA);
        } else {
            return -1;
        }
    }


    /**
     * 同步新注册用户数
     * 同步当天数据
     */
    public void syncRegisterCount() {
        Long startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toEpochSecond(ZoneOffset.of("+8"));
        Long endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        Integer count = getRegisterCountFromServer(startTime, endTime);
        if (count >= 0) {
            ApplicationDataPayload dataPayload = new ApplicationDataPayload();
            dataPayload.setTarget(DataType.REGISTER_USER_NUM.getCode());
            dataPayload.setDate(LocalDate.now().toString());
            dataPayload.setValue(String.valueOf(count));
            dataPayload.setPeriod(PeriodType.DAY.getCode());
            dataPayload.setDataType(DataType.REGISTER_USER_NUM.getCode());
            assetsApplicationDataService.saveApplicationData(dataPayload);
        }
    }

    /**
     * 根据时间段获取新注册用户数
     */
    public Integer getRegisterCountFromServer(Long startTime, Long endTime) {
        String api = configService.selectConfigByKey("cdty.application.api.registerUserCount");
        api = StrUtil.isNotEmpty(api) ? StrUtil.trim(api) : "/api/umc/sysinfo/registerUserCount";
        String server = getServer();
        String url = server + api;
        String token = getAccessToken();
        Map<String, String> headers = initHeaders(token);
        log.info("成都退役获取应用系统请求地址：{},请求头：{}", url, headers);
        JSONObject paramBody = new JSONObject();
        paramBody.set("startTime", startTime);
        paramBody.set("endTime", endTime);
        String result = HttpRequest.get(url).body(paramBody.toString()).addHeaders(headers).execute().body();
        log.info("成都退役获取应用系统注册人数结果：{}", result);
        JSONObject resultObject = JSONUtil.parseObj(result);
        int status = resultObject.getInt(RESPONSE_CODE);
        if (status == SUCCESS_CODE) {
            return resultObject.getInt(RESPONSE_DATA);
        } else {
            return -1;
        }
    }

    /**
     * 同步登录次数数据
     */
    public void syncLoginNumber() {
        Long startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long endTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Integer count = getRegisterCountFromServer(startTime, endTime);
        if (count >= 0) {
            ApplicationDataPayload dataPayload = new ApplicationDataPayload();
            dataPayload.setTarget(DataType.LOGIN_NUM.getCode());
            dataPayload.setDate(LocalDate.now().toString());
            dataPayload.setValue(String.valueOf(count));
            dataPayload.setPeriod(PeriodType.DAY.getCode());
            dataPayload.setDataType(DataType.LOGIN_NUM.getCode());
            assetsApplicationDataService.saveApplicationData(dataPayload);
        }
    }


    /**
     * 获取登录次数
     */
    public Integer getLoginNumber(Long startTime, Long endTime) {
        String api = configService.selectConfigByKey("cdty.application.api.queryLoginHis");
        api = StrUtil.isNotEmpty(api) ? StrUtil.trim(api) : "/api/auditlog/cdInterface/queryLoginHis";
        String server = getServer();
        String url = server + api;
        String token = getAccessToken();
        Map<String, String> headers = initHeaders(token);
        log.info("成都退役获取应用系统请求地址：{},请求头：{}", url, headers);
        JSONObject paramBody = new JSONObject();
        paramBody.set("startTime", startTime);
        paramBody.set("endTime", endTime);
        String result = HttpRequest.get(url).body(paramBody.toString()).addHeaders(headers).execute().body();
        log.info("成都退役获取应用系统登录数量结果：{}", result);
        JSONObject resultObject = JSONUtil.parseObj(result);
        int status = resultObject.getInt(RESPONSE_CODE);
        if (status == SUCCESS_CODE) {
            return resultObject.getInt(RESPONSE_DATA);
        } else {
            return -1;
        }
    }


    /**
     * 同步不同系统的访问量
     */
    public void syncSysVisitCount() {
        Long startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long endTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        List<JSONObject> dataList = countBySys(startTime, endTime);
        if (null != dataList && !dataList.isEmpty()) {
            for (JSONObject data : dataList) {
                ApplicationDataPayload visitPayload = new ApplicationDataPayload();
                visitPayload.setTarget(data.getStr("name"));
                visitPayload.setDate(LocalDate.now().toString());
                visitPayload.setValue(data.getStr("number"));
                visitPayload.setPeriod(PeriodType.DAY.getCode());
                visitPayload.setDataType(DataType.VISIT_NUM.getCode());
                assetsApplicationDataService.saveApplicationData(visitPayload);
                ApplicationDataPayload errorVisitPayload = new ApplicationDataPayload();
                errorVisitPayload.setTarget(data.getStr("name"));
                errorVisitPayload.setDate(LocalDate.now().toString());
                errorVisitPayload.setValue(data.getStr("errorc"));
                errorVisitPayload.setPeriod(PeriodType.DAY.getCode());
                errorVisitPayload.setDataType(DataType.VISIT_NUM_ERROR.getCode());
                assetsApplicationDataService.saveApplicationData(errorVisitPayload);
            }
        }
    }


    /**
     * 获取各业务系统的登录次数
     */
    public List<JSONObject> countBySys(Long startTime, Long endTime) {
        String api = configService.selectConfigByKey("cdty.application.api.countBySysForCD");
        api = StrUtil.isNotEmpty(api) ? StrUtil.trim(api) : "/api/auditlog/cdInterface/statistics/countBySysForCD";
        String server = getServer();
        String url = server + api;
        String token = getAccessToken();
        Map<String, String> headers = initHeaders(token);
        log.info("成都退役获取应用系统请求地址：{},请求头：{}", url, headers);
        JSONObject paramBody = new JSONObject();
        paramBody.set("startTime", startTime);
        paramBody.set("endTime", endTime);
        String result = HttpRequest.get(url).body(paramBody.toString()).addHeaders(headers).execute().body();
        log.info("成都退役获取不同应用系统登录数量结果：{}", result);
        JSONObject resultObject = JSONUtil.parseObj(result);
        int status = resultObject.getInt(RESPONSE_CODE);
        if (status == SUCCESS_CODE) {
            return resultObject.getJSONArray(RESPONSE_DATA).toList(JSONObject.class);
        } else {
            return null;
        }
    }

    private Map<String, String> initHeaders(String token) {
        //根据token获取用户信息
        if (!token.startsWith(Constants.TOKEN_PREFIX)) {
            token = Constants.TOKEN_PREFIX + token;
        }
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Content-Type", "application/json;charset=UTF-8");
        if (StringUtils.isNotEmpty(token)) {
            headers.put("Authorization", token);
        }
        return headers;
    }
}
