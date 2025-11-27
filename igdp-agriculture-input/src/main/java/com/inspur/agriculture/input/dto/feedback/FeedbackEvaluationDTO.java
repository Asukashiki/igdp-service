package com.inspur.agriculture.input.dto.feedback;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * 反馈评价DTO
 *
 * @author igdp
 */
@Data
public class FeedbackEvaluationDTO {

    /** 反馈ID */
    @NotNull(message = "反馈ID不能为空")
    private Long feedbackId;

    /** 满意度评分(1-5分) */
    @NotNull(message = "满意度评分不能为空")
    @Min(value = 1, message = "满意度评分最小为1分")
    @Max(value = 5, message = "满意度评分最大为5分")
    private Integer satisfaction;

    /** 评价内容 */
    @Size(max = 500, message = "评价内容最大长度为500字符")
    private String evaluation;
}
