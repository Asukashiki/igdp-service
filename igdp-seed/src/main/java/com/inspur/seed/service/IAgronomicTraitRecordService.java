package com.inspur.seed.service;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.AgronomicTraitRecordDTO;
import com.inspur.seed.domain.entity.AgronomicTraitRecord;
import com.inspur.seed.domain.vo.AgronomicTraitRecordVO;

import java.util.List;

/**
 * 农艺性状采集主记录Service接口
 * 
 * @author inspur
 */
public interface IAgronomicTraitRecordService {

    /**
     * 查询主记录列表
     * 
     * @param record 查询条件
     * @return 主记录列表
     */
    List<AgronomicTraitRecord> selectRecordList(AgronomicTraitRecord record);

    /**
     * 根据ID查询主记录详情（含明细）
     * 
     * @param recordId 记录ID
     * @return 主记录详情
     */
    AgronomicTraitRecordVO selectRecordById(String recordId);

    /**
     * 新增主记录（含明细）
     * 
     * @param dto 主记录DTO
     * @return 记录ID
     */
    String insertRecord(AgronomicTraitRecordDTO dto);

    /**
     * 更新主记录（含明细）
     * 
     * @param dto 主记录DTO
     * @return 影响行数
     */
    int updateRecord(AgronomicTraitRecordDTO dto);

    /**
     * 批量删除主记录（逻辑删除，级联删除明细）
     * 
     * @param recordIds 记录ID数组
     * @return 影响行数
     */
    int deleteRecordByIds(String[] recordIds);

    /**
     * 生成记录ID
     * 
     * @param plotId 地块ID
     * @return 记录ID
     */
    String generateRecordId(String plotId);

    /**
     * 提交审核
     *
     * @param id 数据集ID
     * @return 操作结果
     */
    AjaxResult submitAgronomicTraitAudit(String id);
}
