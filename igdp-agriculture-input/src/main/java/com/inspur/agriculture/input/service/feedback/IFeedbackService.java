package com.inspur.agriculture.input.service.feedback;

import com.inspur.agriculture.input.dto.feedback.*;
import com.inspur.agriculture.input.vo.feedback.FeedbackStatsVO;
import com.inspur.agriculture.input.vo.feedback.FeedbackVO;

import java.util.List;

/**
 * 反馈Service接口
 *
 * @author igdp
 */
public interface IFeedbackService {

    /**
     * 查询反馈列表
     */
    List<FeedbackVO> getFeedbackList(FeedbackQueryDTO queryDTO);

    /**
     * 根据ID查询反馈详情
     */
    FeedbackVO getFeedbackById(Long feedbackId);

    /**
     * 提交反馈
     */
    int submitFeedback(FeedbackDTO dto);

    /**
     * 更新反馈
     */
    int updateFeedback(FeedbackDTO dto);

    /**
     * 删除反馈
     */
    int deleteFeedback(Long feedbackId);

    /**
     * 批量删除反馈
     */
    int batchDeleteFeedback(Long[] feedbackIds);

    /**
     * 分配处理人
     */
    int assignHandler(Long feedbackId, String handlerId);

    /**
     * 处理反馈
     */
    int handleFeedback(FeedbackHandleDTO dto);

    /**
     * 关闭反馈
     */
    int closeFeedback(Long feedbackId);

    /**
     * 评价反馈
     */
    int evaluateFeedback(FeedbackEvaluationDTO dto);

    /**
     * 回复反馈
     */
    int replyFeedback(FeedbackReplyDTO dto);

    /**
     * 获取反馈统计数据
     */
    FeedbackStatsVO getFeedbackStats();
}
