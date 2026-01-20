package com.inspur.agriculture.input.domain.registration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核流程记录实体类
 * 记录每一次审核动作和意见
 *
 * @author igdp
 */
@Data
@TableName("t_audit_log")
public class AuditLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键 (UUID)
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 关联 t_org_registration.id (无外键约束)
     */
    private String registrationId;

    /**
     * 审核人ID
     */
    private String auditorId;

    /**
     * 审核人姓名
     */
    private String auditorName;

    /**
     * 审核结果：1-通过, 2-驳回
     */
    private Integer auditResult;

    /**
     * 审核意见/驳回原因
     */
    private String auditComment;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;
}
