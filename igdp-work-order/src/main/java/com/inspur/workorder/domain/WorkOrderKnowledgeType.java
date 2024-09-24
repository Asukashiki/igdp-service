package com.inspur.workorder.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import com.inspur.common.core.domain.entity.SysDept;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@TableName("work_order_knowledge_type")
@Setter
@Getter
public class WorkOrderKnowledgeType extends BaseEntity {
    @TableId
    private String id;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 父菜单ID
     */
    private String parentId;
    /**
     * 状态
     */
    private String type;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /** 子节点 */
    private transient List<WorkOrderKnowledgeType> children = new ArrayList<>();
}
