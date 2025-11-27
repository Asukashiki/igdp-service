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
 * 信息反馈对象 feedback
 *
 * @author igdp
 */
@Data
@TableName("feedback")
public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 反馈ID */
    @TableId(type = IdType.AUTO)
    private Long feedbackId;

    /** 反馈编号 */
    private String feedbackNo;

    /** 反馈类型(1质量2服务3效果4价格5其他) */
    private String feedbackType;

    /** 反馈标题 */
    private String title;

    /** 反馈内容 */
    private String content;

    private Integer inputId;

    private String inputName;

    private Integer supplierId;

    private String supplierName;


    /** 联系人 */
    private String contactName;

    /** 联系电话 */
    private String contactPhone;

    /** 联系邮箱 */
    private String contactEmail;

    /** 优先级(0-低/1-中/2-高/3-紧急) - 暂不存储到数据库 */
    @TableField(exist = false)
    private String priority;

    /** 状态(0-待处理/1-处理中/2-已完成/3-已关闭) */
    private String status;

    /** 处理人ID */
    private String handlerId;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    /** 处理结果 */
    private String handleResult;

    /** 处理备注 */
    private String handleRemark;

    /** 满意度评分(1-5分) */
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

    /** 删除标志(0-正常/2-删除) */
    private String delFlag;


    /** 反馈用户名称(非数据库字段) */
    @TableField(exist = false)
    private String userName;

    /** 处理人名称(非数据库字段) */
    @TableField(exist = false)
    private String handlerName;

    /** 反馈类型描述(非数据库字段) */
    @TableField(exist = false)
    private String feedbackTypeDesc;

    /** 状态描述(非数据库字段) */
    @TableField(exist = false)
    private String statusDesc;

    /** 优先级描述(非数据库字段) */
    @TableField(exist = false)
    private String priorityDesc;

    /** 处理耗时(小时,非数据库字段) */
    @TableField(exist = false)
    private Long processingHours;


}
