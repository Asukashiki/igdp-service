package com.inspur.agriculture.input.dto.feedback;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 反馈回复DTO
 *
 * @author igdp
 */
@Data
public class FeedbackReplyDTO {

    /** 反馈ID */
    @NotNull(message = "反馈ID不能为空")
    private Long feedbackId;

    /** 回复内容 */
    @NotBlank(message = "回复内容不能为空")
    @Size(max = 1000, message = "回复内容最大长度为1000字符")
    private String content;

    /** 附件路径(多个用逗号分隔) */
    private String attachments;
}
