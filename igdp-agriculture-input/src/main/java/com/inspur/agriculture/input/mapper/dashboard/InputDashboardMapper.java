package com.inspur.agriculture.input.mapper.dashboard;

import com.inspur.agriculture.input.vo.dashboard.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 大屏数据Mapper接口
 *
 * @author igdp
 */
public interface InputDashboardMapper {

    /**
     * 获取概览统计数据
     * @param year 年份
     */
    DashboardOverviewVO getOverviewData(@Param("year") String year);

    /**
     * 获取需求汇聚统计(按类型)
     * @param year 年份
     */
    List<DemandSummaryVO> getDemandSummaryByType(@Param("year") String year);

    /**
     * 获取需求汇聚统计(按地区)
     * @param year 年份
     */
    List<DemandSummaryVO> getDemandSummaryByRegion(@Param("year") String year);

    /**
     * 获取分配进度数据
     * @param year 年份
     */
    AllocationProgressVO getAllocationProgress(@Param("year") String year);

    /**
     * 获取流通状态数据
     * @param year 年份
     */
    CirculationStatusVO getCirculationStatus(@Param("year") String year);

    /**
     * 获取供应商排行
     * @param limit 数量限制
     */
    List<SupplierRankVO> getTopSuppliers(@Param("limit") Integer limit);

    /**
     * 获取最新动态
     * @param limit 数量限制
     */
    List<RecentActivityVO> getRecentActivities(@Param("limit") Integer limit);
}
