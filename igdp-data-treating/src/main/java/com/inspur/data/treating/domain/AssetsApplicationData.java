package com.inspur.data.treating.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 应用系统统计数量
 * @author liyunlong
 * @version 1.0
 * @ClassName ApplicationData
 * @date 2024/7/17 14:24
 */
@TableName("assets_application_data")
@Setter
@Getter
public class AssetsApplicationData {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 实例
     * 应用系统资产编号等
     * */
    private String target;
    /**
     * 实例名称
     * 应用系统名称
     * */
    private String targetName;
    /**
     * 数据类型
     * 与target以及date组成唯一索引，保证target+period+dataType+date是唯一数据
     * 对应枚举DataType
     * user_total 用户总量；visit_num 访问量；active_user_num 活跃用户数；visit_user_num 访问用户数。。。
     * */
    private String dataType;
    /**
     *
     * */
    private String dataTypeName;
    /**
     * 数据
     * */
    private BigDecimal value;
    /**
     * 统计区间维度
     * year 年；month 月；day 日；hour 小时；
     * */
    private String period;
    /**
     * 日期，根据period来拼接
     * yyyy-MM-dd HH:00:00每小时数据
     * yyyy-MM-dd 每天数据
     * yyyy-MM 月度数据
     * yyyy 年度数据
     * */
    private String date;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
