package com.inspur.agriculture.input.service.dashboard;

import com.inspur.agriculture.input.vo.dashboard.*;

import java.util.List;

/**
 * 大屏数据Service接口
 *
 * @author igdp
 */
public interface IInputDashboardService {

    /**
     * 获取概览统计数据
     * @param year 年份，为空则取当前年
     */
    DashboardOverviewVO getOverviewData(String year);

    /**
     * 获取需求汇聚统计(按类型)
     * @param year 年份
     */
    List<DemandSummaryVO> getDemandSummaryByType(String year);

    /**
     * 获取需求汇聚统计(按地区)
     * @param year 年份
     */
    List<DemandSummaryVO> getDemandSummaryByRegion(String year);

    /**
     * 获取分配进度数据
     * @param year 年份
     */
    AllocationProgressVO getAllocationProgress(String year);

    /**
     * 获取流通状态数据
     * @param year 年份
     */
    CirculationStatusVO getCirculationStatus(String year);

    /**
     * 获取供应商排行
     * @param limit 数量限制，默认10
     */
    List<SupplierRankVO> getTopSuppliers(Integer limit);

    /**
     * 获取最新动态
     * @param limit 数量限制，默认20
     */
    List<RecentActivityVO> getRecentActivities(Integer limit);
}
