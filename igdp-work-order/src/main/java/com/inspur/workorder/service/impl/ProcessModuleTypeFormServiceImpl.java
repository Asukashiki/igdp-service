package com.inspur.workorder.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.annotation.DataSource;
import com.inspur.common.enums.DataSourceType;
import com.inspur.workorder.domain.ProcessModuleTypeForm;
import com.inspur.workorder.domain.vo.ProcessVo;
import com.inspur.workorder.mapper.ProcessModuleTypeFormMapper;
import com.inspur.workorder.service.IProcessModuleTypeFormService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程表单与模块类型的关联关系service
 *
 * @author liyunlong
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class ProcessModuleTypeFormServiceImpl extends ServiceImpl<ProcessModuleTypeFormMapper, ProcessModuleTypeForm> implements IProcessModuleTypeFormService {
    @Override
    public List<String> getTypeList() {
        LambdaQueryWrapper<ProcessModuleTypeForm> queryWrapper = new QueryWrapper<ProcessModuleTypeForm>()
                .select("DISTINCT process_type").lambda();
        List<String> typeList = listObjs(queryWrapper);
        if (null != typeList && !typeList.isEmpty()) {
            List<String> result = new ArrayList<>(typeList.size());
            for (String str : typeList) {
                String type = str.replace("\"", "").replace("[", "").replace("]", "");
                result.add(type);
            }
            return result;
        }
        return null;
    }

    @Override
    public List<ProcessModuleTypeForm> getProcessList(String type) {
        LambdaQueryWrapper<ProcessModuleTypeForm> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(type)){
            StringBuilder typeStr = new StringBuilder("[")
                    .append("\"").append(type).append("\"").append("]");
            queryWrapper.eq(ProcessModuleTypeForm::getProcessType,type).or().eq(ProcessModuleTypeForm::getProcessType,typeStr.toString());
        }
        return list(queryWrapper);
    }

    @Override
    public List<JSONObject> getProcessVoListByModule(String module) {
        List<ProcessModuleTypeForm> allList = list();
        List<JSONObject> resultList = new ArrayList<>();
        if (null != allList && !allList.isEmpty()) {
            List<ProcessVo> processVoList = new ArrayList<>(allList.size());
            for (ProcessModuleTypeForm processModuleTypeForm : allList) {
                ProcessVo processVo = ProcessVo.toProcessVo(processModuleTypeForm);
                processVoList.add(processVo);
            }
            Map<String, List<ProcessVo>> groupByModule = processVoList.stream().collect(Collectors.groupingBy(ProcessVo::getProcessModule));
            List<ProcessVo> processVos = groupByModule.get(module);
            if (null != processVos && !processVos.isEmpty()) {
                Map<String, List<ProcessVo>> groupByType = processVos.stream().collect(Collectors.groupingBy(ProcessVo::getProcessType));
                Set<String> typeSet = groupByType.keySet();
                for (String type : typeSet) {
                    List<ProcessVo> voList = groupByType.get(type);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.set("processType", type);
                    jsonObject.set("processList", voList);
                    resultList.add(jsonObject);
                }
            }
        }
        return resultList;
    }

    @Override
    public ProcessModuleTypeForm getByAppIdFormId(String appId, String formId) {
        LambdaQueryWrapper<ProcessModuleTypeForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProcessModuleTypeForm::getApplicationId, appId);
        queryWrapper.eq(ProcessModuleTypeForm::getProcessFormId, formId);
        return getOne(queryWrapper);
    }
}
