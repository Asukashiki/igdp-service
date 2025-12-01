package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 播种信息实体类
 *
 * @author inspur
 */
@Data
@TableName("sowing_info")
public class SowingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 播种记录ID(主键) */
    @TableId(value = "sowing_id", type = IdType.ASSIGN_UUID)
    private String sowingId;

    /** 关联地块ID */
    @TableField("ground_id")
    private String groundId;

    /** 播种种子数量(kg) */
    @TableField("seed_quantity")
    private BigDecimal seedQuantity;

    /** 播种方式 */
    @TableField("sowing_method")
    private String sowingMethod;

    /** 播种时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("sowing_time")
    private Date sowingTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
