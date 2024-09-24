package com.inspur.workorder.domain.payload;

import lombok.Data;

import java.time.LocalDate;

/**
 * 工单处理信息载体
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderHandlePayload
 * @date 2024/4/30 11:04
 */
@Data
public class WorkOrderHandlePayload {
    private String recordId;

    private String workOrderCode;

    private String detailId;

    private String type;

    private String handleState;

    private String auditState;

    private String content;

    private LocalDate date;
}
