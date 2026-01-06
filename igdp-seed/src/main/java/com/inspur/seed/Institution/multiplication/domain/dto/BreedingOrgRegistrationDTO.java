package com.inspur.seed.Institution.multiplication.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 提交/修改注册申请的请求体 DTO
 *
 * @author igdp
 */
@Data
public class BreedingOrgRegistrationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID（有值则为修改，无值则为新增）
     */
    private String id;

    /**
     * 机构类型：UNION-联合会, COOPERATIVE-合作社
     */
    private String orgType;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 统一社会信用代码
     */
    private String unifiedCode;

    /**
     * 经营许可证号
     */
    private String licenseNumber;

    /**
     * 许可有效期起
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date licenseStart;

    /**
     * 许可有效期止
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date licenseEnd;

    /**
     * 种子/作物类型(逗号分隔)
     */
    private String cropTypes;

    /**
     * 所属区域代码 (从行政区划树选择)
     */
    private String regionCode;

    /**
     * 所属区域名称 (显示用)
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String fullAddress;

    /**
     * 纬度
     */
    private String gpsLat;

    /**
     * 经度
     */
    private String gpsLng;

    /**
     * 营业执照图片路径
     */
    private String businessLicenseUrl;

    /**
     * 税务登记证路径
     */
    private String taxCertUrl;

    /**
     * 其他许可证(JSON存储)
     */
    private String otherCertsJson;

    /**
     * 申请注册的登录账号
     */
    private String applyUsername;

    /**
     * 申请注册的密码 (明文，后端加密存储)
     */
    private String applyPassword;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机号
     */
    private String contactMobile;

    /**
     * 联系人邮箱
     */
    private String contactEmail;
}
