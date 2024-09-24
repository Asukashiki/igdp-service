package com.inspur.workorder.domain.payload;

import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ProcessQueryPayload
 * @date 2024/5/31 17:11
 */
@Setter
@Getter
public class ProcessQueryPayload extends BaseEntity {

    private String deptId;

    private String userId;

    private String itemType;

    private String state;

    private String itemState;

    private Integer year;

    private Integer month;
}
