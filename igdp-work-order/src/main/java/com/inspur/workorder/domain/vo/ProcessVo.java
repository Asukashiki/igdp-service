package com.inspur.workorder.domain.vo;

import cn.hutool.core.util.StrUtil;
import com.inspur.workorder.domain.ProcessModuleTypeForm;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 流程展示信息
 * @author liyunlong02
 * @date 2024/4/15 10:49
 * @version 1.0
 */
@Data
public class ProcessVo {
    private String processName;

    private String processType;

    private String processModule;

    private String formId;

    private String appId;

    private String callbackLink;

    private Integer sortNumber;

    public static ProcessVo toProcessVo(ProcessModuleTypeForm processModuleTypeForm) {
        ProcessVo processVo = new ProcessVo();
        processVo.setProcessName(processModuleTypeForm.getProcessName());
        processVo.setProcessModule(processModuleTypeForm.getProcessModule().replace("\"","").replace("[","").replace("]",""));
        if(StrUtil.isNotEmpty(processModuleTypeForm.getProcessType())){
            processVo.setProcessType(processModuleTypeForm.getProcessType().replace("\"","").replace("[","").replace("]",""));
        }else{
            processVo.setProcessType(processVo.getProcessModule());
        }
        processVo.setAppId(processModuleTypeForm.getApplicationId());
        processVo.setFormId(processModuleTypeForm.getProcessFormId());
        processVo.setCallbackLink(processModuleTypeForm.getCallbackLink());
        processVo.setSortNumber(processVo.getSortNumber());
        return processVo;
    }
}
