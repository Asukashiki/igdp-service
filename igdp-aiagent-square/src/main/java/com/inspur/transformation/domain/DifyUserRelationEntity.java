package com.inspur.transformation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 场景广场
 */
@Data
@TableName("dify_user_releation")
@EqualsAndHashCode(callSuper = false)
public class DifyUserRelationEntity {
    private Integer id;
    private String userId;
    private String agentId; //通用
    private String type; //0智能体 1服务组件

}
