package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 种子推广信息表实体类
 *
 * @author system
 */
@TableName("seed_promotion_info")
@Setter
@Getter
public class SeedPromotionInfo extends BaseEntity {

    /**
     * 推广信息唯一标识
     */
    @TableId
    private String promotionId;

    /**
     * 关联企业唯一标识
     */
    private String enterpriseId;

    /**
     * 推广标题
     */
    private String title;

    /**
     * 宣传视频存储路径
     */
    private String videoUrl;

    /**
     * 推广摘要
     */
    private String promotionSummary;

    /**
     * 推荐品种
     */
    private String recommendedVarieties;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    /**
     * 有效期（天）
     */
    private Integer validPeriod;

    /**
     * 分享链接
     */
    private String shareLink;

    /**
     * 访问次数
     */
    private Integer visitCount;


    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    private String updateBy;
}
