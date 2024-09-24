package com.inspur.workorder.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderDetailVo
 * @date 2024/5/6 14:06
 */
@Data
public class WorkOrderDetailVo {
    private String detailId;

    private String workOrderCode;

    /**
     * 工单类型
     * maintenance 维修工单；
     */
    private String workOrderType;

    /**
     * 处理人用户id
     */
    private String userId;

    /**
     * 处理类型
     * handle 处理；audit 审核
     * */
    private String operationType;


    /**
     * 0待触发；1待处理；2已处理
     * 流程到达该节点则更新为待处理
     */
    private String state;

    /**
     * 处理结果
     * 0未完成；1已完成
     * */
    private String handleState;

    /**
     * 审核结果
     * 0未通过；1通过
     * */
    private String auditState;

    private LocalDateTime handleTime;

    private String workOrderTitle;

    private String workOrderContent;

    private String address;

}
