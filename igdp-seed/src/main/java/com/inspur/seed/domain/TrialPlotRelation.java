package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 试验-地块关联实体类
 *
 * @author inspur
 */
@Data
@TableName("trial_plot_relation")
public class TrialPlotRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 关联记录ID(主键) */
    @TableId(value = "relation_id", type = IdType.ASSIGN_UUID)
    private String relationId;

    /** 试验ID */
    @TableField("trial_id")
    private String trialId;

    /** 地块ID */
    @TableField("ground_id")
    private String groundId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
