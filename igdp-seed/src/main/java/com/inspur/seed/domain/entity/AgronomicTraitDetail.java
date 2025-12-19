package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 农艺性状明细数据实体类
 * 
 * @author inspur
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("agronomic_trait_detail")
public class AgronomicTraitDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 明细ID - {record_id}-D{序号} */
    @TableId(value = "detail_id", type = IdType.INPUT)
    private String detailId;

    // ===== 显式映射BaseEntity字段 =====
    @TableField("create_by")
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private java.time.LocalDateTime createTime;

    @TableField("update_by")
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private java.time.LocalDateTime updateTime;

    /** 主记录ID */
    @TableField("record_id")
    private String recordId;

    /** 性状代码(字典值) */
    @TableField("trait_code")
    private String traitCode;

    /** 性状名称(从字典获取) */
    @TableField("trait_name")
    private String traitName;

    /** 性状值 */
    @TableField("trait_value")
    private BigDecimal traitValue;

    /** 单位(从字典actual_value获取) */
    @TableField("unit")
    private String unit;

    /** 排序序号 */
    @TableField("sort_order")
    private Integer sortOrder;

    /** 逻辑删除(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
