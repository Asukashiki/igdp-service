package com.inspur.seed.domain.breed;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Breeder Seed 分发主表
 *
 * @author igdp
 */
@Data
@TableName("breed_seed_distribute_main")
public class BreedSeedDistributeMain implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键(UUID)
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String distributeId;

    /**
     * OSE ID
     */
    private String oseId;

    /**
     * OSE名称(自动带出)
     */
    private String oseName;

    /**
     * 分发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 分发操作人姓名
     */
    private String people;

    /**
     * 种子机构名称(固定值)
     */
    private String organ;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 明细数量总和
     */
    private BigDecimal totalDistributeQuantity;

    /**
     * 分发状态(默认已分发)
     */
    private String distributeStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
