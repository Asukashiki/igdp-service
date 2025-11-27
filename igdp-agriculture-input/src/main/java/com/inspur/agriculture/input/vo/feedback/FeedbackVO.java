package com.inspur.agriculture.input.vo.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 反馈信息VO
 *
 * @author igdp
 */
@Data
public class FeedbackVO {

    /** 反馈ID */
    private Long feedbackId;

    /** 反馈编号 */
    private String feedbackNo;

    /** 反馈类型 */
    private String feedbackType;

    /** 反馈类型描述 */
    private String feedbackTypeDesc;

    /** 反馈标题 */
    private String title;

    /** 反馈内容 */
    private String content;

    /** 反馈用户ID */
    private String userId;

    /** 反馈用户名称 */
    private String userName;

    /** 反馈用户类型 */
    private String userType;

    /** 联系人 */
    private String contactName;

    /** 联系电话 */
    private String contactPhone;

    /** 联系邮箱 */
    private String contactEmail;

    /** 附件路径 */
    private String attachments;

    /** 附件列表 */
    private List<String> attachmentList;

    /** 优先级 */
    private String priority;

    /** 优先级描述 */
    private String priorityDesc;

    /** 状态 */
    private String status;

    /** 状态描述 */
    private String statusDesc;

    /** 处理人ID */
    private String handlerId;

    /** 处理人名称 */
    private String handlerName;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    /** 处理结果 */
    private String handleResult;

    /** 处理备注 */
    private String handleRemark;

    /** 满意度评分 */
    private Integer satisfaction;

    /** 评价内容 */
    private String evaluation;

    /** 评价时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date evaluationTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 创建人 */
    private String createBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 更新人 */
    private String updateBy;

    /** 处理耗时(小时) */
    private Long processingHours;

    /** 回复列表 */
    private List<FeedbackReplyVO> replies;
}
