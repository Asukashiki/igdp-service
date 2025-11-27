package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.VarietyAudit;
import com.inspur.seed.domain.vo.VarietyAuditTaskVO;

import java.util.List;

/**
 * 品种审核服务接口
 *
 * @author system
 */
public interface IVarietyAuditService extends IService<VarietyAudit> {

    /**
     * 处理品种审核
     *
     * @param varietyAudit 审核信息
     * @return 审核ID
     */
    String handleAudit(VarietyAudit varietyAudit);

    /**
     * 查询审核记录列表
     *
     * @param varietyName 品种名称
     * @param enterpriseName 提交单位
     * @param auditResult 审核结果
     * @return 审核记录列表
     */
    List<VarietyAudit> queryAuditList(String varietyName, String enterpriseName, Integer auditResult);

    /**
     * 根据审核ID查询审核记录
     *
     * @param auditId 审核ID
     * @return 审核记录
     */
    VarietyAudit queryByAuditId(String auditId);

    /**
     * 根据登记ID查询最新审核记录
     *
     * @param registrationId 登记ID
     * @return 最新审核记录
     */
    VarietyAudit queryLatestByRegistrationId(String registrationId);
    
    /**
     * 查询审核任务列表（返回VO对象）
     *
     * @param varietyName 品种名称
     * @param enterpriseName 提交单位
     * @param auditResult 审核结果
     * @return 审核任务VO列表
     */
    List<VarietyAuditTaskVO> queryAuditTaskList(String varietyName, String enterpriseName, Integer auditResult);
    
    /**
     * 根据登记ID查询审核任务详情
     *
     * @param registrationId 登记ID
     * @return 审核任务详情VO
     */
    VarietyAuditTaskVO queryAuditTaskDetail(String registrationId);
}