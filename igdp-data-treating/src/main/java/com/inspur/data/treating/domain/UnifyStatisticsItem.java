package com.inspur.data.treating.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import com.inspur.data.treating.enums.StatisticsItemCategory;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 统一统计指标
 * @author liyunlong
 * @version 1.0
 * @ClassName UnifyStatisticsItem
 * @date 2024/7/17 11:14
 */
@TableName("unify_statistics_item")
@Setter
@Getter
public class UnifyStatisticsItem extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @NotBlank(message = "名称不能为空")
    private String name;

    @NotBlank(message = "编码不能为空")
    private String code;

    /**
     * 来源
     * assets 资产；alert 告警；application 应用系统数据。。。
     * */
    @NotBlank(message = "数据来源不能为空")
    private String dataSource;

    /**
     * 所属分类
     * 资产状态、资产监控数量、资产告警数量、应用系统数量
     * */
    private String category;

    private String description;

    /**
     * 统计周期
     * 年、月、日、时、分
     * year、month、day、hour、minute
     * */
    @NotBlank(message = "统计周期不能为空")
    private String period;

    /**
     * 统计频率
     * 每年一次、每月一次、每日一次、每小时一次、每分钟一次
     * year、month、day、hour、minute
     * */
    @NotBlank(message = "统计频率不能为空")
    private String frequency;

    /**
     * 指标统计类型
     * cur 实时数据，只保存一条，持续覆盖
     * his 历史数据，增量保存，每次统计都增加一条
     * */
    @NotBlank(message = "指标统计类型不能为空")
    private String type;

    /**
     * 状态
     * 0有效
     * */
    private String status;

    /**
     * sql语句
     * */
    private String selectSql;

    /**
     * 删除标志
     * 0有效；2已删除
     * */
    @TableLogic
    private String delFlag;


    public static String TYPE_CUR = "cur";
    public static String TYPE_HIS = "his";
}
