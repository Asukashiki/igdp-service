package com.inspur.assets.monitor.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 告警通知配置信息
 *
 * @author 王海龙
 * @date 2024/9/10
 */
@TableName("assets_cur_configuration")
@Setter
@Getter
public class AssetsCurConfiguration extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 告警来源
     */
    private String source;
    /**
     * 告警等级
     */
    private String grade;
    /**
     * 资产类型
     */
    private String assetsType;
    /**
     * 资产名称
     */
    private String assetsName;
    /**
     * 通知形式
     */
    private String notificationFrom;
    /**
     * 执行频率
     */
    private String execution;
    /**
     * 执行单位
     */
    private String frequency;
    /**
     * 执行状态
     */
    private String status;
    /**
     * 通知团队
     */
    private String recipient;
}
