package com.inspur.seed.breeding.plotAndSowing.service;

import com.inspur.seed.breeding.plotAndSowing.domain.entity.PlotInfo;

import java.util.List;

/**
 * 地块信息Service接口
 *
 * @author inspur
 */
public interface IPlotInfoService {

    /**
     * 查询地块信息列表
     *
     * @param plotInfo 查询条件
     * @return 地块信息列表
     */
    List<PlotInfo> selectPlotInfoList(PlotInfo plotInfo);

    /**
     * 根据地块ID查询地块详情（含播种信息）
     *
     * @param groundId 地块ID
     * @return 地块信息
     */
    PlotInfo selectPlotInfoById(String groundId);

    /**
     * 新增地块信息（含播种信息）
     *
     * @param plotInfo 地块信息
     * @return 地块ID
     */
    String insertPlotInfo(PlotInfo plotInfo);

    /**
     * 修改地块信息（含播种信息）
     *
     * @param plotInfo 地块信息
     * @return 影响行数
     */
    int updatePlotInfo(PlotInfo plotInfo);

    /**
     * 批量删除地块信息（含关联播种信息）
     *
     * @param plotIds 地块ID数组
     * @return 影响行数
     */
    int deletePlotInfoByIds(String[] plotIds);

    /**
     * 根据批次ID查询地块列表
     *
     * @param batchId 批次ID
     * @return 地块列表
     */
    List<PlotInfo> selectPlotsByBatchId(String batchId);

    /**
     * 获取地块下拉选项列表
     * 支持按批次ID和试验ID过滤
     *
     * @param batchId 批次ID（可选）
     * @param trialId 试验ID（可选）
     * @return 地块选项列表
     */
    List<PlotInfo> selectPlotOptions(String batchId, String trialId);

    /**
     * 提交审核
     *
     * @param plotId 地块ID
     * @return 结果
     */
    int submitAudit(String plotId);

    /**
     * 审核通过
     *
     * @param plotId 地块ID
     * @param auditOpinion 审核意见
     * @return 结果
     */
    int approve(String plotId, String auditOpinion);

    /**
     * 审核退回
     *
     * @param plotId 地块ID
     * @param auditOpinion 审核意见
     * @return 结果
     */
    int reject(String plotId, String auditOpinion);

    /**
     * 归档
     *
     * @param plotId 地块ID
     * @return 结果
     */
    int archive(String plotId);

    /**
     * 作废
     *
     * @param plotId 地块ID
     * @return 结果
     */
    int cancel(String plotId);

    /**
     * 作废审核记录（只作废审核记录，不修改地块数据）
     *
     * @param plotId 地块ID
     * @return 结果
     */
    int cancelAuditRecord(String plotId);
}
