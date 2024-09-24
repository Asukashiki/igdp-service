package com.inspur.workorder.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 流程所属模块类型
 * @author liyunlong
 * @date 2024-04-12
 * */
@TableName("process_module_type_form")
@Data
public class ProcessModuleTypeForm {
    /**
     * 所属模块类型
     * 工单流程、资产流程、问题流程。。。
     * */
    private String processModule;
    /**
     * 类型
     * 问题工单、资源申请。。。
     * */
    private String processType;
    /**
     * 名称
     * */
    private String processName;
    /**
     * 对应的appId
     * */
    private String applicationId;
    /**
     * 对应的formId
     * */
    private String processFormId;
    /**
     * 访问地址
     * */
    private String callbackLink;
}
