package com.inspur.agriculture.input.dto.supplier;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 供应商认证申请DTO
 *
 * @author igdp
 */
@Data
public class SupplierCertApplyDTO {

    /** 用户ID */
    @NotNull(message = "用户ID不能为空")
    private String userId;

    /** 企业/组织名称 */
    @NotBlank(message = "企业/组织名称不能为空")
    private String orgName;

    /** 统一社会信用代码 */
    @NotBlank(message = "统一社会信用代码不能为空")
//    @Pattern(regexp = "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$",
//             message = "统一社会信用代码格式错误")
    private String creditCode;

    /** 法定代表人/负责人 */
    @NotBlank(message = "法定代表人/负责人不能为空")
    private String legalPerson;

    /** 法定代表人身份证号 */
    @NotBlank(message = "法定代表人身份证号不能为空")
//    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",
//             message = "身份证号格式错误")
    private String legalId;

    /** 行政区划代码 */
    @NotBlank(message = "行政区划代码不能为空")
    private String adCode;

    /** 经营范围/主要产品 */
    @NotBlank(message = "经营范围/主要产品不能为空")
    private String businessScope;

    /** 营业执照存储路径 */
    @NotBlank(message = "营业执照文件不能为空")
    private String licensePath;

    /** 联系人姓名 */
    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    /** 联系人手机 */
    @NotBlank(message = "联系人手机不能为空")
    private String contactPhone;
}
