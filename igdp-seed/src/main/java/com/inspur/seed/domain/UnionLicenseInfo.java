package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Union许可信息实体类
 *
 * @author system
 */
@TableName("union_license_info")
@Setter
@Getter
public class UnionLicenseInfo extends BaseEntity {

    /**
     * 数据唯一标识（主键，系统生成）
     */
    @TableId
    private String dataId;

    /**
     * 企业唯一标识（由后端自动生成和设置）
     */
    private String enterpriseId;

    /**
     * 营业执照文件路径
     */
    @NotBlank(message = "营业执照不能为空")
    private String businessLicense;

    /**
     * 种子许可证文件路径
     */
    private String seedLicense;

    /**
     * 税务登记证文件路径
     */
    private String taxCertificate;

    /**
     * 工厂许可证文件路径
     */
    @NotBlank(message = "工厂许可证不能为空")
    private String factoryPermit;
}
