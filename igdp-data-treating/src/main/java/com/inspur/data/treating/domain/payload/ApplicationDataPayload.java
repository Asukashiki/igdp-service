package com.inspur.data.treating.domain.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ApplicationDataPayload
 * @date 2024/7/24 10:10
 */
@Setter
@Getter
public class ApplicationDataPayload {
    @NotBlank(message = "应用系统资产编号[target]不能为空")
    private String target;

    /**
     * 数据
     */
    @NotBlank(message = "统计数据[value]不能为空")
    private String value;

    /**
     * 数据类型
     * 与target以及date组成唯一索引，保证target+dataType+date是唯一数据
     * 对应枚举DataType
     * user_total 用户总量；visit_num 访问量；active_user_num 活跃用户数；visit_user_num 访问用户数。。。
     * */
    @NotBlank(message = "数据类型[dataType]不能为空")
    private String dataType;

    /**
     * 统计区间维度
     * year 年；month 月；day 日；hour 小时；
     * */
    @NotBlank(message = "统计区间维度[period]不能为空")
    private String period;

    /**
     * year就传入yyyy
     * month就传入yyyy-MM
     * day 传入yyyy-MM-dd
     * */
    @NotBlank(message = "日期不能为空")
    private String date;

    /**
     * 传入小时
     * 0-23
     * */
    private String time;
}
