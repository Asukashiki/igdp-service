package com.inspur.agriculture.input.mapper.feedback;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.feedback.Feedback;
import com.inspur.agriculture.input.dto.feedback.FeedbackQueryDTO;
import com.inspur.agriculture.input.vo.feedback.FeedbackStatsVO;
import com.inspur.agriculture.input.vo.feedback.FeedbackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 反馈Mapper接口
 *
 * @author igdp
 */
public interface FeedbackMapper extends BaseMapper<Feedback> {

    /**
     * 查询反馈列表
     *
     * @param queryDTO 查询条件
     * @return 反馈列表
     */
    List<FeedbackVO> selectFeedbackList(@Param("query") FeedbackQueryDTO queryDTO);

    /**
     * 根据ID查询反馈详情
     *
     * @param feedbackId 反馈ID
     * @return 反馈详情
     */
    FeedbackVO selectFeedbackById(@Param("feedbackId") Long feedbackId);

    /**
     * 生成反馈编号
     *
     * @return 反馈编号
     */
    String generateFeedbackNo();

    /**
     * 获取反馈统计数据
     *
     * @return 统计数据
     */
    FeedbackStatsVO getFeedbackStats();
}
