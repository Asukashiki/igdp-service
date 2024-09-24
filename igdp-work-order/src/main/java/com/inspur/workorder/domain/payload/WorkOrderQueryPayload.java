package com.inspur.workorder.domain.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单查询条件
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderQueryPayload
 * @date 2024/4/29 9:48
 */
@Data
public class WorkOrderQueryPayload {

    private String state;

    private String code;

    private String grade;

    private String title;

    private String userId;

    private List<String> workOrderCodes;
    /**
     * 发起时间区间
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;
    /**
     * 完成时间区间
     * */
    private LocalDateTime finishedBeginTime;
    private LocalDateTime finishedEndTime;
}
