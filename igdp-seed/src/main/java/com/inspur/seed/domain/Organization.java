package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织管理 organization
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("organization")
public class Organization extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 组织编号 */
    private String orgCode;

    /** 组织名称 */
    private String orgName;

    /** 组织类别 */
    private String orgCategory;

    /** 地点 */
    private String location;

    /** 所属区域 */
    private String region;

    /** 负责人 */
    private String contactPerson;

    /** 电话号码 */
    private String phoneNumber;

    /** 状态（0正常 1停用） */
    private String status;

    /** 备注 */
    private String remark;

    /** 搜索关键字 */
    @TableField(exist = false)
    private String keyword;
}
