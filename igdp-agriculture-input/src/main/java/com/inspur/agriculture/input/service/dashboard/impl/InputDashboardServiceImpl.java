package com.inspur.agriculture.input.service.dashboard.impl;

import com.inspur.agriculture.input.mapper.dashboard.InputDashboardMapper;
import com.inspur.agriculture.input.service.dashboard.IInputDashboardService;
import com.inspur.agriculture.input.vo.dashboard.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

/**
 * 大屏数据Service业务层实现
 *
 * @author igdp
 */
@Service
public class InputDashboardServiceImpl implements IInputDashboardService {

    @Autowired
    private InputDashboardMapper inputDashboardMapper;

    /**
     * 获取当前年份
     */
    private String getCurrentYear() {
        return String.valueOf(Year.now().getValue());
    }

    /**
     * 获取有效年份（为空则返回当前年）
     */
    private String getValidYear(String year) {
        return (year == null || year.isEmpty()) ? getCurrentYear() : year;
    }

    @Override
    public DashboardOverviewVO getOverviewData(String year) {
        String validYear = getValidYear(year);
        DashboardOverviewVO result = inputDashboardMapper.getOverviewData(validYear);
        if (result == null) {
            result = new DashboardOverviewVO();
        }
        return result;
    }

    @Override
    public List<DemandSummaryVO> getDemandSummaryByType(String year) {
        String validYear = getValidYear(year);
        return inputDashboardMapper.getDemandSummaryByType(validYear);
    }

    @Override
    public List<DemandSummaryVO> getDemandSummaryByRegion(String year) {
        String validYear = getValidYear(year);
        return inputDashboardMapper.getDemandSummaryByRegion(validYear);
    }

    @Override
    public AllocationProgressVO getAllocationProgress(String year) {
        String validYear = getValidYear(year);
        AllocationProgressVO result = inputDashboardMapper.getAllocationProgress(validYear);
        if (result == null) {
            result = new AllocationProgressVO();
        }
        return result;
    }

    @Override
    public CirculationStatusVO getCirculationStatus(String year) {
        String validYear = getValidYear(year);
        CirculationStatusVO result = inputDashboardMapper.getCirculationStatus(validYear);
        if (result == null) {
            result = new CirculationStatusVO();
        }
        return result;
    }

    @Override
    public List<SupplierRankVO> getTopSuppliers(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        List<SupplierRankVO> list = inputDashboardMapper.getTopSuppliers(limit);
        // 设置排名
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(i + 1);
        }
        return list;
    }

    @Override
    public List<RecentActivityVO> getRecentActivities(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 20;
        }
        return inputDashboardMapper.getRecentActivities(limit);
    }
}
