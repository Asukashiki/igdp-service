package com.inspur.seed.service.impl;

import com.inspur.seed.domain.PlotAuditRecord;
import com.inspur.seed.mapper.PlotAuditRecordMapper;
import com.inspur.seed.service.IPlotAuditRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地块审核记录Service实现
 *
 * @author inspur
 */
@Service
public class PlotAuditRecordServiceImpl implements IPlotAuditRecordService {

    @Autowired
    private PlotAuditRecordMapper plotAuditRecordMapper;

    @Override
    public int insertAuditRecord(PlotAuditRecord record) {
        return plotAuditRecordMapper.insert(record);
    }

    @Override
    public List<PlotAuditRecord> selectAuditHistory(String plotId) {
        return plotAuditRecordMapper.selectAuditHistory(plotId);
    }
}
