package com.inspur.agriculture.input.dto.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 反馈查询DTO
 *
 * @author igdp
 */
@Data
public class FeedbackQueryDTO {

    /** 反馈编号 */
    private String feedbackNo;

    /** 反馈类型 */
    private String feedbackType;

    /** 状态 */
    private String status;

    /** 农资ID */
    private Integer inputId;

    /** 供应商ID */
    private Integer supplierId;

    /** 处理人ID */
    private String handlerId;

    /** 标题关键字 */
    private String titleKeyword;

    /** 内容关键字 */
    private String contentKeyword;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 联系人 */
    private String contactName;

    /** 联系电话 */
    private String contactPhone;
}
