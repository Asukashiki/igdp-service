package com.inspur.agriculture.input.service.feedback.impl;

import com.inspur.agriculture.input.domain.feedback.Feedback;
import com.inspur.agriculture.input.domain.feedback.FeedbackReply;
import com.inspur.agriculture.input.domain.feedback.enums.FeedbackPriorityEnum;
import com.inspur.agriculture.input.domain.feedback.enums.FeedbackStatusEnum;
import com.inspur.agriculture.input.domain.feedback.enums.FeedbackTypeEnum;
import com.inspur.agriculture.input.dto.feedback.*;
import com.inspur.agriculture.input.mapper.feedback.FeedbackMapper;
import com.inspur.agriculture.input.mapper.feedback.FeedbackReplyMapper;
import com.inspur.agriculture.input.service.feedback.IFeedbackService;
import com.inspur.agriculture.input.vo.feedback.FeedbackReplyVO;
import com.inspur.agriculture.input.vo.feedback.FeedbackStatsVO;
import com.inspur.agriculture.input.vo.feedback.FeedbackVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 反馈Service业务层处理
 *
 * @author igdp
 */
@Service
public class FeedbackServiceImpl implements IFeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private FeedbackReplyMapper feedbackReplyMapper;

    @Override
    public List<FeedbackVO> getFeedbackList(FeedbackQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new FeedbackQueryDTO();
        }
        List<FeedbackVO> list = feedbackMapper.selectFeedbackList(queryDTO);
        // 补充枚举描述
        for (FeedbackVO vo : list) {
            enrichFeedbackVO(vo);
        }
        return list;
    }

    @Override
    public FeedbackVO getFeedbackById(Long feedbackId) {
        FeedbackVO vo = feedbackMapper.selectFeedbackById(feedbackId);
        if (vo != null) {
            enrichFeedbackVO(vo);
            // 查询回复列表
            List<FeedbackReplyVO> replies = feedbackReplyMapper.selectByFeedbackId(feedbackId);
            // 处理附件
            for (FeedbackReplyVO reply : replies) {
                if (reply.getAttachments() != null && !reply.getAttachments().isEmpty()) {
                    reply.setAttachmentList(Arrays.asList(reply.getAttachments().split(",")));
                }
            }
            vo.setReplies(replies);
            // 处理附件
            if (vo.getAttachments() != null && !vo.getAttachments().isEmpty()) {
                vo.setAttachmentList(Arrays.asList(vo.getAttachments().split(",")));
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int submitFeedback(FeedbackDTO dto) {
        String feedbackNo = feedbackMapper.generateFeedbackNo();

        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(dto, feedback);
        feedback.setFeedbackNo(feedbackNo);
        feedback.setStatus("0"); // 待处理
        feedback.setCreateTime(new Date());
        try {
            String userId = SecurityUtils.getUserId().toString();
            feedback.setCreateBy(SecurityUtils.getUsername());
            // 判断用户类型（可以根据角色判断）
        } catch (Exception e) {
            feedback.setCreateBy("anonymous");
        }
        feedback.setDelFlag("0");

        // 如果没有设置优先级，默认为低
        if (feedback.getPriority() == null || feedback.getPriority().isEmpty()) {
            feedback.setPriority("0");
        }

        return feedbackMapper.insert(feedback);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateFeedback(FeedbackDTO dto) {
        Feedback existFeedback = feedbackMapper.selectById(dto.getFeedbackId());
        if (existFeedback == null || "2".equals(existFeedback.getDelFlag())) {
            throw new ServiceException("反馈不存在");
        }
        // 只有待处理状态可以修改
        if (!"0".equals(existFeedback.getStatus())) {
            throw new ServiceException("只能修改待处理状态的反馈");
        }

        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(dto, feedback);
        feedback.setUpdateTime(new Date());
        try {
            feedback.setUpdateBy(SecurityUtils.getUsername());
        } catch (Exception e) {
            feedback.setUpdateBy("system");
        }

        return feedbackMapper.updateById(feedback);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteFeedback(Long feedbackId) {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(feedbackId);
        feedback.setDelFlag("2");
        feedback.setUpdateTime(new Date());
        try {
            feedback.setUpdateBy(SecurityUtils.getUsername());
        } catch (Exception e) {
            feedback.setUpdateBy("system");
        }
        return feedbackMapper.updateById(feedback);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchDeleteFeedback(Long[] feedbackIds) {
        int rows = 0;
        for (Long id : feedbackIds) {
            rows += deleteFeedback(id);
        }
        return rows;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int assignHandler(Long feedbackId, String handlerId) {
        Feedback existFeedback = feedbackMapper.selectById(feedbackId);
        if (existFeedback == null || "2".equals(existFeedback.getDelFlag())) {
            throw new ServiceException("反馈不存在");
        }

        Feedback feedback = new Feedback();
        feedback.setFeedbackId(feedbackId);
        feedback.setHandlerId(handlerId);
        feedback.setStatus("1"); // 处理中
        feedback.setUpdateTime(new Date());
        try {
            feedback.setUpdateBy(SecurityUtils.getUsername());
        } catch (Exception e) {
            feedback.setUpdateBy("system");
        }

        return feedbackMapper.updateById(feedback);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int handleFeedback(FeedbackHandleDTO dto) {
        Feedback existFeedback = feedbackMapper.selectById(dto.getFeedbackId());
        if (existFeedback == null || "2".equals(existFeedback.getDelFlag())) {
            throw new ServiceException("反馈不存在");
        }

        Feedback feedback = new Feedback();
        feedback.setFeedbackId(dto.getFeedbackId());
        feedback.setHandleResult(dto.getHandleResult());
        feedback.setHandleRemark(dto.getHandleRemark());
        feedback.setStatus(dto.getStatus());
        feedback.setHandleTime(new Date());
        feedback.setUpdateTime(new Date());

        try {
            String userId = SecurityUtils.getUserId().toString();
            if (existFeedback.getHandlerId() == null) {
                feedback.setHandlerId(userId);
            }
            feedback.setUpdateBy(SecurityUtils.getUsername());
        } catch (Exception e) {
            feedback.setUpdateBy("system");
        }

        return feedbackMapper.updateById(feedback);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int closeFeedback(Long feedbackId) {
        Feedback existFeedback = feedbackMapper.selectById(feedbackId);
        if (existFeedback == null || "2".equals(existFeedback.getDelFlag())) {
            throw new ServiceException("反馈不存在");
        }

        Feedback feedback = new Feedback();
        feedback.setFeedbackId(feedbackId);
        feedback.setStatus("3"); // 已关闭
        feedback.setUpdateTime(new Date());
        try {
            feedback.setUpdateBy(SecurityUtils.getUsername());
        } catch (Exception e) {
            feedback.setUpdateBy("system");
        }

        return feedbackMapper.updateById(feedback);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int evaluateFeedback(FeedbackEvaluationDTO dto) {
        Feedback existFeedback = feedbackMapper.selectById(dto.getFeedbackId());
        if (existFeedback == null || "2".equals(existFeedback.getDelFlag())) {
            throw new ServiceException("反馈不存在");
        }
        // 只有已完成的反馈可以评价
        if (!"2".equals(existFeedback.getStatus())) {
            throw new ServiceException("只能评价已完成的反馈");
        }

        Feedback feedback = new Feedback();
        feedback.setFeedbackId(dto.getFeedbackId());
        feedback.setSatisfaction(dto.getSatisfaction());
        feedback.setEvaluation(dto.getEvaluation());
        feedback.setEvaluationTime(new Date());
        feedback.setUpdateTime(new Date());
        try {
            feedback.setUpdateBy(SecurityUtils.getUsername());
        } catch (Exception e) {
            feedback.setUpdateBy("system");
        }

        return feedbackMapper.updateById(feedback);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int replyFeedback(FeedbackReplyDTO dto) {
        Feedback existFeedback = feedbackMapper.selectById(dto.getFeedbackId());
        if (existFeedback == null || "2".equals(existFeedback.getDelFlag())) {
            throw new ServiceException("反馈不存在");
        }

        FeedbackReply reply = new FeedbackReply();
        reply.setFeedbackId(dto.getFeedbackId());
        reply.setContent(dto.getContent());
        reply.setAttachments(dto.getAttachments());
        reply.setCreateTime(new Date());
        reply.setDelFlag("0");

        try {
            String userId = SecurityUtils.getUserId().toString();
            reply.setReplyUserId(userId);
            reply.setCreateBy(SecurityUtils.getUsername());
            // 判断是否为处理人员
            if (userId.equals(existFeedback.getHandlerId())) {
                reply.setReplyUserType("1"); // 处理人员
            } else {
                reply.setReplyUserType("0"); // 用户
            }
        } catch (Exception e) {
            reply.setReplyUserId("system");
            reply.setCreateBy("system");
            reply.setReplyUserType("1");
        }

        return feedbackReplyMapper.insert(reply);
    }

    @Override
    public FeedbackStatsVO getFeedbackStats() {
        return feedbackMapper.getFeedbackStats();
    }

    /**
     * 补充VO枚举描述
     */
    private void enrichFeedbackVO(FeedbackVO vo) {
        if (vo.getFeedbackType() != null) {
            vo.setFeedbackTypeDesc(FeedbackTypeEnum.getDescByCode(vo.getFeedbackType()));
        }
        if (vo.getStatus() != null) {
            vo.setStatusDesc(FeedbackStatusEnum.getDescByCode(vo.getStatus()));
        }
        if (vo.getPriority() != null) {
            vo.setPriorityDesc(FeedbackPriorityEnum.getDescByCode(vo.getPriority()));
        }
    }
}
