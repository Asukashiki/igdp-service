package com.inspur.scenario.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.domain.BaseLLmEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 场景广场
 */
@Data
@TableName("scenario_hub")
@EqualsAndHashCode(callSuper = false)
public class ScenarioSquareLLmEntity extends BaseLLmEntity {
    private String scenarioNameZh;
    private String category; //通用
    private String zyCategory; //专用
    private String lyCategory; //领域
    private String description;
    private String painPoints;
    private String technologies;
    private String url;
    private String agentId;
    private String baseModelId;
    private String industryModelId;
    private String visibility;
}
