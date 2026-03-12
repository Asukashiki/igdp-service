package com.inspur.agriculture.input.dto.demand_confirmation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 需求确认日志 DTO
 *
 * @author igdp
 */
@Data
public class AgriDemandConfirmationDTO {

    /**
     * 确认ID
     */
    private String confirmationId;

    /**
     * 发送方（DA/Coop/Union/District/Zone/Region）
     */
    @NotBlank(message = "From actor cannot be empty")
    private String fromActor;

    /**
     * 接收方
     */
    @NotBlank(message = "To actor cannot be empty")
    private String toActor;

    /**
     * 关联需求记录ID
     */
    @NotBlank(message = "Reference ID cannot be empty")
    private String referenceId;

    /**
     * 确认类型(Receipt-接收确认/Delivery-发送确认)
     */
    @NotBlank(message = "Confirmation type cannot be empty")
    private String confirmationType;

    /**
     * 确认时间
     */
    private Date confirmedTime;

    /**
     * 创建人
     */
    private String createPeople;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updatePeople;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标识（0-正常/2-删除）
     */
    private String delFlag;
}