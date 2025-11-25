package com.inspur.agriculture.input.dto.supplier;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 供应商认证查询DTO
 *
 * @author igdp
 */
@Data
public class SupplierCertQueryDTO {

    /** 企业/组织名称 */
    private String orgName;

    /** 统一社会信用代码 */
    private String creditCode;

    /** 联系人姓名 */
    private String contactName;

    /** 联系人手机 */
    private String contactPhone;

    /** 认证状态(0-未通过/1-审核中/2-已通过) */
    private Integer status;

    /** 行政区划代码 */
    private String adCode;

    /** 申请开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTimeStart;

    /** 申请结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTimeEnd;

    /** 审批开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTimeStart;

    /** 审批结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTimeEnd;

    /** 关键词搜索（企业名称/信用代码/联系人） */
    private String keyword;
}
