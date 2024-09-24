package com.inspur.workorder.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 工单的节点明细
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderNodeDetail
 * @date 2024/4/28 14:46
 */
@TableName("work_order_node_detail")
@Data
public class WorkOrderNodeDetail {
    @TableId(type = IdType.ASSIGN_ID)
    private String detailId;

    private String nodeName;

    private String workOrderCode;

    private Integer number;

    /**
     * 节点状态
     * 0未到达、1当前节点、2已过节点
     * */
    private String nodeState;

    /**
     * 并行会签 and，当前节点所有人签署
     * 顺序会签 sort，当前节点处理人按顺序签署
     * 或签 or，任何一个人签署则进入下一个节点
     * */
    private String countersignType;

    public static final String COUNTERSIGN_TYPE_AND = "and";
    public static final String COUNTERSIGN_TYPE_SORT = "sort";
    public static final String COUNTERSIGN_TYPE_OR = "or";
}
