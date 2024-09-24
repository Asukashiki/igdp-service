package com.inspur.workorder.controller;

import cn.hutool.json.JSONObject;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.workorder.domain.ProcessFrequentlyUsed;
import com.inspur.workorder.domain.ProcessModuleTypeForm;
import com.inspur.workorder.service.IProcessModuleTypeFormService;
import com.inspur.workorder.service.IProcessFrequentlyUsedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liyunlong02
 * @version 1.0
 * @ClassName ProcessController
 * @date 2024/4/15 11:10
 */
@RestController
@RequestMapping("/work-order/process")
public class ProcessController {
    @Resource
    private IProcessModuleTypeFormService processModuleTypeFormService;
    @Resource
    private IProcessFrequentlyUsedService processFrequentlyUsedService;

    /**
     * 获取类别列表
     */
    @GetMapping("/typeList")
    public AjaxResult listType() {
        List<String> list = processModuleTypeFormService.getTypeList();
        return AjaxResult.success(list);

    }

    @GetMapping("/processList")
    public AjaxResult listProcess(String type) {

        List<ProcessModuleTypeForm> list = processModuleTypeFormService.getProcessList(type);
        return AjaxResult.success(list);
    }

    @GetMapping("/listByModule")
    public AjaxResult getProcessListByModule(String module) {
        List<JSONObject> resultList = processModuleTypeFormService.getProcessVoListByModule(module);
        return AjaxResult.success(resultList);
    }

    /**
     * 常用流程表单
     */
    @GetMapping("/listFrequentlyUsed")
    public AjaxResult listFrequentlyUsed() {
        List<ProcessFrequentlyUsed> list = processFrequentlyUsedService.getList(null);
        return AjaxResult.success(list);
    }
}
