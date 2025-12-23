package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 地块审核记录实体类
 *
 * @author inspur
 */
@Data
@TableName("plot_audit_record")
public class PlotAuditRecord {

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 地块ID */
    @TableField("plot_id")
    private String plotId;

    /** 审核类型:SUBMIT-提交,APPROVE-通过,REJECT-退回,ARCHIVE-归档,CANCEL-作废 */
    @TableField("audit_type")
    private String auditType;

    /** 审核前状态 */
    @TableField("before_status")
    private String beforeStatus;

    /** 审核后状态 */
    @TableField("after_status")
    private String afterStatus;

    /** 审核意见 */
    @TableField("audit_opinion")
    private String auditOpinion;

    /** 审核人ID */
    @TableField("auditor")
    private String auditor;

    /** 审核人姓名 */
    @TableField("auditor_name")
    private String auditorName;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("audit_time")
    private LocalDateTime auditTime;
}
