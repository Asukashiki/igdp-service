package com.inspur.workorder.domain.payload;

import lombok.Data;

/**
 * 待办事项明细接收参数载体
 * @author liyunlong02
 * @version 1.0
 * @ClassName TodoItemPayload
 * @date 2024/4/16 11:03
 */
@Data
public class TodoItemDetailPayload {

    private String detailId;

    private String dataId;

    private String recipientUserId;

    private String businessId;

    private String businessCode;

    /**
     * 状态
     * 0新增；1已处理
     * */
    private String status;

}
