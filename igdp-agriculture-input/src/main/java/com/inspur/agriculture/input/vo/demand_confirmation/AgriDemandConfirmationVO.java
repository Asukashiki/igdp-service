package com.inspur.agriculture.input.vo.demand_confirmation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 需求确认日志 VO
 *
 * @author igdp
 */
@Data
public class AgriDemandConfirmationVO {

    /**
     * 确认ID
     */
    private String confirmationId;

    /**
     * 发送方（DA/Coop/Union/District/Zone/Region）
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
     * 确认类型(Receipt-接收确认/Delivery-发送确认)
     */
    private String confirmationType;

    /**
     * 确认类型描述
     */
    private String confirmationTypeDesc;

    /**
     * 确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date confirmedTime;

    /**
     * 创建人
     */
    private String createPeople;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}