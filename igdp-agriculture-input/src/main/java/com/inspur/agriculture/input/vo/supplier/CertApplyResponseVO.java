package com.inspur.agriculture.input.vo.supplier;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 认证申请响应VO
 *
 * @author igdp
 */
@Data
public class CertApplyResponseVO {

    /** 认证ID */
    private Long certId;

    /** 认证状态 */
    private Integer status;

    /** 状态描述 */
    private String statusDesc;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;
}
