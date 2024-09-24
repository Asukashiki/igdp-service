package com.inspur.workorder.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 待办事项处理明细
 * 每个处理节点一条明细记录
 * @author liyunlong02
 * @version 1.0
 * @date 2024/4/15 15:59
 */
@TableName("todo_item_detail")
@Data
public class TodoItemDetail {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 对应低代码等业务系统的明细id
     * */
    private String detailId;

    /**
     * 对应的数据id
     * */
    private String dataId;

    /**
     * 工单待办事项ID
     * */
    private String todoId;

    private String businessCode;

    private String businessId;

    private String type;

    /**
     * 内容
     * */
    private String content;

    /**
     * 处理状态
     * 0未处理；1已处理
     * */
    private String status;

    /**
     * 是否已读
     * 0未读；1已读
     * */
    private String readStatus;


    /**
     * userId
     * */
    private String userId;

    private String deptId;

    /**
     * 本地创建时间
     * */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     * */
    private LocalDateTime updateTime;

    /**
     * 处理时间
     * */
    private String handleTime;

    private Integer year;

    private Integer month;

    /**
     * 工单主信息
     * */
    private transient TodoItem todoItem;

    public static final String STATUS_NEW = "0";
    public static final String STATUS_COMPLETE = "1";

    public static final String READ_STATUS_NEW = "0";
    public static final String READ_STATUS_COMPLETE = "1";

}
