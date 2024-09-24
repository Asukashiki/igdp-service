package com.inspur.workorder.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 工单同步任务所需要的配置内容
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderSyncConfig
 * @date 2024/6/22 19:32
 */
@TableName("work_order_sync_config")
@Setter
@Getter
public class WorkOrderSyncConfig extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     * */
    private String name;

    /**
     * 状态
     * 0正常；1停用
     * */
    private String status;

    private String databaseSource;

    /**
     * 对应三方应用id
     * SysOpenApp
     * */
    private String openAppId;

    /**
     * sql语句
     * */
    private String sqlStr;

    private String userId;

    private String deptId;
}
