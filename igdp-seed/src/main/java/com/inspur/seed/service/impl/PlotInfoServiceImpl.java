package com.inspur.seed.service.impl;

import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.PlotInfo;
import com.inspur.seed.domain.PlotAuditRecord;
import com.inspur.seed.mapper.PlotInfoMapper;
import com.inspur.seed.service.IPlotInfoService;
import com.inspur.seed.service.IPlotAuditRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 地块信息Service实现类
 *
 * @author inspur
 */
@Service
public class PlotInfoServiceImpl implements IPlotInfoService {

    @Autowired
    private PlotInfoMapper plotInfoMapper;

    @Autowired
    private IPlotAuditRecordService plotAuditRecordService;

    @Override
    public List<PlotInfo> selectPlotInfoList(PlotInfo plotInfo) {
        List<PlotInfo> list = plotInfoMapper.selectPlotInfoList(plotInfo);
        
        // 如果查询的是已审核状态(S2)，需要过滤掉已作废的审核记录
        if ("S2".equals(plotInfo.getAuditStatus())) {
            list = list.stream()
                    .filter(item -> item.getAuditCanceled() == null || item.getAuditCanceled() == 0)
                    .collect(java.util.stream.Collectors.toList());
        }
        // 如果查询的是已作废状态(S10)，只返回审核记录被作废的数据
        // 注意：主表状态为S10的记录（通过cancel方法作废的）不应该在审核页面显示
        else if ("S10".equals(plotInfo.getAuditStatus())) {
            // 只查询主表状态为S2但审核记录被作废的记录（通过cancelAuditRecord方法作废的）
            PlotInfo s2Query = new PlotInfo();
            s2Query.setAuditStatus("S2");
            s2Query.setBatchId(plotInfo.getBatchId());
            s2Query.setTrialId(plotInfo.getTrialId());
            s2Query.setVarietyCode(plotInfo.getVarietyCode());
            
            list = plotInfoMapper.selectPlotInfoList(s2Query).stream()
                    .filter(item -> item.getAuditCanceled() != null && item.getAuditCanceled() > 0)
                    .collect(java.util.stream.Collectors.toList());
        }
        
        return list;
    }

