package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 机构注册审核记录实体类
 *
 * @author system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("org_registration_audit")
public class OrgRegistrationAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键UUID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 机构ID(关联org_enterprise_info)
     */
    private String enterpriseId;

    /**
     * 审核人ID
     */
    private String auditUserId;

    /**
     * 审核人姓名
     */
    private String auditUserName;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    /**
     * 审核结果(approved/rejected)
     */
    private String auditResult;

    /**
     * 审核意见(驳回时必填)
     */
    private String auditOpinion;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 是否删除(0:否 1:是)
     */
    @TableLogic
    private Integer isDeleted;
}
