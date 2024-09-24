package com.inspur.workorder.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 常用流程工单
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName FrequentlyUsedProcess
 * @date 2024/5/20 11:16
 */
@TableName("process_frequently_used")
@Setter
@Getter
public class ProcessFrequentlyUsed extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String title;

    private String type;

    private String routePath;

    private String linkUrl;

    private String userId;

    private String deptId;

    /**
     * 试用次数，以此排序
     * */
    private Long usedNumber;
}