    @Override
    public PlotInfo selectPlotInfoById(String plotId) {
        return plotInfoMapper.selectPlotInfoById(plotId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertPlotInfo(PlotInfo plotInfo) {
        // 生成地块ID: {trial_id}-P{replication_no}{row_no}{column_no}
        String plotId = generatePlotId(plotInfo.getTrialId(), plotInfo.getReplicationNo(),
                                        plotInfo.getRowNo(), plotInfo.getColumnNo());
        plotInfo.setPlotId(plotId);

        // 设置创建信息
        plotInfo.setCreateTime(LocalDateTime.now());
        // BaseEntity: createBy 存用户ID
        plotInfo.setCreateBy(SecurityUtils.getUserId().toString());
        // 业务字段：createdBy/createdName 存ID与姓名，供列表显示
        plotInfo.setCreatedBy(SecurityUtils.getUserId().toString());
        plotInfo.setCreatedName(SecurityUtils.getUsername());

        // 设置默认审核状态为草稿
        plotInfo.setAuditStatus("S0");

        // 保存地块信息
        plotInfoMapper.insert(plotInfo);

        return plotId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePlotInfo(PlotInfo plotInfo) {
        // 设置更新信息
        plotInfo.setUpdateTime(LocalDateTime.now());
        // BaseEntity: updateBy 存用户ID
        plotInfo.setUpdateBy(SecurityUtils.getUserId().toString());
        // 业务字段：modifiedBy/modifiedName 供列表显示
        plotInfo.setModifiedBy(SecurityUtils.getUserId().toString());
        plotInfo.setModifiedName(SecurityUtils.getUsername());

        // 更新地块信息
        return plotInfoMapper.updateById(plotInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePlotInfoByIds(String[] plotIds) {
        int count = 0;
        for (String plotId : plotIds) {
            // 删除地块信息 - 使用deleteById方法，让@TableLogic自动处理逻辑删除
            boolean success = plotInfoMapper.deleteById(plotId) > 0;
            if (success) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<PlotInfo> selectPlotsByBatchId(String batchId) {
        return plotInfoMapper.selectPlotsByBatchId(batchId);
    }

    @Override
    public List<PlotInfo> selectPlotOptions(String batchId, String trialId) {
        return plotInfoMapper.selectPlotOptions(batchId, trialId);
    }

    /**
     * 生成地块ID
     * 格式: {trial_id}-P{replication_no}{row_no}{column_no}
     */
    private String generatePlotId(String trialId, Integer replicationNo, Integer rowNo, Integer columnNo) {
        if (trialId == null || trialId.isEmpty()) {
            throw new ServiceException("试验ID不能为空");
        }
        if (replicationNo == null) {
            throw new ServiceException("重复组编号不能为空");
        }
        if (rowNo == null) {
            throw new ServiceException("行号不能为空");
        }
        if (columnNo == null) {
            throw new ServiceException("列号不能为空");
        }

        return String.format("%s-P%d%d%d", trialId, replicationNo, rowNo, columnNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submitAudit(String plotId) {
        PlotInfo plotInfo = plotInfoMapper.selectPlotInfoById(plotId);
        if (plotInfo == null) {
            throw new ServiceException("地块不存在");
        }
        if (!"S0".equals(plotInfo.getAuditStatus()) && !"S3".equals(plotInfo.getAuditStatus())) {
            throw new ServiceException("只有草稿或已退回状态才能提交审核");
        }

        String beforeStatus = plotInfo.getAuditStatus();
        plotInfo.setAuditStatus("S1");
        plotInfoMapper.updateById(plotInfo);

        PlotAuditRecord record = new PlotAuditRecord();
        record.setPlotId(plotId);
        record.setAuditType("SUBMIT");
        record.setBeforeStatus(beforeStatus);
        record.setAfterStatus("S1");
        record.setAuditor(SecurityUtils.getUserId().toString());
        record.setAuditorName(SecurityUtils.getUsername());
        record.setAuditTime(LocalDateTime.now());
        plotAuditRecordService.insertAuditRecord(record);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(String plotId, String auditOpinion) {
        PlotInfo plotInfo = plotInfoMapper.selectPlotInfoById(plotId);
        if (plotInfo == null) {
            throw new ServiceException("地块不存在");
        }
        if (!"S1".equals(plotInfo.getAuditStatus())) {
            throw new ServiceException("只有待审批状态才能审核通过");
        }

        plotInfo.setAuditStatus("S2");
        plotInfo.setAuditOpinion(auditOpinion);
        plotInfo.setAuditedBy(SecurityUtils.getUserId().toString());
        plotInfo.setAuditedName(SecurityUtils.getUsername());
        plotInfo.setAuditTime(LocalDateTime.now());
        plotInfoMapper.updateById(plotInfo);

        PlotAuditRecord record = new PlotAuditRecord();
        record.setPlotId(plotId);
        record.setAuditType("APPROVE");
        record.setBeforeStatus("S1");
        record.setAfterStatus("S2");
        record.setAuditOpinion(auditOpinion);
        record.setAuditor(SecurityUtils.getUserId().toString());
        record.setAuditorName(SecurityUtils.getUsername());
        record.setAuditTime(LocalDateTime.now());
        plotAuditRecordService.insertAuditRecord(record);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reject(String plotId, String auditOpinion) {
        PlotInfo plotInfo = plotInfoMapper.selectPlotInfoById(plotId);
        if (plotInfo == null) {
            throw new ServiceException("地块不存在");
        }
        if (!"S1".equals(plotInfo.getAuditStatus())) {
            throw new ServiceException("只有待审批状态才能退回");
        }
        if (auditOpinion == null || auditOpinion.trim().isEmpty()) {
            throw new ServiceException("退回必须填写审核意见");
        }

        plotInfo.setAuditStatus("S3");
        plotInfo.setAuditOpinion(auditOpinion);
        plotInfo.setAuditedBy(SecurityUtils.getUserId().toString());
        plotInfo.setAuditedName(SecurityUtils.getUsername());
        plotInfo.setAuditTime(LocalDateTime.now());
        plotInfoMapper.updateById(plotInfo);

        PlotAuditRecord record = new PlotAuditRecord();
        record.setPlotId(plotId);
        record.setAuditType("REJECT");
        record.setBeforeStatus("S1");
        record.setAfterStatus("S3");
        record.setAuditOpinion(auditOpinion);
        record.setAuditor(SecurityUtils.getUserId().toString());
        record.setAuditorName(SecurityUtils.getUsername());
        record.setAuditTime(LocalDateTime.now());
        plotAuditRecordService.insertAuditRecord(record);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int archive(String plotId) {
        PlotInfo plotInfo = plotInfoMapper.selectPlotInfoById(plotId);
        if (plotInfo == null) {
            throw new ServiceException("地块不存在");
        }
        if (!"S2".equals(plotInfo.getAuditStatus())) {
            throw new ServiceException("只有已审批状态才能归档");
        }

        plotInfo.setAuditStatus("S9");
        plotInfoMapper.updateById(plotInfo);

        PlotAuditRecord record = new PlotAuditRecord();
        record.setPlotId(plotId);
        record.setAuditType("ARCHIVE");
        record.setBeforeStatus("S2");
        record.setAfterStatus("S9");
        record.setAuditor(SecurityUtils.getUserId().toString());
        record.setAuditorName(SecurityUtils.getUsername());
        record.setAuditTime(LocalDateTime.now());
        plotAuditRecordService.insertAuditRecord(record);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancel(String plotId) {
        PlotInfo plotInfo = plotInfoMapper.selectPlotInfoById(plotId);
        if (plotInfo == null) {
            throw new ServiceException("地块不存在");
        }
        if (!"S0".equals(plotInfo.getAuditStatus()) && !"S3".equals(plotInfo.getAuditStatus())) {
            throw new ServiceException("只有草稿或已退回状态才能作废");
        }

        String beforeStatus = plotInfo.getAuditStatus();
        plotInfo.setAuditStatus("S10");
        plotInfoMapper.updateById(plotInfo);

        PlotAuditRecord record = new PlotAuditRecord();
        record.setPlotId(plotId);
        record.setAuditType("CANCEL");
        record.setBeforeStatus(beforeStatus);
        record.setAfterStatus("S10");
        record.setAuditor(SecurityUtils.getUserId().toString());
        record.setAuditorName(SecurityUtils.getUsername());
        record.setAuditTime(LocalDateTime.now());
        plotAuditRecordService.insertAuditRecord(record);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelAuditRecord(String plotId) {
        PlotInfo plotInfo = plotInfoMapper.selectPlotInfoById(plotId);
        if (plotInfo == null) {
            throw new ServiceException("地块不存在");
        }
        if (!"S2".equals(plotInfo.getAuditStatus())) {
            throw new ServiceException("只有已审批状态才能作废");
        }

        // 注意：作废审核记录时，只在审核记录表中记录作废操作
        // 不修改主表（plot_info）的状态，保持主表数据不受影响
        String beforeStatus = plotInfo.getAuditStatus();

        // 写入审核记录 - 记录作废操作，但主表状态保持为 S2
        PlotAuditRecord record = new PlotAuditRecord();
        record.setPlotId(plotId);
        record.setAuditType("CANCEL_AUDIT");
        record.setBeforeStatus(beforeStatus);
        record.setAfterStatus(beforeStatus); // 主表状态不变，仍为 S2
        record.setAuditOpinion("作废审核记录");
        record.setAuditor(SecurityUtils.getUserId().toString());
        record.setAuditorName(SecurityUtils.getUsername());
        record.setAuditTime(LocalDateTime.now());
        plotAuditRecordService.insertAuditRecord(record);

        return 1;
    }
}
