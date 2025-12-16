package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.entity.ApprovalComment;
import com.inspur.seed.mapper.ApprovalCommentMapper;
import com.inspur.seed.service.IApprovalCommentService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 审批意见 Service实现类
 * 
 * @author AI Assistant
 * @date 2025-12-15
 */
@Service
public class ApprovalCommentServiceImpl extends ServiceImpl<ApprovalCommentMapper, ApprovalComment> implements IApprovalCommentService {

    @Override
    public List<ApprovalComment> selectApprovalCommentList(ApprovalComment approvalComment) {
        QueryWrapper<ApprovalComment> queryWrapper = new QueryWrapper<>();
        
        // 根据数据ID查询
        if (approvalComment.getDataId() != null && !approvalComment.getDataId().isEmpty()) {
            queryWrapper.eq("data_id", approvalComment.getDataId());
        }
        
        // 根据审批人ID查询
        if (approvalComment.getApproverId() != null && !approvalComment.getApproverId().isEmpty()) {
            queryWrapper.eq("approver_id", approvalComment.getApproverId());
        }
        
        // 根据审批人名称查询
        if (approvalComment.getApproverName() != null && !approvalComment.getApproverName().isEmpty()) {
            queryWrapper.like("approver_name", approvalComment.getApproverName());
        }
        
        return list(queryWrapper);
    }

    @Override
    public IPage<ApprovalComment> selectApprovalCommentPage(IPage<ApprovalComment> page, ApprovalComment approvalComment) {
        QueryWrapper<ApprovalComment> queryWrapper = new QueryWrapper<>();
        
        // 根据数据ID查询
        if (approvalComment.getDataId() != null && !approvalComment.getDataId().isEmpty()) {
            queryWrapper.eq("data_id", approvalComment.getDataId());
        }
        
        // 根据审批人ID查询
        if (approvalComment.getApproverId() != null && !approvalComment.getApproverId().isEmpty()) {
            queryWrapper.eq("approver_id", approvalComment.getApproverId());
        }
        
        // 根据审批人名称查询
        if (approvalComment.getApproverName() != null && !approvalComment.getApproverName().isEmpty()) {
            queryWrapper.like("approver_name", approvalComment.getApproverName());
        }
        
        return page(page, queryWrapper);
    }

    @Override
    public ApprovalComment selectApprovalCommentById(String id) {
        return getById(id);
    }

    @Override
    public List<ApprovalComment> selectApprovalCommentByDataId(String dataId) {
        QueryWrapper<ApprovalComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_id", dataId);
        queryWrapper.orderByDesc("create_time");
        return list(queryWrapper);
    }

    @Override
    public int insertApprovalComment(ApprovalComment approvalComment) {
        return baseMapper.insert(approvalComment) > 0 ? 1 : 0;
    }

    @Override
    public int updateApprovalComment(ApprovalComment approvalComment) {
        return baseMapper.updateById(approvalComment) > 0 ? 1 : 0;
    }

    @Override
    public int deleteApprovalCommentById(String id) {
        return baseMapper.deleteById(id) > 0 ? 1 : 0;
    }

    @Override
    public int deleteApprovalCommentByIds(String[] ids) {
        return baseMapper.deleteBatchIds(Arrays.asList(ids)) > 0 ? 1 : 0;
    }
}