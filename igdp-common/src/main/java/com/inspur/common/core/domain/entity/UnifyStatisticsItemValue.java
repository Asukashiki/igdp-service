package com.inspur.common.core.domain.entity;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 统计值
 * @author liyunlong
 * @version 1.0
 * @ClassName UnifyStatisticsItemValue
 * @date 2024/7/17 11:25
 */
@TableName("unify_statistics_item_value")
@Setter
@Getter
public class UnifyStatisticsItemValue extends BaseEntity {
    /**
     * 键值，保证唯一
     * category::item::target::date
     * */
    @TableId
    private String key;

    /**
     * 指标ID
     * */
    private String itemId;

    private String itemCode;

    /**
     * 所属分类
     * 应用运行分析、用户行为分析、应用系统云主机
     * */
    private String category;

    /**
     * 分类名称
     * */
    private String categoryName;

    private String description;

    private String unit;

    private BigDecimal value;

    /**
     * 日期
     * yyyy-MM-dd 每天
     * yyyy-MM 月度
     * yyyy 年度
     * */
    private String date;

    /**
     * 实例
     * 例如：虚拟机类型编号、网络设备、具体的应用系统资产编号等
     * */
    private String target;

    /**
     * 具体名称
     * */
    private String targetName;

    /**
     * 统计内容的code
     * 状态编码、告警等级编码、访问量对应的编码等等
     * */
    private String statisticsCode;

    /**
     * 统计维度名称
     * 在用、库存、严重告警、一般告警、访问量
     * */
    private String statisticsName;

    /**
     * 类型，同UnifyStatisticsItem中的type
     * cur实时数据；his 历史数据
     * 实时数据会根据定时任务进行更新
     * */
    private String type;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONObject labels;

}
