package com.inspur.assets.monitor.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 告警处理记录
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName AssetsAlertHandleLog
 * @date 2024/7/8 11:07
 */
@Setter
@Getter
@TableName("assets_alert_handle_log")
public class AssetsAlertHandleLog extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 对应alert_cur以及alert_his中的id
     */
    private String alertId;

    /**
     * 处理内容
     */
    private String handleContent;

    /**
     * 处理结果
     */
    private String handleResult;
    /**
     * 处理时间
     */
    private String handleTime;

    private String userId;

    private String deptId;
}
