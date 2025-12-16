package com.inspur.agriculture.input.domain.registration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 机构注册申请实体类
 * 存储 Union/Cooperative 的全部申报信息及账号信息
 *
 * @author igdp
 */
@Data
@TableName("t_org_registration")
public class OrgRegistration implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键 (UUID)
     */
    @TableId(type = IdType.ASSIGN_UUID)
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
     * 投入品类型(逗号分隔): SEED,FERTILIZER,PESTICIDE
     */
    private String inputTypes;

    /**
     * 所属区域代码 (从/rbac/organ/allTree选择)
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
     * 年产能/吞吐量
     */
    private BigDecimal annualCapacity;

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
     * 申请注册的密码 (加密存储)
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

    /**
     * 审核状态：0-待审核, 1-已通过, 2-已驳回
     */
    private Integer auditStatus;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 申请人ID
     */
    private String applicantId;

    /**
     * 删除标识：0-正常, 2-删除
     */
    private String delFlag;
}
