package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 繁殖种子监管信息实体类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seed_breeding_seed_supervision")
public class BreedingSeedSupervision extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID(主键)
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String dataId;

    /**
     * 繁育批次ID
     */
    private String breedingBatchId;

    /**
     * 认证ID
     */
    private String authId;

    /**
     * 批准编号
     */
    private String approvalNumber;

    /**
     * 批准机构
     */
    private String approvalOrganization;

    /**
     * 批准日期
     */
    private Date approvalDate;

    /**
     * 认证文件(文件路径)
     */
    private String certificationDocument;

    /**
     * 删除标志(0正常 2删除)
     */
    private String delFlag;
}
