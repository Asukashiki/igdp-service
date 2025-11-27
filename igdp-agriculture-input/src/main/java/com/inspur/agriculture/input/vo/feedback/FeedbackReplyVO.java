package com.inspur.agriculture.input.vo.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 反馈回复VO
 *
 * @author igdp
 */
@Data
public class FeedbackReplyVO {

    /** 回复ID */
    private Long replyId;

    /** 反馈ID */
    private Long feedbackId;

    /** 回复内容 */
    private String content;

    /** 回复人ID */
    private String replyUserId;

    /** 回复人名称 */
    private String replyUserName;

    /** 回复人类型(0-用户/1-处理人员) */
    private String replyUserType;

    /** 附件路径 */
    private String attachments;

    /** 附件列表 */
    private List<String> attachmentList;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 创建人 */
    private String createBy;
}
