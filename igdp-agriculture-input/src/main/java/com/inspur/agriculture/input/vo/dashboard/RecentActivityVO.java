package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;
import java.util.Date;

/**
 * 最新动态记录VO
 *
 * @author igdp
 */
@Data
public class RecentActivityVO {

    /** 记录ID */
    private String id;

    /** 活动类型: allocation/distribution/receive */
    private String activityType;

    /** 活动类型描述 */
    private String activityTypeDesc;

    /** 投入品类型 */
    private String inputType;

    /** 投入品名称 */
    private String inputName;

    /** 数量 */
    private String quantity;

    /** 单位 */
    private String unit;

    /** 来源(发送方) */
    private String source;

    /** 目标(接收方) */
    private String target;

    /** 状态 */
    private String status;

    /** 状态描述 */
    private String statusDesc;

    /** 发生时间 */
    private Date activityTime;
}
