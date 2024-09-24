package com.inspur.ucif.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.common.constant.ApiConstants;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.system.service.ISysConfigService;
import com.inspur.system.service.ISysDeptService;
import com.inspur.system.service.ISysUserService;
import com.inspur.ucif.service.IAccountSyncStrategy;
import com.inspur.ucif.utils.GdzfwHAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 广东政法委账号体系同步
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName GdzfwAccountAsyncStrategy
 * @date 2024/5/30 13:57
 */
@Service("gdzfwAccountAsyncStrategy")
@Slf4j
public class GdzfwAccountSyncStrategy implements IAccountSyncStrategy {

    private static final String CODE_TAG = "code";
    private static final Integer CODE_SUCCESS = 200;
    private static final String DATA_TAG = "data";
    private static final String STATUS_DELETE = "0";

    @Resource
    private ISysDeptService sysDeptService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private GdzfwAuthStrategy gdzfwAuthStrategy;
    @Resource
    private ISysConfigService configService;

    private String getAncestors() {
        return configService.selectConfigByKey("gdzfw.sync.dept.ancestors");
    }

    @Override
    public void asyncDept() {
        String url = gdzfwAuthStrategy.getServer() + ApiConstants.GDZFW_SSO_DEPT_LIST;
        JSONObject params = new JSONObject();
        Long timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long uptime = LocalDateTime.of(LocalDate.of(1900, 1, 1), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String appId = gdzfwAuthStrategy.getAppId();
        String secret = gdzfwAuthStrategy.getSecret();
        params.set("appid", appId);
        params.set("deptId", "");
        params.set("uptime", uptime);
        String message = "timestamp=" + timestamp + "appid=" + appId + "deptId=" + "uptime=" + timestamp;
        params.set("uptime", uptime);
        String sign = GdzfwHAUtils.encode(secret, message);
        Map<String, String> headers = gdzfwAuthStrategy.initHeaders(sign, timestamp);
        log.info("拉取部门列表参数为：{}", params);
        log.info("拉取部门列表签名message为：{}", message);
        log.info("拉取部门列表headers为：{}", headers);
        String result = HttpRequest.post(url).addHeaders(headers).body(params.toString()).execute().body();
        log.info("拉取部门列表的请求地址：{}", url);
        log.info("拉取部门列表结果：{}", result);
        JSONObject retJo = JSONUtil.parseObj(result);
        if (retJo.getInt(CODE_TAG).equals(CODE_SUCCESS)) {
            JSONArray dataArr = retJo.getJSONArray(DATA_TAG);
            List<JSONObject> dataList = dataArr.toList(JSONObject.class);
            if (null != dataList && !dataList.isEmpty()) {
                String syncAncestors = getAncestors();
                List<SysDept> currentDeptList = sysDeptService.list();
                Map<String, SysDept> deptMap = null;
                List<SysDept> createList = new ArrayList<>();
                List<SysDept> updateList = new ArrayList<>();
                if (null != currentDeptList && !currentDeptList.isEmpty()) {
                    deptMap = currentDeptList.stream().collect(Collectors.toMap(SysDept::getDeptId, dept -> dept));
                    for (JSONObject data : dataList) {
                        String deptId = data.getStr("deptId");
                        SysDept dept = deptMap.get(deptId);
                        boolean isUpdate = null != dept;
                        if (null == dept) {
                            dept = new SysDept();
                        }
                        toSysDept(data, dept);
                        if (StrUtil.isNotEmpty(syncAncestors) && !dept.getAncestors().startsWith(syncAncestors)) {
                            continue;
                        }
                        if (isUpdate) {
                            dept.setUpdateTime(LocalDateTime.now());
                            updateList.add(dept);
                        } else {
                            dept.setCreateTime(LocalDateTime.now());
                            createList.add(dept);
                        }
                    }
                }
                if (!updateList.isEmpty()) {
                    sysDeptService.updateBatchById(updateList);
                }
                if (!createList.isEmpty()) {
                    sysDeptService.saveBatch(createList);
                }
            }
        }
    }

    @Override
    public void asyncUser() {
        String getUserListUrl = gdzfwAuthStrategy.getServer() + ApiConstants.GDZFW_SSO_USER_LIST;
        JSONObject params = new JSONObject();
        Long timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long uptime = LocalDateTime.of(LocalDate.of(1900, 1, 1), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String appId = gdzfwAuthStrategy.getAppId();
        String secret = gdzfwAuthStrategy.getSecret();
        params.set("appid", appId);
        params.set("uptime", uptime);
        String message = "timestamp=" + timestamp + "appid=" + appId + "uname=" + "uptime=" + uptime;
        params.set("appid", appId);
        params.set("uname", "");
        params.set("uptime", uptime);
        String sign = GdzfwHAUtils.encode(secret, message);
        Map<String, String> headers = gdzfwAuthStrategy.initHeaders(sign, timestamp);
        log.info("拉取用户列表参数为：{}", params);
        log.info("拉取用户列表headers为：{}", headers);
        String result = HttpRequest.post(getUserListUrl).addHeaders(headers).body(params.toString()).execute().body();
        log.info("拉取用户列表的请求地址：{}", getUserListUrl);
        log.info("拉取用户列表结果：{}", result);
        JSONObject retJo = JSONUtil.parseObj(result);
        if (retJo.getInt(CODE_TAG).equals(CODE_SUCCESS)) {
            JSONArray dataArr = retJo.getJSONArray(DATA_TAG);
            List<JSONObject> dataList = dataArr.toList(JSONObject.class);
            if (null != dataList && !dataList.isEmpty()) {
                List<SysUser> userList = sysUserService.list();
                Map<String, SysUser> userMap;
                List<SysUser> createList = new ArrayList<>();
                List<SysUser> updateList = new ArrayList<>();
                if (null != userList && !userList.isEmpty()) {
                    userMap = userList.stream().collect(Collectors.toMap(SysUser::getUserName, user -> user));
                    for (JSONObject data : dataList) {
                        String username = data.getStr("uname");
                        SysUser user = userMap.get(username);
                        boolean isUpdate = null != user;
                        if (null == user) {
                            user = new SysUser();
                        }
                        toSysUser(data, user);
                        if (isUpdate) {
                            user.setUpdateTime(LocalDateTime.now());
                            updateList.add(user);
                        } else {
                            user.setCreateTime(LocalDateTime.now());
                            createList.add(user);
                        }
                    }
                }
                if (!updateList.isEmpty()) {
                    sysUserService.updateBatchById(updateList);
                }
                if (!createList.isEmpty()) {
                    sysUserService.saveBatch(createList);
                }
            }
        }
    }

    @Override
    public void syncUserByUsername(String username) {
        String getUserListUrl = gdzfwAuthStrategy.getServer() + ApiConstants.GDZFW_SSO_USER;
        JSONObject params = new JSONObject();
        Long timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long uptime = LocalDateTime.of(LocalDate.of(1900, 1, 1), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String appId = gdzfwAuthStrategy.getAppId();
        String secret = gdzfwAuthStrategy.getSecret();
        params.set("appid", appId);
        params.set("uname", username);
        params.set("uptime", uptime);
        String message = "timestamp=" + timestamp + " " + "appid=" + appId + " " + "uname=" + username + " " + "uptime=" + uptime;
        String sign = GdzfwHAUtils.encode(secret, message);
        Map<String, String> headers = gdzfwAuthStrategy.initHeaders(sign, timestamp);
        log.info("拉取用户信息参数为：{}", params);
        log.info("拉取用户信息headers为：{}", headers);
        String result = HttpRequest.post(getUserListUrl).addHeaders(headers).body(params.toString()).execute().body();
        log.info("拉取用户信息结果：{}", result);
        JSONObject retJo = JSONUtil.parseObj(result);
        if (retJo.getInt(CODE_TAG).equals(CODE_SUCCESS)) {
            JSONArray dataArr = retJo.getJSONArray(DATA_TAG);
            List<JSONObject> dataList = dataArr.toList(JSONObject.class);
            if (null != dataList && !dataList.isEmpty()) {
                JSONObject data = dataList.get(0);
                SysUser user = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, username));
                boolean isUpdate = null != user;
                if (null == user) {
                    user = new SysUser();
                }
                toSysUser(data, user);
                if (isUpdate) {
                    user.setUpdateTime(LocalDateTime.now());
                    sysUserService.updateById(user);
                } else {
                    user.setCreateTime(LocalDateTime.now());
                    sysUserService.save(user);
                }
            }
        }

    }

    @Override
    public void syncDeptByDeptId(String deptId) {
        String loginUrl = gdzfwAuthStrategy.getServer() + ApiConstants.GDZFW_SSO_DEPT;
        JSONObject params = new JSONObject();
        Long timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long uptime = LocalDateTime.of(LocalDate.of(1900, 1, 1), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String appId = gdzfwAuthStrategy.getAppId();
        String secret = gdzfwAuthStrategy.getSecret();
        params.set("appid", appId);
        params.set("deptId", deptId);
        params.set("uptime", uptime);
        String message = "timestamp=" + timestamp + " " + "appid=" + appId + " " + "deptId=" + deptId + " " + "uptime=" + uptime;
        String sign = GdzfwHAUtils.encode(secret, message);
        Map<String, String> headers = gdzfwAuthStrategy.initHeaders(sign, timestamp);
        log.info("拉取部门信息参数为：{}", params);
        log.info("拉取部门信息headers为：{}", headers);
        String result = HttpRequest.post(loginUrl).addHeaders(headers).body(params.toString()).execute().body();
        log.info("拉取部门信息结果：{}", result);
        JSONObject retJo = JSONUtil.parseObj(result);
        if (retJo.getInt(CODE_TAG).equals(CODE_SUCCESS)) {
            JSONArray dataArr = retJo.getJSONArray(DATA_TAG);
            List<JSONObject> dataList = dataArr.toList(JSONObject.class);
            if (null != dataList && !dataList.isEmpty()) {
                SysDept dept = sysDeptService.getById(deptId);
                boolean isUpdate = null != dept;
                if (null == dept) {
                    dept = new SysDept();
                }
                JSONObject data = dataList.get(0);
                toSysDept(data, dept);
                if (isUpdate) {
                    dept.setUpdateTime(LocalDateTime.now());
                    sysDeptService.updateById(dept);
                } else {
                    dept.setCreateTime(LocalDateTime.now());
                    sysDeptService.save(dept);
                }
            }
        }
    }


    private void toSysDept(JSONObject data, SysDept dept) {
        String deptId = data.getStr("deptId");
        String deptName = data.getStr("deptName");
        String deptType = data.getStr("deptType");
        String parentId = data.getStr("parentId");
        Integer orderNumber = data.getInt("sort");
        //状态（1 正常 2 停用 0 删除）
        String status = data.getStr("status");
        dept.setDeptName(deptName);
        if (StrUtil.isEmpty(dept.getDeptId())) {
            dept.setDeptId(deptId);
        }
        dept.setParentId(parentId);
        dept.setOrderNum(orderNumber);
        if (StrUtil.isNotEmpty(status)) {
            if (STATUS_DELETE.equals(status)) {
                dept.setDelFlag(Constants.DELETE_FLAG_INVALID);
            } else {
                dept.setDelFlag(Constants.DELETE_FLAG_VALID);
                dept.setStatus(String.valueOf(Integer.parseInt(status) - 1));
            }
        }
    }


    private void toSysUser(JSONObject data, SysUser user) {
        String userId = data.getStr("userId");
        String username = data.getStr("uname");
        String nickname = data.getStr("cname");
        String deptId = data.getStr("deptId");
        String orgId = data.getStr("orgId");
        String orgName = data.getStr("orgName");
        String email = data.getStr("email");
        String phone = data.getStr("phone");
        String sex = data.getStr("sex");
        //状态（1 正常 2 停用 0 删除）
        String status = data.getStr("status");
        Map<String, Integer> sortCodeMap = (Map<String, Integer>) data.get("sortCode");
        if (null != sortCodeMap) {
            Integer sortNumber = sortCodeMap.get(deptId);
            user.setSortNumber(sortNumber);
        }
        if (StrUtil.isEmpty(user.getUserId())) {
            user.setUserId(userId);
        }
        user.setDeptId(deptId);
        user.setUserName(username);
        user.setNickName(nickname);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setSex(sex);
        if (StrUtil.isNotEmpty(status)) {
            if (STATUS_DELETE.equals(status)) {
                user.setDelFlag(Constants.DELETE_FLAG_INVALID);
            } else {
                user.setDelFlag(Constants.DELETE_FLAG_VALID);
                user.setStatus(String.valueOf(Integer.parseInt(status) - 1));
            }
        }
        user.setAllowedShow("0");
    }


}
