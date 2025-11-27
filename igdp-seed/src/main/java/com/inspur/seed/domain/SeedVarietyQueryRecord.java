package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 种子品种查询记录表实体类
 *
 * @author system
 */
@TableName("seed_variety_query_record")
@Setter
@Getter
public class SeedVarietyQueryRecord extends BaseEntity {

    /**
     * 查询记录唯一标识
     */
    @TableId
    private String queryId;

    /**
     * 查询关键词
     */
    private String queryKeyword;

    /**
     * 查询时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime queryTime;

    /**
     * 访问IP地址
     */
    private String ipAddress;

    /**
     * 查询结果数量
     */
    private Integer queryResultCount;

    /**
     * 查看的发布ID
     */
    private String viewedPublishId;
}
