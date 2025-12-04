package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.PlotInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 地块信息Mapper接口
 *
 * @author inspur
 */
@Mapper
public interface PlotInfoMapper extends BaseMapper<PlotInfo> {

    /**
     * 查询地块信息列表
     *
     * @param plotInfo 查询条件
     * @return 地块信息列表
     */
    List<PlotInfo> selectPlotInfoList(PlotInfo plotInfo);

    /**
     * 根据地块ID查询地块详情
     *
     * @param plotId 地块ID
     * @return 地块信息
     */
    PlotInfo selectPlotInfoById(@Param("plotId") String plotId);

    /**
     * 根据试验ID查询关联地块列表
     *
     * @param trialId 试验ID
     * @return 地块列表
     */
    List<PlotInfo> selectPlotsByTrialId(@Param("trialId") String trialId);

    /**
     * 根据批次ID查询地块列表
     *
     * @param batchId 批次ID
     * @return 地块列表
     */
    List<PlotInfo> selectPlotsByBatchId(@Param("batchId") String batchId);

    /**
     * 获取地块下拉选项列表
     * 支持按批次ID和试验ID过滤
     *
     * @param batchId 批次ID（可选）
     * @param trialId 试验ID（可选）
     * @return 地块选项列表
     */
    List<PlotInfo> selectPlotOptions(@Param("batchId") String batchId, @Param("trialId") String trialId);
}
