package com.inspur.agriculture.input.dto.feedback;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 反馈提交/编辑DTO
 *
 * @author igdp
 */
@Data
public class FeedbackDTO {

    /** 反馈ID（编辑时必填） */
    private Long feedbackId;

    /** 反馈类型(0-投诉/1-建议/2-咨询/3-故障报告/4-其他) */
    @NotBlank(message = "反馈类型不能为空")
    private String feedbackType;

    /** 反馈标题 */
    @NotBlank(message = "反馈标题不能为空")
    @Size(max = 200, message = "反馈标题最大长度为200字符")
    private String title;

    /** 反馈内容 */
    @NotBlank(message = "反馈内容不能为空")
    @Size(max = 2000, message = "反馈内容最大长度为2000字符")
    private String content;

    /** 联系人 */
    @NotBlank(message = "联系人不能为空")
    @Size(max = 50, message = "联系人最大长度为50字符")
    private String contactName;

    /** 联系电话 */
    @NotBlank(message = "联系电话不能为空")
    @Size(max = 20, message = "联系电话最大长度为20字符")
    private String contactPhone;

    /** 联系邮箱 */
    @Size(max = 100, message = "联系邮箱最大长度为100字符")
    private String contactEmail;

    /** 附件路径(多个用逗号分隔) */
    private String attachments;

    /** 优先级(0-低/1-中/2-高/3-紧急) */
    private String priority;

    private Integer inputId;

    private String inputName;

    private Integer supplierId;

    private String supplierName;

    private String inputCategory;

    private String inputType;
}
