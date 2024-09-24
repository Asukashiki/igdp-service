package com.inspur.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 统一待办
 * @author liyunlong
 * @date 2024/1/24
 */
@TableName("sys_unify_todo")
@Setter
@Getter
public class SysUnifyTodo extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 标题
     * */
    private String title;

    /**
     * 类型
     */
    private String type;

    /**
     * 数据来源
     */
    private String source;

    /**
     * 来源appId
     * */
    private String appId;

    /**
     * 待办来源：工单、事项、问题。。。
     */
    private String modular;


    /**
     * 发起人userId
     */
    private String initiateUserId;

    /**
     * 详细内容描述
     */
    private String content;

    /**
     * 回调地址
     */
    private String callbackLink;

    /**
     * 等级
     * 1 一般；2紧急；3非常紧急
     */
    private Integer grader;

    /**
     * 接收人用户ID
     */
    private String userId;

    /**
     * 部门id
     * */
    private String deptId;

    /**
     * 业务id，由推送方带过来
     * 后续更新状态由这个id进行更新
     */
    private String businessId;

    /**
     * 业务编码
     * 流程工单号等
     * */
    private String businessCode;

    /**
     * 数据id
     * */
    private String dataId;

    /**
     * 状态
     * 状态:0待办；1已处理;-1撤销作废
     * */
    private String status = STATUS_NEW;

    /**
     * 是否已读
     * 0未读；1已读
     * */
    private String readStatus = READ_STATUS_UN;

    /**
     * 提交时间
     * */
    private LocalDateTime submitTime;

    /**
     * 处理时间
     * */
    private LocalDateTime handleTime;

    /**
     * 状态:0待办；1已处理;-1撤销作废
     */
    public static final String STATUS_NEW = "0";
    public static final String STATUS_DONE = "1";
    public static final String STATUS_REVOKED = "-1";

    /**
     * 等级
     * 1 一般；2紧急；3非常紧急
     */
    public static final Integer GRADER_NORMAL = 1;
    public static final Integer GRADER_MORE = 2;
    public static final Integer GRADER_MOST = 3;

    public static final String READ_STATUS_UN = "0";
    public static final String READ_STATUS_ED = "1";
}
