package com.inspur.workorder.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.constant.ApiConstants;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.utils.LoginHelper;
import com.inspur.system.service.ISysConfigService;
import com.inspur.system.service.ISysUserService;
import com.inspur.workorder.domain.ProcessScheduledTask;
import com.inspur.workorder.mapper.ProcessScheduledTaskMapper;
import com.inspur.workorder.service.IProcessScheduledTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ProcessScheduledTaskServiceImpl
 * @date 2024/6/4 16:45
 */
@Service("processScheduledTaskService")
@Slf4j
public class ProcessScheduledTaskServiceImpl extends ServiceImpl<ProcessScheduledTaskMapper, ProcessScheduledTask> implements IProcessScheduledTaskService {


    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysConfigService configService;


    @Override
    public List<ProcessScheduledTask> selectList(ProcessScheduledTask queryParam) {
        LambdaQueryWrapper<ProcessScheduledTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getTaskId()), ProcessScheduledTask::getTaskId, queryParam.getTaskId());
        queryWrapper.like(StrUtil.isNotEmpty(queryParam.getTaskName()), ProcessScheduledTask::getTaskName, queryParam.getTaskName());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getIcdAppId()), ProcessScheduledTask::getIcdAppId, queryParam.getIcdAppId());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getIcdFormId()), ProcessScheduledTask::getIcdFormId, queryParam.getIcdFormId());
        Map<String, Object> params = queryParam.getParams();
        if (null != params) {
            queryWrapper.ge(ObjectUtil.isNotEmpty(params.get("beginTime")), ProcessScheduledTask::getCreateTime, params.get("beginTime"));
            queryWrapper.le(ObjectUtil.isNotEmpty(params.get("endTime")), ProcessScheduledTask::getCreateTime, params.get("endTime"));
        }
        queryWrapper.orderByDesc(ProcessScheduledTask::getCreateTime);
        List<ProcessScheduledTask> taskList = list(queryWrapper);
        if (null != taskList && !taskList.isEmpty()) {
            for (ProcessScheduledTask task : taskList) {
                handleTaskApprove(task);
                SysUser user = sysUserService.getById(task.getInitiatorUserId());
                if (null != user) {
                    task.setInitiatorName(user.getNickName());
                }
            }
        }
        return taskList;
    }

    @Override
    public ProcessScheduledTask getTaskById(String taskId) {
        ProcessScheduledTask task = getById(taskId);
        if (null != task) {
            handleTaskApprove(task);
        }
        return task;
    }

    private void handleTaskApprove(ProcessScheduledTask task) {
        if (StrUtil.isNotEmpty(task.getApproveNodeUsers())) {
            List<JSONObject> approveNodeUsers = JSONUtil.parseArray(task.getApproveNodeUsers()).toList(JSONObject.class);
            if (null != approveNodeUsers && !approveNodeUsers.isEmpty()) {
                List<String> approveNodeUserIds = new ArrayList<>();
                StringBuilder approveNodeUserNames = new StringBuilder();
                for (JSONObject jsonObject : approveNodeUsers) {
                    approveNodeUserIds.add(jsonObject.getStr("id"));
                    if (approveNodeUserNames.length() > 0) {
                        approveNodeUserNames.append(",");
                    }
                    approveNodeUserNames.append(jsonObject.getStr("name"));
                }
                task.setApproveNodeUserIds(approveNodeUserIds);
                task.setApproveNodeUserName(approveNodeUserNames.toString());
            }
        }
    }

    @Override
    public boolean addTask(ProcessScheduledTask processScheduledTask) {
        processScheduledTask.setCreateTime(LocalDateTime.now());
        processScheduledTask.setCreateBy(LoginHelper.getUsername());
        processScheduledTask.setUserId(LoginHelper.getUserId());
        processScheduledTask.setDeptId(LoginHelper.getDeptId());

        handleApproveNodeUserForSave(processScheduledTask);
        return save(processScheduledTask);
    }

    @Override
    public boolean updateTask(ProcessScheduledTask processScheduledTask) {
        ProcessScheduledTask currentTask = this.getById(processScheduledTask.getTaskId());
        if (null != currentTask) {
            processScheduledTask.setCreateBy(currentTask.getCreateBy());
            processScheduledTask.setCreateTime(currentTask.getCreateTime());
            processScheduledTask.setUpdateTime(LocalDateTime.now());
            processScheduledTask.setUpdateBy(LoginHelper.getUsername());
            handleApproveNodeUserForSave(processScheduledTask);
            return updateById(processScheduledTask);
        }
        return false;
    }

    private void handleApproveNodeUserForSave(ProcessScheduledTask task) {
        if (null != task.getApproveNodeUserIds() && !task.getApproveNodeUserIds().isEmpty()) {
            List<SysUser> userList = sysUserService.list(new LambdaQueryWrapper<SysUser>().in(SysUser::getUserId, task.getApproveNodeUserIds()));
            if (null != userList && !userList.isEmpty()) {
                List<JSONObject> approveNodeUsers = new ArrayList<>();
                for (SysUser user : userList) {
                    JSONObject approveNodeUser = new JSONObject();
                    approveNodeUser.set("id", user.getUserId());
                    approveNodeUser.set("name", user.getNickName());
                    approveNodeUsers.add(approveNodeUser);
                }
                task.setApproveNodeUsers(approveNodeUsers.toString());
            }
        }
    }

    @Override
    public void startTask(String taskId) {

        ProcessScheduledTask processScheduledTask = this.getById(taskId);
        if (null != processScheduledTask) {
            String icdServer = configService.selectConfigByKey("icd.service.uri");
            if(StrUtil.isNotEmpty(icdServer)){
                throw new RuntimeException("获取icd的服务地址为空");
            }
            String url = icdServer + ApiConstants.ICD_WORKFLOW_SUB_SUBMIT_INSTANCE;
            SysUser user = sysUserService.selectUserById(processScheduledTask.getInitiatorUserId());
            if (null == user) {
                throw new RuntimeException("发起人信息为空");
            }
            JSONObject params = getParams(user, processScheduledTask);
            log.info("发起流程参数为：{}", params);
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Content-Type", "application/json");
            String result = HttpRequest.post(url).addHeaders(headers).body(params.toString()).execute().body();
            log.info("发起流程响应内容：{}", result);
        }
    }

    private static JSONObject getParams(SysUser user, ProcessScheduledTask processScheduledTask) {
        JSONObject params = new JSONObject();
        JSONObject userInfo = getUserInfo(user);
        params.set("name", processScheduledTask.getTaskName());
        params.set("applicationId", processScheduledTask.getIcdAppId());
        params.set("formDataId", "");
        params.set("formId", processScheduledTask.getIcdFormId());
        params.set("versionId", processScheduledTask.getIcdVersionId());
        params.set("userInfo", userInfo);
        params.set("creator", user.getUserId());
        params.set("creatorName", user.getNickName());
        List<JSONObject> approveLit = JSONUtil.parseArray(processScheduledTask.getApproveNodeUsers()).toList(JSONObject.class);
        JSONObject selectApproveNode = new JSONObject();
        selectApproveNode.set("approverList", approveLit);
        List<JSONObject> approveNodeList = new ArrayList<>();
        approveNodeList.add(selectApproveNode);
        //组装表单数据
        JSONObject formData = new JSONObject();
        formData.set("pub_sjmc",processScheduledTask.getTaskName() + LocalDate.now());
        params.set("formData",formData);
        params.set("taskDefinitionKey", "");
        params.set("selectApproverNodeList", approveNodeList);
        return params;
    }

    private static JSONObject getUserInfo(SysUser user) {
        JSONObject userInfo = new JSONObject();
        userInfo.set("organName", null != user.getDept() ? user.getDept().getDeptName() : "");
        userInfo.set("regionCode", "000000000000");
        userInfo.set("roles", "000000000000");
        userInfo.set("mobile", user.getPhoneNumber());
        userInfo.set("organCode", user.getDeptId());
        userInfo.set("sysParentOrganCodes", null != user.getDept() ? user.getDept().getAncestors() : "");
        userInfo.set("userId", user.getUserId());
        userInfo.set("email", user.getEmail());
        userInfo.set("name", user.getUserName());
        return userInfo;
    }
}
