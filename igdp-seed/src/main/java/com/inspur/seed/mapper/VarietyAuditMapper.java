package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.VarietyAudit;
import com.inspur.seed.domain.vo.VarietyAuditTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 品种审核记录Mapper接口
 *
 * @author system
 */
@Mapper
public interface VarietyAuditMapper extends BaseMapper<VarietyAudit> {
    
    /**
     * 查询审核任务列表，关联品种登记表
     * @param varietyName 品种名称
     * @param enterpriseName 企业名称
     * @param auditResult 审核结果
     * @return 审核任务VO列表
     */
    List<VarietyAuditTaskVO> selectAuditTaskList(@Param("varietyName") String varietyName, 
                                                @Param("enterpriseName") String enterpriseName, 
                                                @Param("auditResult") Integer auditResult);
    
    /**
     * 根据登记ID查询审核任务详情，关联品种登记表
     * @param registrationId 登记ID
     * @return 审核任务VO
     */
    VarietyAuditTaskVO selectAuditTaskDetail(@Param("registrationId") String registrationId);
}