package com.inspur.agriculture.input.vo.feedback;

import lombok.Data;

/**
 * 反馈统计VO
 *
 * @author igdp
 */
@Data
public class FeedbackStatsVO {

    /** 总反馈数 */
    private Long totalFeedbacks;

    /** 待处理数 */
    private Long pendingCount;

    /** 处理中数 */
    private Long processingCount;

    /** 已完成数 */
    private Long completedCount;

    /** 已关闭数 */
    private Long closedCount;

    /** 今日新增 */
    private Long todayCount;

    /** 本周新增 */
    private Long weekCount;

    /** 本月新增 */
    private Long monthCount;

    /** 平均处理时长(小时) */
    private Double avgProcessingHours;

    /** 平均满意度 */
    private Double avgSatisfaction;

    /** 投诉类型数量 */
    private Long complaintCount;

    /** 建议类型数量 */
    private Long suggestionCount;

    /** 咨询类型数量 */
    private Long consultationCount;

    /** 故障报告数量 */
    private Long bugReportCount;

    /** 高优先级数量 */
    private Long highPriorityCount;

    /** 紧急优先级数量 */
    private Long urgentPriorityCount;
}
