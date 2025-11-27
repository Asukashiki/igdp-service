package com.inspur.agriculture.input.dto.feedback;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 反馈处理DTO
 *
 * @author igdp
 */
@Data
public class FeedbackHandleDTO {

    /** 反馈ID */
    @NotNull(message = "反馈ID不能为空")
    private Long feedbackId;

    /** 处理结果 */
    @NotBlank(message = "处理结果不能为空")
    @Size(max = 1000, message = "处理结果最大长度为1000字符")
    private String handleResult;

    /** 处理备注 */
    @Size(max = 500, message = "处理备注最大长度为500字符")
    private String handleRemark;

    /** 状态(1-处理中/2-已完成) */
    @NotBlank(message = "处理状态不能为空")
    private String status;
}
