package com.inspur.workorder.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.workorder.domain.ProcessModuleTypeForm;
import com.inspur.workorder.domain.vo.ProcessVo;

import java.util.List;
import java.util.Map;

/**
 * @author liyunlong
 * @date 2024-04-12
 */
public interface IProcessModuleTypeFormService extends IService<ProcessModuleTypeForm> {


    /**
     * 获取所有类别选择列表
     * @return 类别选择列表
     * */
    List<String> getTypeList();

    /**
     * 获取流程列表
     * @param type 模块类别
     * @return 列表
     * */
    List<ProcessModuleTypeForm> getProcessList(String type);

    /**
     * 查询条件或者明细，按照module、type进行分组
     * @Author liyunlong
     * @param module 模块：事项工单、资产流程、问题流程
     * @return 集合
     */
    List<JSONObject> getProcessVoListByModule(String module);

    /**
     * 根据appId以及formId获取表单信息
     * @param appId appId
     * @param formId formId
     * @return 查询信息
     * */
    ProcessModuleTypeForm getByAppIdFormId(String appId,String formId);
}
