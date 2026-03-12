package com.inspur.agriculture.input.dto.demand_confirmation;

import lombok.Data;

/**
 * 需求确认日志查询 DTO
 *
 * @author igdp
 */
@Data
public class AgriDemandConfirmationQueryDTO {

    /**
     * 发送方
     */
    private String fromActor;

    /**
     * 接收方
     */
    private String toActor;

    /**
     * 关联需求记录ID
     */
    private String referenceId;

    /**
     * 确认类型
     */
    private String confirmationType;

    /**
     * 页码
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}