package com.inspur.agriculture.input.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 需求确认日志实体类
 *
 * @author igdp
 */
@Data
@TableName("agri_demand_confirmation")
public class AgriDemandConfirmation implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 确认ID */
    @TableId
    private String confirmationId;

    /** 发送方（DA/Coop/Union/District/Zone/Region） */
    private String fromActor;

    /** 接收方 */
    private String toActor;

    /** 关联需求记录ID */
    private String referenceId;

    /** 确认类型(Receipt-接收确认/Delivery-发送确认) */
    private String confirmationType;

    /** 确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date confirmedTime;

    /** 创建人 */
    private String createPeople;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    private String updatePeople;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 删除标识（0-正常/2-删除） */
    private String delFlag;
}