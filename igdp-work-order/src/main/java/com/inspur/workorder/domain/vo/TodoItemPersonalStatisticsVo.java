package com.inspur.workorder.domain.vo;

import lombok.Data;

/**
 * 待办事项个人统计信息
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName TodoItemStatisticsVo
 * @date 2024/5/17 9:55
 */
@Data
public class TodoItemPersonalStatisticsVo {
    private Integer year;

    private Integer month;

    /**
     * 我的发起
     */
    private Long initiativeCount = 0L;
    /**
     * 我的办结
     */
    private Long concludeCount = 0L;
    /**
     * 我的待办
     */
    private Long undoCount = 0L;
    /**
     * 我的已办
     */
    private Long doneCount = 0L;
    /**
     * 我发起的处理中的
     */
    private Long countActive = 0L;
}
