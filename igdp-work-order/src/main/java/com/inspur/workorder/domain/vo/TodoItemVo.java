package com.inspur.workorder.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName TodoItemVo
 * @date 2024/7/2 15:43
 */
@Data
public class TodoItemVo {
    private String businessCode;

    private String title;

    private String type;
    private String state;
    private String userId;
    private String nickName;
    private String deptId;
    private String deptName;
    private LocalDateTime submitTime;
    private String callbackLink;
}
