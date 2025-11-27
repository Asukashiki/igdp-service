package com.inspur.agriculture.input.mapper.feedback;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.feedback.FeedbackReply;
import com.inspur.agriculture.input.vo.feedback.FeedbackReplyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 反馈回复Mapper接口
 *
 * @author igdp
 */
public interface FeedbackReplyMapper extends BaseMapper<FeedbackReply> {

    /**
     * 根据反馈ID查询回复列表
     *
     * @param feedbackId 反馈ID
     * @return 回复列表
     */
    List<FeedbackReplyVO> selectByFeedbackId(@Param("feedbackId") Long feedbackId);
}
