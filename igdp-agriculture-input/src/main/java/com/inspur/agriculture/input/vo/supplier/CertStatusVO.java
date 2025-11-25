package com.inspur.agriculture.input.vo.supplier;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 认证状态查询响应VO
 *
 * @author igdp
 */
@Data
public class CertStatusVO {

    /** 认证ID */
    private Long certId;

    /** 用户ID */
    private Long userId;

    /** 企业/组织名称 */
    private String orgName;

    /** 统一社会信用代码 */
    private String creditCode;

    /** 认证状态 */
    private Integer status;

    /** 状态描述 */
    private String statusDesc;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;

    /** 审核意见 */
    private String auditOpinion;

    /** 驳回原因 */
    private String rejectReason;

    /** 审批人ID */
    private Long approverId;
}
