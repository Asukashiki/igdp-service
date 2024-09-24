package com.inspur.workorder.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author 王海龙
 */
@TableName("work_order_knowledge_search")
@Setter
@Getter
public class WorkOrderSearch extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 查询的关键字
     */
    private String search;
    /**
     * 查询次数
     */
    private Integer frequency;


}
