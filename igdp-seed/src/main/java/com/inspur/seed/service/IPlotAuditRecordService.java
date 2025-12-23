package com.inspur.seed.service;

import com.inspur.seed.domain.PlotAuditRecord;

import java.util.List;

/**
 * 地块审核记录Service接口
 *
 * @author inspur
 */
public interface IPlotAuditRecordService {

    /**
     * 新增审核记录
     *
     * @param record 审核记录
     * @return 结果
     */
    int insertAuditRecord(PlotAuditRecord record);

    /**
     * 查询地块审核历史
     *
     * @param plotId 地块ID
     * @return 审核记录列表
     */
    List<PlotAuditRecord> selectAuditHistory(String plotId);
}
