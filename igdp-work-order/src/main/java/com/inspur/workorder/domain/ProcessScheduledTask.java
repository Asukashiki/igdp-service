package com.inspur.workorder.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流程工单定时任务配置
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderScheduledTask
 * @date 2024/6/4 15:46
 */
@Setter
@Getter
@TableName("process_scheduled_task")
public class ProcessScheduledTask extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String taskId;

    private String taskName;

    /**
     * 低代码应用id
     * */
    private String icdAppId;

    /**
     * 低代码formId
     * */
    private String icdFormId;

    private String icdVersionId;

    private String userId;

    private String deptId;

    private String status;

    /**发起人userId*/
    private String initiatorUserId;

    /**
     * 下个节点处理人
     * 格式参考icd的AuditUserBo
     * id（用户id）
     * name 用户名称
     * auditOrder 审批顺序
     * */
    private String approveNodeUsers;

    private transient List<String> approveNodeUserIds;

    private transient String initiatorName;

    private transient String approveNodeUserName;
}
