package com.inspur.agriculture.input.vo.supplier;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 审批响应VO
 *
 * @author igdp
 */
@Data
public class ApproveResponseVO {

    /** 认证ID */
    private Long certId;

    /** 认证状态 */
    private Integer status;

    /** 状态描述 */
    private String statusDesc;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;

    /** 审批人ID */
    private Long approverId;

    /** 驳回原因 */
    private String rejectReason;
}
