package com.inspur.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysOpenApiLog
 * @date 2024/6/14 15:40
 */
@TableName("sys_open_api_log")
@Setter
@Getter
public class SysOpenApiLog extends BaseEntity {
    /**
     * id
     * */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String appId;

    private String apiPath;

    private String title;

    private String method;

    private String body;

    private String result;

    private String ip;

}
