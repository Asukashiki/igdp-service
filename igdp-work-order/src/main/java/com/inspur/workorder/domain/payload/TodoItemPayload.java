package com.inspur.workorder.domain.payload;

import lombok.Data;

import java.util.List;

/**
 * 待办事项主信息接收载体
 *
 * @author liyunlong02
 * @version 1.0
 * @ClassName TodoItemPayload
 * @date 2024/4/16 11:13
 */
@Data
public class TodoItemPayload {


    private String businessId;

    private String businessCode;

    private String appId;

    private String formId;

    private String dataId;

    private String title;

    private String content;

    private String currentStatus;

    /**
     * 发起人userId
     */
    private String initiatorUserId;
    /**
     * 发起人姓名
     */
    private transient String initiatorName;

    private String modular;

    private String source;

    /**
     * 类型：工单、资产、事项、问题。。。
     */
    private String type;

    /**
     * 回调地址
     */
    private String callbackLink;

    /**
     * 待办事项接收人明细
     */
    private List<TodoItemDetailPayload> createList;
    private List<TodoItemDetailPayload> updateList;
}
