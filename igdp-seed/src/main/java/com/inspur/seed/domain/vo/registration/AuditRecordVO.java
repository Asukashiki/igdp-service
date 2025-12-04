package com.inspur.seed.domain.vo.registration;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审核记录VO
 *
 * @author system
 */
@Data
public class AuditRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审核记录ID
     */
    private String id;

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
     * 审核结果
     */
    private String auditResult;

    /**
     * 审核结果名称
     */
    private String auditResultName;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 备注
     */
    private String remark;
}
