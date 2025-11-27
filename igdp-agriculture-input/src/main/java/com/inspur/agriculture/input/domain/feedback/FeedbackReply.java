package com.inspur.agriculture.input.domain.feedback;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 反馈回复对象 feedback_reply
 *
 * @author igdp
 */
@Data
@TableName("feedback_reply")
public class FeedbackReply implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 回复ID */
    @TableId(type = IdType.AUTO)
    private Long replyId;

    /** 反馈ID */
    private Long feedbackId;

    /** 回复内容 */
    private String content;

    /** 回复人ID */
    private String replyUserId;

    /** 回复人类型(0-用户/1-处理人员) */
    private String replyUserType;

    /** 附件路径(多个用逗号分隔) */
    private String attachments;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 创建人 */
    private String createBy;

    /** 删除标志(0-正常/2-删除) */
    private String delFlag;

    /** 回复人名称(非数据库字段) */
    @TableField(exist = false)
    private String replyUserName;
}
