package com.inspur.agriculture.input.dto.supplier;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 供应商认证信息更新DTO
 *
 * @author igdp
 */
@Data
public class SupplierCertUpdateDTO {

    /** 认证ID */
    @NotNull(message = "认证ID不能为空")
    private Long certId;

    /** 企业/组织名称 */
    @NotBlank(message = "企业/组织名称不能为空")
    private String orgName;

    /** 统一社会信用代码 */
    @NotBlank(message = "统一社会信用代码不能为空")
    private String creditCode;

    /** 法定代表人/负责人 */
    @NotBlank(message = "法定代表人/负责人不能为空")
    private String legalPerson;

    /** 法定代表人身份证号 */
    @NotBlank(message = "法定代表人身份证号不能为空")
    private String legalId;

    /** 行政区划代码 */
    @NotBlank(message = "行政区划代码不能为空")
    private String adCode;

    /** 经营范围/主要产品 */
    @NotBlank(message = "经营范围/主要产品不能为空")
    private String businessScope;

    /** 营业执照存储路径 */
    private String licensePath;

    /** 联系人姓名 */
    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    /** 联系人手机 */
    @NotBlank(message = "联系人手机不能为空")
    private String contactPhone;
}
