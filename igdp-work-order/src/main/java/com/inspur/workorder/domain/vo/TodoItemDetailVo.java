package com.inspur.workorder.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 待办工单展示内容
 * @author liyunlong
 * @version 1.0
 * @ClassName TodoItemDetailVo
 * @date 2024/5/18 10:55
 */
@Data
public class TodoItemDetailVo {
    private String todoId;

    private String businessId;

    private String businessCode;

    private String detailId;

    private String title;

    private String content;

    private LocalDateTime submitTime;

    private String detailState;

    private String callbackLink;
}
