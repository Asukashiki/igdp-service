package com.inspur.seed.breeding.breedingBatch.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.seed.breeding.breedingBatch.domain.entity.ApprovalComment;

import java.util.List;

/**
 * 审批意见 Service接口
 *
 * @author AI Assistant
 * @date 2025-12-15
 */
public interface IApprovalCommentService {

    /**
     * 查询审批意见列表
     *
     * @param approvalComment 审批意见对象
     * @return 审批意见列表
     */
    List<ApprovalComment> selectApprovalCommentList(ApprovalComment approvalComment);

    /**
     * 分页查询审批意见列表
     *
     * @param page 分页对象
     * @param approvalComment 审批意见对象
     * @return 审批意见分页数据
     */
    IPage<ApprovalComment> selectApprovalCommentPage(IPage<ApprovalComment> page, ApprovalComment approvalComment);

    /**
     * 通过ID查询审批意见
     *
     * @param id 审批意见ID
     * @return 审批意见对象
     */
    ApprovalComment selectApprovalCommentById(String id);

    /**
     * 通过数据ID查询审批意见列表
     *
     * @param dataId 数据ID
     * @return 审批意见列表
     */
    List<ApprovalComment> selectApprovalCommentByDataId(String dataId);

    /**
     * 新增审批意见
     *
     * @param approvalComment 审批意见对象
     * @return 结果
     */
    int insertApprovalComment(ApprovalComment approvalComment);

    /**
     * 修改审批意见
     *
     * @param approvalComment 审批意见对象
     * @return 结果
     */
    int updateApprovalComment(ApprovalComment approvalComment);

    /**
     * 删除审批意见
     *
     * @param id 审批意见ID
     * @return 结果
     */
    int deleteApprovalCommentById(String id);

    /**
     * 批量删除审批意见
     *
     * @param ids 审批意见ID数组
     * @return 结果
     */
    int deleteApprovalCommentByIds(String[] ids);
}
