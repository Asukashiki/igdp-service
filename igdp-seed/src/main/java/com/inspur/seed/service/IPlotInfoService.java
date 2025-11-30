package com.inspur.seed.service;

import com.inspur.seed.domain.PlotInfo;

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
     * @param groundIds 地块ID数组
     * @return 影响行数
     */
    int deletePlotInfoByIds(String[] groundIds);

    /**
     * 根据批次ID查询地块列表
     *
     * @param batchId 批次ID
     * @return 地块列表
     */
    List<PlotInfo> selectPlotsByBatchId(String batchId);
}
