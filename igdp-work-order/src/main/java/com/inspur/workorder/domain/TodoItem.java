package com.inspur.workorder.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 待办事项
 *
 * @author liyunlong
 * @date 2024/4/8
 */
@TableName("todo_item")
@Data
public class TodoItem extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String todoId;

    /**
     * 工单事项ID
     */
    private String businessId;
    /**
     * 事项标题
     */
    private String title;

    /**
     * 发起人userId
     */
    private String userId;

    /**
     * 发起人部门id
     */
    private String deptId;

    /**
     * 工单事项编号
     */
    private String businessCode;

    /**
     * 内容
     */
    private String businessContent;

    /**
     * 低代码对应的appid、formId、dataId
     * */
    private String appId;

    private String formId;

    private String dataId;

    /**
     * 类型
     * 故障工单、事项工单、问题处理
     */
    private String type;

    /**
     * 来源
     * 客服电话、服务门户、微信。。。
     */
    private String businessSource;

    /**
     * 来源
     * icd 低代码流程表单
     * workOrder 系统内置工单。。。
     */
    private String source;

    /**
     * 工单所属系统模块
     * 运维工单、运维事项、资产流程···
     */
    private String modular;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 当前状态，由事项推送方转译后内容
     * 0: 无效，未发起
     * 1：处理中，对应低代码的1、3、5、-1
     * 2：已解决，对应低代码的2
     * 4：未解决，对应低代码的4
     */
    private String state;

    /**
     * 业务系统推送过来的状态码
     * 低代码流程状态
     */
    private String businessStatus;

    /**
     * 当前处理人姓名
     */
    private transient String currentProcessorNames;

    /**
     * 当前处理人userId
     * 多个处理人用逗号分割
     */
    private String currentProcessorIds;

    /**
     * 本地创建时间
     */
    private LocalDateTime createTime;

    /**
     * 其他类型参数
     * json格式存储
     */
    private String paramsJson;

    /**
     * 回调地址
     */
    private String callbackLink;

    private Integer year;

    private Integer month;

    /**
     * 低代码流程状态码
     * 0 未提交
     * 1 处理中
     * 2 完成（通过）
     * 3 重新提交
     * 4 完成（未通过）
     * 5 并行处理中
     * -1 重新发起
     */
    public static final String BUSINESS_STATUS_INVALID = "0";
    public static final String BUSINESS_STATUS_ACTIVE = "1";
    public static final String BUSINESS_STATUS_SUCCESS = "2";
    public static final String BUSINESS_STATUS_FAILURE = "4";
    public static final String BUSINESS_STATUS_RE_COMMIT = "3";
    public static final String BUSINESS_STATUS_RE_START = "-1";

    /**
     * 对应字典中字典项code
     */
    public static final String BUSINESS_STATUS_DICT_CODE = "work_order_business_status";

    /**
     * item中的state状态码
     * 1：处理中，对应低代码的1、3、5、-1
     * 2：已解决，对应低代码的2
     * 4：未解决，对应低代码的4
     */
    public static final String STATE_INVALID = "0";
    public static final String STATE_ACTIVE = "1";
    public static final String STATE_SUCCESS = "2";
    public static final String STATE_FAILURE = "4";

    public static final String SOURCE_ICD = "icd";

    public static final Map<String, String> STATE_MAP = new HashMap<String, String>() {{
        put(STATE_INVALID, "未发起");
        put(STATE_ACTIVE, "处理中");
        put(STATE_SUCCESS, "已完成");
        put(STATE_FAILURE, "未解决");
    }};
    private transient String sql;
    private transient String timeType;

}
