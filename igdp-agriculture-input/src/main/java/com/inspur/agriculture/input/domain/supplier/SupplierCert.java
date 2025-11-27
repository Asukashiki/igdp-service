package com.inspur.agriculture.input.domain.supplier;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商认证对象 supplier_cert
 *
 * @author igdp
 */
@Data
@TableName("supplier_cert")
public class SupplierCert implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 认证ID */
    @TableId(type = IdType.AUTO)
    private Long certId;

    /** 用户ID */
    private String userId;

    /** 企业/组织名称 */
    private String orgName;

    /** 统一社会信用代码 */
    private String creditCode;

    /** 法定代表人/负责人 */
    private String legalPerson;

    /** 法定代表人身份证号 */
    private String legalId;

    /** 行政区划代码 */
    private String adCode;

    /** 经营范围/主要产品 */
    private String businessScope;

    /** 营业执照存储路径 */
    private String licensePath;

    /** 联系人姓名 */
    private String contactName;

    /** 联系人手机 */
    private String contactPhone;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /** 认证状态(0-未通过/1-审核中/2-已通过) */
    private Integer status;

    /** 审批人ID */
    private Long approverId;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;

    /** 审核意见 */
    private String auditOpinion;

    /** 驳回原因 */
    private String rejectReason;

    /** 创建人 */
    private String createPeople;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    private String updatePeople;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 删除标志(0-正常/2-删除) */
    private String delFlag;
}
